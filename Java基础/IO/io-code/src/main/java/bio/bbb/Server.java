package bio.bbb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        //1.定义一个ServerSocket对象进行服务端的端口注册
        ServerSocket ss = new ServerSocket(9999);
        //2.监听客户端的Socket连接请求
        Socket socket = ss.accept();
        //3.从socket管道中得到输入流
        InputStream is = socket.getInputStream();
        //事先约定好发的是字符串，所以采用缓存字符输入流
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String msg;
        //逐行读取文本字符串，这里是阻塞的，如果不是一行字符串，就一组阻塞
        while ((msg = br.readLine()) != null) {
            System.out.println("服务端收到：" + msg);
        }
    }
}
