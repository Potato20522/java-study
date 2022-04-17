package com.potato;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

/**
 * https://blog.csdn.net/xushiyu1996818/article/details/113142831
 * mark()：记录当前position的位置，使mark=position
 * <p>
 * reset()：恢复到上次备忘的位置，即position=mark
 * <p>
 * clear()：将缓存区置为待填充状态，即position=0;limit=capacity;mark=-1
 * <p>
 * flip()：将缓冲区的内容切换为待读取状态，即limit=position;position=0;mark=-1
 * <p>
 * rewind()：恢复缓冲区为待读取状态，即position=0;mark=-1
 * <p>
 * remaining()：缓冲区剩余元素，即limit-position
 * <p>
 * compact()：丢弃已经读取的数据，保留未读取的数据，并使缓存中处于待填充状态
 * <p>
 * isDirect()：是否是直接操作内存的Buffer；若是，则此Buffer直接操作JVM堆外内存使用Unsafe实现；否则操作JVM堆内存
 * <p>
 * slice()：从当前buffer中生成一个该buffer尚未使用部分的新的缓冲区，例如当前buffer的position为3，limit为5，则新的缓冲区limit和capacity都为2，offset的3，数据区域两者共享
 * <p>
 * Unsafe类中的方法 https://blog.csdn.net/zyzzxycj/article/details/89877863
 */
public class IOTest {
    @Test
    void test01() throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(16);
        float[] floats = {1.1f, 2.2f, -3.333f, 4.5667f};
        for (int i = 0; i < floats.length; i++) {
            float v = floats[i];
            byteBuffer.putFloat(i * 4, v);
        }
        //直接内存中的数据===>直接写出到文件
        FileChannel fileChannel = new FileOutputStream("floats").getChannel();
        fileChannel.write(byteBuffer);
        fileChannel.close();
    }

    @Test
    void test02() {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(16);
        float[] floats = {1.1f, 2.2f, -3.333f, 4.5667f};
        for (int i = 0; i < floats.length; i++) {
            float v = floats[i];
            byteBuffer.putFloat(i * 4, v);
        }
        byteBuffer.rewind();//切换成读取数据模式
        byte[] out = new byte[16];
        //直接内存中的数据===>拷贝的JVM堆内存中
        byteBuffer.get(out, 0, 16);
    }

    @Test
    void test03() throws IOException {
        FloatBuffer floatBuffer = ByteBuffer.allocateDirect(16).asFloatBuffer();
        float[] floats = {1.1f, 2.2f, -3.333f, 4.5667f};
        floatBuffer.put(floats);
//        FileChannel fileChannel = new FileOutputStream("floats").getChannel();
        System.out.println(Arrays.toString(floatBuffer.array()));
//        fileChannel.write(floatBuffer);
//        fileChannel.close();
    }
}
