package bio.aaa;

import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try {
            //1.创建Socket对象请求服务端的连接
            Socket socket = new Socket("127.0.0.1", 9999);
            //2.从Socket对象中获取一个字节输出流
            OutputStream os = socket.getOutputStream();
            //包装成打印流
            PrintStream ps = new PrintStream(os);
            ps.println("hello world");
            ps.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
