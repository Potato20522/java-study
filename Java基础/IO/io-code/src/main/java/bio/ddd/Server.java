package bio.ddd;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(9999);
        HandlerSocketServerPool pool = new HandlerSocketServerPool(6, 10);
        while (true) {
            Socket socket = ss.accept();
            Runnable target = new ServerRunnableTarget(socket);
            pool.execute(target);
        }
    }
}
