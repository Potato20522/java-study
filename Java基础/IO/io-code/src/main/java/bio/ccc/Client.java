package bio.ccc;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 9999);
        PrintStream ps = new PrintStream(socket.getOutputStream());
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("请输入:");
            String msg = sc.nextLine();
            ps.println(msg);
            ps.flush();
        }
    }
}
