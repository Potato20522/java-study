# Java9响应式编程Reactive Stream
## 设计思想
响应式编程范式通常在面向对象语言中作为观察者模式的扩展出现。可以将其与大家熟知的迭代器模式作对比，主要区别在于：


迭代器（Iterator）	响应式流（Reactive Stream）
设计模式	迭代器模式	观察者模式
数据方向	拉模式（PULL）	推模式（PUSH）
获取数据	T next()	onNext(T)
处理完成	hasNext()	onCompleted()
异常处理	throws Exception	onError(Exception)

Java 8 引入了 Stream 用于流的操作，Java 9 引入的 Flow 也是数据流的操作。相比之下：

Stream 更侧重于流的过滤、映射、整合、收集，使用的是 PULL 模式。
而 Flow/RxJava/Reactor 更侧重于流的产生与消费，使用的是 PUSH 模式 。

Java 9的 Reactive Streams是对异步流式编程的一种实现。它基于异步发布和订阅模型，具有非阻塞“背压”数据处理的特点。

Non-blocking Back Pressure(非阻塞背压)：它是一种机制，让发布订阅模型中的订阅者避免接收大量数据(超出其处理能力)，订阅者可以异步通知发布者降低或提升数据生产发布的速率。它是响应式编程实现效果的核心特点！

