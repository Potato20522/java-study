package netty.eventloop;

import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.NettyRuntime;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class TestEventLoop {
    public static void main(String[] args) {
        //1.创建事件循环组
        EventLoopGroup group = new NioEventLoopGroup(2);//IO事件、普通任务、定时任务
        //DefaultEventLoopGroup //普通任务、定时任务
        //2.获取下一个事件循环对象
        System.out.println(group.next());

        //3.普通任务执行，类似线程池。submit 或 execute
        group.next().submit(() -> log.info("ok"));
        log.info("main");

        //4.定时任务
        group.next().scheduleAtFixedRate(() -> log.info("aaa"), 0, 1, TimeUnit.SECONDS);
    }
}
