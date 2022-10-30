package nio.buffer;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Buffer clear() 	//清空缓冲区并返回对缓冲区的引用
 * Buffer flip() //将缓冲区的界限设置为当前位置，并将当前位置充值为 0。即将写入模式翻转成读取模式
 * int capacity() //返回 Buffer 的 capacity 大小
 * boolean hasRemaining() //判断缓冲区中是否还有元素
 * int limit() //返回 Buffer 的界限(limit) 的位置
 * Buffer limit(int n) //将设置缓冲区界限为 n, 并返回一个具有新 limit 的缓冲区对象
 * Buffer mark() //对缓冲区设置标记，将当前position的值保存起来，放在mark属性中，让mark属性记住这个临时位置；之后，可以调用Buffer.reset()方法将mark的值恢复到position中
 * int position() //返回缓冲区的当前位置 position
 * Buffer position(int n) //将设置缓冲区的当前位置为 n , 并返回修改后的 Buffer 对象
 * int remaining() //返回 position 和 limit 之间的元素个数
 * Buffer reset() //将位置 position 转到以前设置的 mark 所在的位置
 * Buffer rewind() //将位置设为为 0， 取消设置的 mark。已经读完的数据，如果需要再读一遍，可以调用rewind()方法。rewind()也叫倒带，就像播放磁带一样倒回去，再重新播放。
 */
public class BufferTest {
    @Test
    void test01() {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        System.out.println("position:"+buffer.position());//0
        System.out.println("limit:"+buffer.limit());//10
        System.out.println("capacity:"+buffer.capacity());//10
        System.out.println("--------");

        String name = "potato!";
        buffer.put(name.getBytes());
        System.out.println("position:"+buffer.position());//7
        System.out.println("limit:"+buffer.limit());//10
        System.out.println("capacity:"+buffer.capacity());//10
        System.out.println("--------");

        buffer.flip();//切换到可读模式
        System.out.println("position:"+buffer.position());//0
        System.out.println("limit:"+buffer.limit());//7,前面的七个位置是可以读取的
        System.out.println("capacity:"+buffer.capacity());//10
        System.out.println("--------");

        char ch = (char) buffer.get();
        System.out.println(ch);
        System.out.println("position:"+buffer.position());//1
        System.out.println("limit:"+buffer.limit());//7
        System.out.println("capacity:"+buffer.capacity());//10
    }

    @Test
    void test02() {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        System.out.println("position:"+buffer.position());//0
        System.out.println("limit:"+buffer.limit());//10
        System.out.println("capacity:"+buffer.capacity());//10
        System.out.println("--------");

        String name = "potato!";
        buffer.put(name.getBytes());
        System.out.println("position:"+buffer.position());//7
        System.out.println("limit:"+buffer.limit());//10
        System.out.println("capacity:"+buffer.capacity());//10
        System.out.println("--------");

        buffer.clear();//只是将位置重置，并没有清除里面的数据
        System.out.println("position:"+buffer.position());//0
        System.out.println("limit:"+buffer.limit());//10
        System.out.println("capacity:"+buffer.capacity());//10
        System.out.println((char) buffer.get());//p
        System.out.println("--------");

        //标记
        ByteBuffer buf = ByteBuffer.allocate(10);
        buf.put("potato!".getBytes());
        buf.flip();
        byte[] b = new byte[2];
        buf.get(b);
        String s = new String(b);
        System.out.println(s);
        System.out.println("position:"+buf.position());//2
        System.out.println("limit:"+buf.limit());//7
        System.out.println("capacity:"+buf.capacity());//10
        System.out.println("--------");
        buf.mark();//标记这个位置
        byte[] b2 = new byte[3];
        buf.get(b2);
        System.out.println(new String(b2));
        System.out.println("position:"+buf.position());//5
        System.out.println("limit:"+buf.limit());//7
        System.out.println("capacity:"+buf.capacity());//10
        System.out.println("--------");
        buf.reset();
        if (buf.hasRemaining()) {
            System.out.println(buf.remaining());//5
        }
    }

    //直接内存、非直接内存
    @Test
    void test03() {
        //直接内存缓冲区
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

    }
}
