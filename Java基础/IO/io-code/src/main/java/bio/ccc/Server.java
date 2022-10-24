package bio.ccc;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//可以接收多个客户端的请求
public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(9999);
        ExecutorService pool = Executors.newCachedThreadPool();
        while (true) {
            Socket socket = ss.accept();
            new ServerThreadReader(socket).start();
        }
    }
}
