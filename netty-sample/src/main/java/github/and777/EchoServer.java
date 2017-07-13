package github.and777;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author edliao on 2017/7/4.
 * @description 基本应答服务器
 */
public class EchoServer {

  private final Integer port;

  public EchoServer(Integer port) {
    this.port = port;
  }

  public void start() throws Exception {
    //使用NIO Group接受和处理新连接 : 连接/读写
    EventLoopGroup group = new NioEventLoopGroup();
    try {
      //创建服务器启动对象 : 绑定和启动服务器
      ServerBootstrap b = new ServerBootstrap();
      b.group(group)
          .channel(NioServerSocketChannel.class) //指定通道类型是NIO Socket Channel
          .localAddress(port) //指定端口
          //指定连接后调用的handler
          .childHandler(
              //通过 ChannelInitializer 把 ChannelHandler链 安装到 pipeline
              new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel channel) throws Exception {
                  channel.pipeline().addLast(new EchoServerHandler());
                }
              }
          );
      //绑定服务器 ,并等待绑定完成
      ChannelFuture f = b.bind().sync();
      System.out.println(EchoServer.class.getName() + "started and listen on " + f.channel().localAddress());

      //关闭
      f.channel().closeFuture().sync();
    } finally {
      group.shutdownGracefully().sync();
    }
  }

  //监听服务器接受的事件
  class EchoServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
      System.out.println("Server received: " + msg);
      ctx.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
      System.out.println("Read Complete");
      //关闭这Channel
      ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      cause.printStackTrace();
      ctx.close();
    }
  }

  public static void main(String[] args) throws Exception {
    new EchoServer(65535).start();
  }
}
