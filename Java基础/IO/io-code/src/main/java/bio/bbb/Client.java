package bio.bbb;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        //1.创建Socket对象请求服务端的连接
        Socket socket = new Socket("127.0.0.1", 9999);
        //2.从Socket对象中获取一个字节输出流
        OutputStream os = socket.getOutputStream();
        //包装成打印流
        PrintStream ps = new PrintStream(os);
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("请输入:");
            String msg = sc.nextLine();
            ps.println(msg);
            ps.flush();
        }
    }
}
