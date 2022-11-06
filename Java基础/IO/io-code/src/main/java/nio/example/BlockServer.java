package nio.example;

import lombok.extern.slf4j.Slf4j;
import nio.Util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

//使用nio来理解阻塞模式，单线程
@Slf4j
public class BlockServer {
    public static void main(String[] args) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(8080));
        List<SocketChannel> channels = new ArrayList<>();//连接集合
        while (true) {
            log.debug("connecting...");
            //与客户端建立连接，SocketChannel用来与客户端通信
            SocketChannel sc = ssc.accept();//accept 默认阻塞
            log.debug("connected...{}",sc);
            channels.add(sc);
            for (SocketChannel channel : channels) {
                //接受客户端发送的数据
                log.debug("before read...{}",channel);
                channel.read(buffer);//read也是阻塞方法
                //切换到读模式
                buffer.flip();
                Util.print(buffer);
                buffer.clear();
                log.debug("after read...{}",channel);
            }
        }
    }
}
