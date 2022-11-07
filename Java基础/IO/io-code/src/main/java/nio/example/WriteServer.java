package nio.example;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;

public class WriteServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);

        Selector selector = Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT);

        ssc.bind(new InetSocketAddress(8080));

        while (true) {
            selector.select();
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();
                if (key.isAcceptable()) {
                    SocketChannel sc = ssc.accept();//因为这里只有一个 ServerSocketChannel，可以这么写
                    sc.configureBlocking(false);
                    SelectionKey sckey = sc.register(selector, 0, null);
                    //1.向客户端发送大量数据
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < 30000000; i++) {
                        sb.append('a');
                    }
                    ByteBuffer buffer = Charset.defaultCharset().encode(sb.toString());
                    //2.返回值代表实际写入的字节数
                    int write = sc.write(buffer);//并不能保证一次把所有内容都写到客户端
                    System.out.println(write);
                    //3.判断是否有剩余内存
                    if (buffer.hasRemaining()) {
                        //4.关注可写事件 (+ 或者 | 运算，保留原来的 interestOps)
                        sckey.interestOps(sckey.interestOps() + SelectionKey.OP_WRITE);
                        //5.把未写完的数据挂到sckey上
                        sckey.attach(buffer);
                    }
                } else if (key.isWritable()) {
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    SocketChannel sc = (SocketChannel) key.channel();
                    int write = sc.write(buffer);
                    System.out.println(write);
                    //6.写完了，buffer清理
                    if (!buffer.hasRemaining()) {
                        key.attach(null);
                        //写完了，就不需要关注可写事件了
                        key.interestOps(key.interestOps() - SelectionKey.OP_WRITE);
                    }
                }
            }
        }
    }
}
