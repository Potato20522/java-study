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


@Slf4j
public class NonBlockServer {
    public static void main(String[] args) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);//设置非阻塞，默认是阻塞
        ssc.bind(new InetSocketAddress(8080));
        List<SocketChannel> channels = new ArrayList<>();//连接集合
        while (true) {
            //与客户端建立连接，SocketChannel用来与客户端通信
            //accept 默认阻塞。非阻塞时，线程还会继续运行，如果没有连接建立，但返回值是null
            SocketChannel sc = ssc.accept();
            if (sc != null) {
                log.debug("connected...{}",sc);
                sc.configureBlocking(false);//设置非阻塞，默认是阻塞
                channels.add(sc);
                for (SocketChannel channel : channels) {
                    //接受客户端发送的数据
                    // read也是默认阻塞方法。
                    // 非阻塞时，线程继续运行，如果没有读到数据，read返回0
                    int read = channel.read(buffer);
                    if (read > 0) {
                        //切换到读模式
                        buffer.flip();
                        Util.print(buffer);
                        buffer.clear();
                        log.debug("after read...{}",channel);
                    }
                }
            }
        }
    }
}
