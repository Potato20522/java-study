package bio.file;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(8888);
        ExecutorService pool = Executors.newCachedThreadPool();
        while (true) {
            Socket socket = ss.accept();
            pool.execute(() -> {
                try {
                    DataInputStream dis = new DataInputStream(socket.getInputStream());
                    String suffix = dis.readUTF();
                    OutputStream os = new FileOutputStream("./"+ UUID.randomUUID()+suffix);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = dis.read(buffer)) > 0) {
                        os.write(buffer,0,len);
                    }
                    os.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
