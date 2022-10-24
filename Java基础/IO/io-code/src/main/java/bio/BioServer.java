package bio;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BioServer {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(6666);
        while (true) {
            Socket s = ss.accept();
            System.out.println("A client connected!");
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            String str;
            if ((str = dis.readUTF()) != null) {
                System.out.println(str);
                System.out.println("from "+s.getInetAddress()+",port #"+s.getPort());
            }
            dis.close();
            dos.close();
        }
    }
}
