package nio;

import java.nio.ByteBuffer;

public class Util {
    public static void print(ByteBuffer buffer) {
        System.out.println(buffer);
        byte[] bytes = new byte[buffer.limit()];
        buffer.get(bytes);
        System.out.println(new String(bytes));
    }
}
