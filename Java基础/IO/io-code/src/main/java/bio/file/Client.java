package bio.file;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

//实现上传任意格式的文件
public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1",8888);
        //包装成一个数据输出流
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        //先发送文件后缀名
        dos.writeUTF(".png");
        InputStream is = new FileInputStream("java.png");
        byte[] buf = new byte[1024];
        int len;
        while ((len = is.read(buf)) > 0) {
            dos.write(buf, 0, len);
        }
        dos.flush();
        socket.shutdownOutput();//通知服务端，这边的数据发送完毕了
        is.close();
    }
}
