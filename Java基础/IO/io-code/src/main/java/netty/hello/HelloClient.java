package netty.hello;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

/**
 * 客户端使用之前写的BIO、NIO客户端去连Netty服务端也是可以的，这里为了学习netty用
 */
public class HelloClient {
    public static void main(String[] args) throws InterruptedException {
        //1.启动类
        new Bootstrap()
                //2.添加 EventLoop
                .group(new NioEventLoopGroup())
                //3.选择客户端 channel 实现
                .channel(NioSocketChannel.class)
                //4.添加处理器
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override //连接建立后被调用
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        //字符串编码,和服务端对应
                        channel.pipeline().addLast(new StringEncoder());
                    }
                })
                //5.连接到服务器
                .connect(new InetSocketAddress("localhost", 8080))
                .sync()  // 阻塞方法，直到连接建立
                .channel()
                //6.向服务器发送数据
                .writeAndFlush("hello world")
        ;
    }
}
