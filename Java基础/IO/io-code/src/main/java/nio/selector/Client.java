package nio.selector;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException{
        //1.获取通道
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));
        //2.切换成非阻塞模式
        socketChannel.configureBlocking(false);
        //3.分配指定大小的缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //4.发送数据给服务器
        Scanner scanner = new Scanner(System.in);//IDEA无法在单元测试时在控制台输入数据
        while (scanner.hasNext()){
            String s = scanner.next();
            buffer.put((new Date() +"\n"+s).getBytes());
            buffer.flip();
            socketChannel.write(buffer);
            buffer.clear();
        }
        //5.关闭通道
        socketChannel.close();
    }
}
