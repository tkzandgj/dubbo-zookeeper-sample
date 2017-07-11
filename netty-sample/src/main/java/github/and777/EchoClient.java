package github.and777;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import java.net.InetSocketAddress;

/**
 * @author edliao on
 * @description 应答客户端
 */
public class EchoClient {

  private final String host;
  private final Integer port;

  public EchoClient(String host, Integer port) {
    this.host = host;
    this.port = port;
  }

  public void start() throws Exception {
    EventLoopGroup group = new NioEventLoopGroup();
    try {
      Bootstrap b = new Bootstrap();
      b.group(group)
          .channel(NioSocketChannel.class)
          //监听远程地址
          .remoteAddress(new InetSocketAddress(host, port))
          //处理
          .handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
              ch.pipeline().addLast(new EchoClientHandler());
            }
          });
      //连接
      ChannelFuture f = b.connect().sync();

      //关闭
      f.channel().closeFuture().sync();
    } finally {
      group.shutdownGracefully().sync();
    }
  }

  /**
   * SimpleChannelInboundHandler 在 ChannelInboundHandlerAdapter的基础上扩展了 ,可以自动装成ByteBuf
   */
  class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
      ctx.write(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
      ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
      System.out
          .println("Client received: " + ByteBufUtil.hexDump(msg.readBytes(msg.readableBytes())));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
        throws Exception {
      cause.printStackTrace();
      ctx.close();
    }
  }


  public static void main(String[] args) throws Exception {
    new EchoClient("localhost", 65535).start();
  }
}

