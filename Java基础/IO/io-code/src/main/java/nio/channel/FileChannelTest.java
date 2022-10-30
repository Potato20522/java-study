package nio.channel;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static java.nio.file.StandardOpenOption.*;

public class FileChannelTest {
    @Test
    void testWrite() throws IOException {
        //字节输出流通向文件
        FileOutputStream fos = new FileOutputStream("src/main/java/nio/channel/data01.txt");
        //都得到输出流对应的Channel
        FileChannel channel = fos.getChannel();
        //分配缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put("hello,Potato程序员!".getBytes());
        //缓冲区切换成写出模式
        buffer.flip();
        channel.write(buffer);
        fos.close();
    }

    @Test
    void testRead() throws IOException {
        FileInputStream fis = new FileInputStream("src/main/java/nio/channel/data01.txt");
        FileChannel channel = fis.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //数据读取到缓冲区
        channel.read(buffer);
        buffer.flip();
        System.out.println(new String(buffer.array(), 0, buffer.remaining()));
        fis.close();
    }

    @Test
    void testCopy() throws Exception {
        long start = System.currentTimeMillis();
        String srcFile = "D:\\迅雷下载\\龙之家族.House.of.the.Dragon.S01E07.1080p.H265-NEW字幕组.mp4";
        String destFile = "D:\\迅雷下载\\龙之家族.House.of.the.Dragon.S01E07.1080p.H265-NEW字幕组-copy.mp4";
        FileInputStream fis = new FileInputStream(srcFile);
        FileOutputStream fos = new FileOutputStream(destFile);
        FileChannel is = fis.getChannel();
        FileChannel os = fos.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1204);
        while (is.read(buffer) > -1) {
            //切换到读取模式
            buffer.flip();
            os.write(buffer);
            //先清空缓冲区，再写入
            buffer.clear();
        }
        fis.close();
        fos.close();
        long end = System.currentTimeMillis();
        System.out.println("复制完成，耗时:" + (end - start) + " ms");//16163
    }

    //使用直接缓冲区完成文件的复制（内存映射文件）
    @Test
    void testCopy2() throws Exception {
        long start = System.currentTimeMillis();
        String srcFile = "D:\\迅雷下载\\龙之家族.House.of.the.Dragon.S01E07.1080p.H265-NEW字幕组.mp4";
        String destFile = "D:\\迅雷下载\\龙之家族.House.of.the.Dragon.S01E07.1080p.H265-NEW字幕组-copy.mp4";
        FileChannel inChannel = FileChannel.open(Paths.get(srcFile), READ);
        FileChannel outChannel = FileChannel.open(Paths.get(destFile), WRITE, READ, CREATE);
        //内存映射文件
        MappedByteBuffer inMapBuffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
        MappedByteBuffer outMapBuffer = outChannel.map(FileChannel.MapMode.READ_WRITE, 0, inChannel.size());
        //直接对缓冲区进行数据的读写操作
        byte[] dst = new byte[inMapBuffer.limit()];
        inMapBuffer.get(dst);
        outMapBuffer.put(dst);

        inChannel.close();
        outChannel.close();
        long end = System.currentTimeMillis();
        System.out.println("复制完成，耗时:：" + (end - start) + " ms");//1027 ms
    }

    @Test
    void testCopy3() throws Exception {
        long start = System.currentTimeMillis();
        String srcFile = "D:\\迅雷下载\\龙之家族.House.of.the.Dragon.S01E07.1080p.H265-NEW字幕组.mp4";
        String destFile = "D:\\迅雷下载\\龙之家族.House.of.the.Dragon.S01E07.1080p.H265-NEW字幕组-copy.mp4";
        FileInputStream fis = new FileInputStream(srcFile);
        FileOutputStream fos = new FileOutputStream(destFile);
        FileChannel is = fis.getChannel();
        FileChannel os = fos.getChannel();
        for (long count = is.size(); count > 0; ) {
            long transferred = is.transferTo(is.position(), count, os);
            is.position(is.position() + transferred);
            count -= transferred;
        }
        fis.close();
        fos.close();
        long end = System.currentTimeMillis();
        System.out.println("复制完成，耗时:" + (end - start) + " ms");//6180 ms
    }

    @Test
    void testCopy4() throws Exception {
        long start = System.currentTimeMillis();
        String srcFile = "D:\\迅雷下载\\龙之家族.House.of.the.Dragon.S01E07.1080p.H265-NEW字幕组.mp4";
        String destFile = "D:\\迅雷下载\\龙之家族.House.of.the.Dragon.S01E07.1080p.H265-NEW字幕组-copy.mp4";
        Files.copy(Paths.get(srcFile), Paths.get(destFile));
        long end = System.currentTimeMillis();
        System.out.println("复制完成，耗时:" + (end - start) + " ms");//14650 ms
    }

    //多个缓存，数据分散
    @Test
    void test() throws IOException {
        FileInputStream fis = new FileInputStream("src/main/java/nio/channel/data01.txt");
        FileOutputStream fos = new FileOutputStream("src/main/java/nio/channel/data02.txt");
        FileChannel isChannel = fis.getChannel();
        FileChannel osChannel = fos.getChannel();
        ByteBuffer buffer1 = ByteBuffer.allocate(4);
        ByteBuffer buffer2 = ByteBuffer.allocate(1024);
        ByteBuffer[] buffers = {buffer1, buffer2};

        isChannel.read(buffers);
        for (ByteBuffer buffer : buffers) {
            buffer.flip();//切换到读
            System.out.println(new String(buffer.array(), 0, buffer.remaining()));
        }

        //聚集写入到通道
        osChannel.write(buffers);
        fis.close();
        fos.close();
    }

    @Test
    void testTransfer() throws IOException {
        FileInputStream fis = new FileInputStream("src/main/java/nio/channel/data01.txt");
        FileOutputStream fos = new FileOutputStream("src/main/java/nio/channel/data03.txt");
        FileChannel isChannel = fis.getChannel();
        FileChannel osChannel = fos.getChannel();
        //内部使用 MappedByteBuffer 进行传输
        //osChannel.transferFrom(isChannel, isChannel.position(), isChannel.size());
        isChannel.transferTo(isChannel.position(), isChannel.size(), osChannel);
        fis.close();
        fos.close();
    }

    @Test
    void testTransfer2() throws IOException {
        try (FileChannel from = new FileInputStream("data.txt").getChannel();
             FileChannel to = new FileOutputStream("to.txt").getChannel();) {
            long size = from.size();
            //left变量代表还剩余多少字节
            for (long left = size; left > 0;) {
                left -= from.transferTo((size-left), left, to);
            }
        }
    }
}
