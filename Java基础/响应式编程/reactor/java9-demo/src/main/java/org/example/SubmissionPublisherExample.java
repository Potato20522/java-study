package org.example;

import java.util.concurrent.Executors;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

public class SubmissionPublisherExample {
    public static void main(String[] args) {
        var executor = Executors.newFixedThreadPool(1);
        //消息发布者
        var publisher = new SubmissionPublisher<String>(executor, Flow.defaultBufferSize());
        publisher.subscribe(new MySubscriber());   //建立订阅关系，可以有多个订阅者
        publisher.submit("数据 1");  //发送消息1
        publisher.submit("数据 2"); //发送消息2
        publisher.submit("数据 3"); //发送消息3
    }
}
