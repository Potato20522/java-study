package nio.buffer;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

public class PackTest {
    @Test
    void test() {
        ByteBuffer source = ByteBuffer.allocate(32);
        source.put("Hello, world\nI'm zhangsan\nHo".getBytes());
        split(source);
        source.put("w are you?\n".getBytes());
        split(source);
    }
    private void split(ByteBuffer source) {
        source.flip();
        for (int i = 0; i < source.limit(); i++) {
            if (source.get(i)=='\n') {//get(i)不会导致指针移动
                //把这条消息存入新的bytebuffer
                int length = i + 1 - source.position();
                ByteBuffer target = ByteBuffer.allocate(length);
                //从source向target写
                for (int j = 0; j < length; j++) {
                    target.put(source.get());
                }
            }
        }
        //将未读完的数据，移到数据前面，便于下次读
        source.compact();
    }
}
