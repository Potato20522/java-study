package bio.chat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * BIO下的端口转发思想
 * 服务端实现的需求
 * 1、注册端口
 * 2、接收客户端的 socket连接,交给一个独立的线程来处理
 * 3、把当前连接的客户端 socket存入到一个所谓的在线 socket集合中保存
 * 4、接收客户端的消息,然后推送给当前所有在线的socket接收
 */
public class Server {
    static List<Socket> onLine = new ArrayList<>();
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(9999);
        ExecutorService pool = Executors.newCachedThreadPool();
        while (true) {
            Socket socket = ss.accept();
            onLine.add(socket);
            pool.execute(()->{
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String msg;
                    while ((msg = br.readLine()) != null) {
                        //推送给所有在线的socket
                        sendToAll(msg);
                    }
                } catch (Exception e) {
                    System.out.println("当前有客户端下线了");
                    onLine.remove(socket);
                }
            });
        }
    }

    private static void sendToAll(String msg) throws IOException {
        for (Socket socket : onLine) {
            PrintStream ps = new PrintStream(socket.getOutputStream());
            ps.println(msg);
            ps.flush();
        }
    }
}
