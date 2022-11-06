package nio.example;

import lombok.extern.slf4j.Slf4j;
import nio.Util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class SelectorServer {

    private static void split(ByteBuffer source) {
        source.flip();
        for (int i = 0; i < source.limit(); i++) {
            if (source.get(i)=='\n') {//get(i)不会导致指针移动
                //把这条消息存入新的bytebuffer
                int length = i + 1 - source.position();
                ByteBuffer target = ByteBuffer.allocate(length);
                //从source向target写
                for (int j = 0; j < length; j++) {
                    target.put(source.get());
                }
            }
        }
        //将未读完的数据，移到数据前面，便于下次读
        source.compact();
    }

    public static void main(String[] args) throws IOException {
        //1.创建 Selector，管理多个 channel
        Selector selector = Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);

        //2.建立 Selector 和 channel 之间的联系（注册）
        //SelectionKey 就是将来事件发生之后，通过它可以知道事件和哪个channel的事件
        //事件有：accept(会有连接请求时触发)、connect(是客户端，建立连接后触发)、read(可读事件)、write(可写事件)
        SelectionKey sscKey = ssc.register(selector, 0, null);//0表示不关注任何事件
        log.debug("register key:{}",sscKey);
        //对哪个事件感兴趣呢:只关注ACCEPT
        sscKey.interestOps(SelectionKey.OP_ACCEPT);
        ssc.bind(new InetSocketAddress(8080));

        while (true) {
            //3.select方法，没有事件发生，线程阻塞，有事件，线程才会运行
            selector.select();
            //4.处理事件,selectedKeys 内部包含了所有发生的事件
            //想要在遍历的时候删除元素，就要用迭代器遍历，不要用增强for
            //select在事件未处理时，它不会阻塞;事件发生后要么处理，要么取消，不能置之不理，否则死循环
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();//accept,read
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();
                log.debug("key:{}",key);//这个key就是上面注册的那个sscKey
                //5.区分事件类型
                if (key.isAcceptable()) {
                    ServerSocketChannel channel = (ServerSocketChannel)key.channel();
                    SocketChannel sc = channel.accept();//建立连接，处理了，
                    sc.configureBlocking(false);
                    //SocketChannel注册到selector上
                    ByteBuffer buffer = ByteBuffer.allocate(16);
                    //将 ByteBuffer 作为附件关联到 selectionKey 上
                    SelectionKey socketKey = sc.register(selector, 0, buffer);
                    //关注读数据事件
                    socketKey.interestOps(SelectionKey.OP_READ);
                    log.debug("{}",sc);
                }else if (key.isReadable()) {
                    try {
                        SocketChannel channel = (SocketChannel) key.channel();//拿到触发事件的channel
                        //获取 selectionKey 上关联的附件
                        ByteBuffer buffer = (ByteBuffer)key.attachment();
                        //如果是正常断开，read的返回值是 -1
                        int read = channel.read(buffer);
                        if (read == -1) {
                            key.cancel();
                        } else {
                            split(buffer);
                            //说明buffer满了，需要扩容
                            if (buffer.position() == buffer.limit()) {
                                ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() * 2);
                                buffer.flip();
                                newBuffer.put(buffer);
                                key.attach(newBuffer);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        key.cancel();
                    }
                }
            }
        }
    }
}
