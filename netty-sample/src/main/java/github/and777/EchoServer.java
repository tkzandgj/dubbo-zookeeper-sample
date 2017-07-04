package github.and777;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
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
    EventLoopGroup group = new NioEventLoopGroup();
    try {
      ServerBootstrap b = new ServerBootstrap();
      b.group(group).channel(NioServerSocketChannel.class).localAddress(port).childHandler(
          new ChannelInitializer<Channel>() {

            @Override
            protected void initChannel(Channel channel) throws Exception {
              channel.pipeline().addLast(new ChannelHandler() {
                @Override
                public void handlerAdded(ChannelHandlerContext channelHandlerContext)
                    throws Exception {
                  System.out.println("handlerAdded");
                }

                @Override
                public void handlerRemoved(ChannelHandlerContext channelHandlerContext)
                    throws Exception {
                  System.out.println("handlerRemoved");
                }

                @Override
                public void exceptionCaught(ChannelHandlerContext channelHandlerContext,
                    Throwable throwable)
                    throws Exception {
                  System.out.println("exceptionCaught");
                }
              });
            }
          });
      ChannelFuture f = b.bind().sync();
      System.out.println(
          EchoServer.class.getName() + "started and listen on " + f.channel().localAddress());
      f.channel().closeFuture().sync();
    } finally {
      group.shutdownGracefully().sync();
    }
  }

  public static void main(String[] args) throws Exception {
    new EchoServer(65535).start();
  }
}
