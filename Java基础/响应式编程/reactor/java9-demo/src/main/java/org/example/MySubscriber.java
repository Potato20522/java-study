package org.example;

import java.util.concurrent.Flow;

/**
 * 订阅者
 */
public class MySubscriber implements Flow.Subscriber<String>{
    private Flow.Subscription subscription;  //订阅令牌
    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        System.out.println("订阅关系建立onSubscribe: " + subscription);
        this.subscription = subscription;
        subscription.request(2);
    }

    @Override
    public void onNext(String item) {
        System.out.println("item: " + item);
        // 一个消息处理完成之后，可以继续调用subscription.request(n);向发布者要求数据发送
        //subscription.request(n);
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("onError: " + throwable);
    }

    @Override
    public void onComplete() {
        System.out.println("onComplete");
    }
}
