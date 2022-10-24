# Reactor3快速入门

转载来源：

[（3）lambda与函数式——响应式Spring的道法术器_享学IT的博客-CSDN博客](https://blog.csdn.net/get_set/article/details/79480121)

[（4）Reactor 3快速上手——响应式Spring的道法术器_享学IT的博客-CSDN博客_reactor3](https://blog.csdn.net/get_set/article/details/79480172)

Reactor与Spring是兄弟项目，侧重于Server端的响应式编程，主要 artifact 是 reactor-core，这是一个基于 Java 8 的实现了响应式流规范 （Reactive Streams specification）的响应式库。

本文对Reactor的介绍以基本的概念和简单的使用为主，深度以能够满足基本的Spring WebFlux使用为准。

maven依赖：

```xml
<dependency>
    <groupId>io.projectreactor</groupId>
    <artifactId>reactor-core</artifactId>
    <version>3.4.22</version>
</dependency>
<dependency>
    <groupId>io.projectreactor</groupId>
    <artifactId>reactor-test</artifactId>
    <version>3.4.22</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.13.2</version>
    <scope>test</scope>
</dependency>

```

## Flux与Mono

Reactor中的发布者（Publisher）由`Flux`和`Mono`两个类定义，它们都提供了丰富的操作符（operator）。一个Flux对象代表一个包含0…N个元素的响应式序列，而一个Mono对象代表一个包含零/一个（0…1）元素的结果。

既然是“数据流”的发布者，Flux和Mono都可以发出**三种“数据信号”**：**元素值、错误信号、完成信号**，错误信号和完成信号都是终止信号，完成信号用于告知下游订阅者该数据流正常结束，错误信号终止数据流的同时将错误传递给下游订阅者。

下图所示就是一个Flux类型的数据流，黑色箭头是时间轴。它连续发出“1” - “6”共6个元素值，以及一个完成信号（图中⑥后边的加粗竖线来表示），完成信号告知订阅者数据流已经结束。

![img](img/Reactor3快速入门.assets/2021031318064743.png)
下图所示是一个Mono类型的数据流，它发出一个元素值后，又发出一个完成信号。

![img](img/Reactor3快速入门.assets/20210313180709388.png)



> 既然Flux具有发布一个数据元素的能力，为什么还要专门定义一个Mono类呢？举个例子，一个HTTP请求产生一个响应，所以对其进行“count”操作是没有多大意义的。表示这样一个结果的话，应该用`Mono<HttpResponse>`而不是 `Flux<HttpResponse>`，对于的操作通常只用于处理 0/1 个元素。它们从语义上就原生包含着元素个数的信息，从而避免了对Mono对象进行多元素场景下的处理。

> 有些操作可以改变基数，从而需要切换类型。比如，count操作用于Flux，但是操作返回的结果是`Mono<Long>`。

我们可以用如下代码声明上边两幅图所示的Flux和Mono：

```java
Flux.just(1, 2, 3, 4, 5, 6);
Mono.just(1);
```

Flux和Mono提供了多种创建数据流的方法，`just`就是一种比较直接的声明数据流的方式，其参数就是数据元素。

对于图中的Flux，还可以通过如下方式声明（分别基于数组、集合和Stream生成）：

```java
//Flux的声明方式
Integer[] array = new Integer[]{1,2,3,4,5,6};
List<Integer> list = Arrays.asList(array);
Stream<Integer> stream = list.stream();

Flux.fromArray(array);
Flux.fromIterable(list);
Flux.fromStream(stream);
```

不过，这三种信号都不是一定要具备的：

- 首先，错误信号和完成信号都是终止信号，二者不可能同时共存；
- 如果没有发出任何一个元素值，而是直接发出完成/错误信号，表示这是一个空数据流；
- 如果没有错误信号和完成信号，那么就是一个无限数据流。

比如，对于只有完成/错误信号的数据流：

```java
// 只有完成信号的空数据流
Flux.just();
Flux.empty();
Mono.empty();
Mono.justOrEmpty(Optional.empty());
// 只有错误信号的数据流
Flux.error(new Exception("some error"));
Mono.error(new Exception("some error"));
```

你可能会纳闷，空的数据流有什么用？举个例子，当我们从响应式的DB中获取结果的时候（假设DAO层是`ReactiveRepository<User>`），就有可能为空：

```java
Mono<User> findById(long id);
Flux<User> findAll();
```

无论是空还是发生异常，都需要通过完成/错误信号告知订阅者，已经查询完毕，但是抱歉没有得到值，礼貌问题嘛~

## 订阅前什么都不会发生

数据流有了，假设我们想把每个数据元素原封不动地打印出来：

```java
Flux.just(1, 2, 3, 4, 5, 6).subscribe(System.out::print);
System.out.println();
Mono.just(1).subscribe(System.out::println);
```

输出如下：

```
123456
1
```

不调用subscribe进行订阅的话，啥事也不会发生。

可见，subscribe方法中的lambda表达式作用在了每一个数据元素上。此外，Flux和Mono还提供了多个subscribe方法的变体：

```java
// 订阅并触发数据流
subscribe(); 
// 订阅并指定对正常数据元素如何处理
subscribe(Consumer<? super T> consumer); 
// 订阅并定义对正常数据元素和错误信号的处理
subscribe(Consumer<? super T> consumer,
          Consumer<? super Throwable> errorConsumer); 
// 订阅并定义对正常数据元素、错误信号和完成信号的处理
subscribe(Consumer<? super T> consumer,
          Consumer<? super Throwable> errorConsumer,
          Runnable completeConsumer); 
// 订阅并定义对正常数据元素、错误信号和完成信号的处理，以及订阅发生时的处理逻辑
subscribe(Consumer<? super T> consumer,
          Consumer<? super Throwable> errorConsumer,
          Runnable completeConsumer,
          Consumer<? super Subscription> subscriptionConsumer); 

```

1）如果是订阅上边声明的Flux：

```java
Flux.just(1, 2, 3, 4, 5, 6).subscribe(
    System.out::println,
    System.err::println,
    () -> System.out.println("Completed!"));
```

输出如下：

```
java.lang.Exception: some error
```

打印出了错误信号，没有输出`Completed!`表明没有发出完成信号。

这里需要注意的一点是，`Flux.just(1, 2, 3, 4, 5, 6)`仅仅声明了这个数据流，此时数据元素并未发出，只有`subscribe()`方法调用的时候才会触发数据流。所以，**订阅前什么都不会发生**。

## 测试与调试

从命令式和同步式编程切换到响应式和异步式编程有时候是令人生畏的。学习曲线中最陡峭的地方就是出错时如何分析和调试。

在命令式世界，调试通常都是非常直观的：直接看 stack trace 就可以找到问题出现的位置， 以及其他信息：是否问题责任全部出在你自己的代码？问题是不是发生在某些库代码？如果是， 那你的哪部分代码调用了库，是不是传参不合适导致的问题？等等。

当你切换到响应式的异步代码，事情就变得复杂的多了。不过我们先不接触过于复杂的内容，先了解一个基本的单元测试工具——`StepVerifier`。

最常见的测试 Reactor 序列的场景就是定义一个 Flux 或 Mono，然后在订阅它的时候测试它的行为。

当你的测试关注于每一个数据元素的时候，就非常贴近使用 StepVerifier 的测试场景： 下一个期望的数据或信号是什么？你是否期望使用 Flux 来发出某一个特别的值？或者是否接下来 300ms 什么都不做？——所有这些都可以使用 StepVerifier API 来表示。

还是以那个1-6的Flux以及会发出错误信号的Mono为例：

```java
private Flux<Integer> generateFluxFrom1To6() {
    return Flux.just(1, 2, 3, 4, 5, 6);
}
private Mono<Integer> generateMonoWithError() {
    return Mono.error(new Exception("some error"));
}
@Test
public void testViaStepVerifier() {
    StepVerifier.create(generateFluxFrom1To6())
            .expectNext(1, 2, 3, 4, 5, 6)
            .expectComplete()
            .verify();
    StepVerifier.create(generateMonoWithError())
            .expectErrorMessage("some error")
            .verify();
}
```

其中，`expectNext`用于测试下一个期望的数据元素，`expectErrorMessage`用于校验下一个元素是否为错误信号，`expectComplete`用于测试下一个元素是否为完成信号。

`StepVerifier`还提供了其他丰富的测试方法，我们会在后续的介绍中陆续接触到。

## 操作符（Operator）

通常情况下，我们需要对源发布者发出的原始数据流进行多个阶段的处理，并最终得到我们需要的数据。这种感觉就像是一条**流水线**，从流水线的源头进入传送带的是原料，经过流水线上各个工位的处理，逐渐由原料变成半成品、零件、组件、成品，最终成为消费者需要的包装品。这其中，流水线源头的下料机就相当于源发布者，消费者就相当于订阅者，**流水线上的一道道工序就相当于一个一个的操作符（Operator）**。

下面介绍一些我们常用的操作符。

操作符理解为 对数据的如何进行处理，类比Java8的Stream API中的那些操作。熟悉Java8的Stream API的话，下面看起来就很轻松，看起来基本都是一样的。

### map - 元素映射为新元素

`map`操作可以将数据元素进行转换/映射，得到一个新元素。

![img](img/Reactor3快速入门.assets/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2dldF9zZXQ=,size_16,color_FFFFFF,t_70.png)

```java
public final <V> Flux<V> map(Function<? super T,? extends V> mapper)
public final <R> Mono<R> map(Function<? super T, ? extends R> mapper) 
```

上图是Flux的map操作示意图，上方的箭头是原始序列的时间轴，下方的箭头是经过map处理后的数据序列时间轴。

`map`接受一个`Function`的函数式接口为参数，这个函数式的作用是定义转换操作的策略。举例说明：

```java
StepVerifier.create(Flux.range(1, 6)    // 1
            .map(i -> i * i))   // 2
            .expectNext(1, 4, 9, 16, 25, 36)    //3
            .expectComplete();  // 4

```

1. `Flux.range(1, 6)`用于生成从“1”开始的，自增为1的“6”个整型数据；
2. `map`接受lambda`i -> i * i`为参数，表示对每个数据进行平方；
3. 验证新的序列的数据；
4. `verifyComplete()`相当于`expectComplete().verify()`。

### flatMap - 元素映射为流

`flatMap`操作可以将每个数据元素转换/映射为一个流，然后将这些流合并为一个大的数据流。

![img](img/Reactor3快速入门.assets/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2dldF9zZXQ=,size_16,color_FFFFFF,t_70-16611743542573.png)

注意到，流的合并是异步的，先来先到，并非是严格按照原始序列的顺序（如图蓝色和红色方块是交叉的）。

```java
public final <R> Flux<R> flatMap(Function<? super T, ? extends Publisher<? extends R>> mapper)
public final <R> Mono<R> flatMap(Function<? super T, ? extends Mono<? extends R>> transformer) 
```

`flatMap`也是接收一个`Function`的函数式接口为参数，这个函数式的输入为一个T类型数据值，**对于Flux来说输出可以是Flux和Mono，对于Mono来说输出只能是Mono。**举例说明：

```java
StepVerifier.create(
    Flux.just("flux", "mono")
            .flatMap(s -> Flux.fromArray(s.split("\\s*"))   // 1
                    .delayElements(Duration.ofMillis(100))) // 2
            .doOnNext(System.out::print)) // 3
    .expectNextCount(8) // 4
    .verifyComplete();
```

1. 对于每一个字符串`s`，将其拆分为包含一个字符的字符串流；
2. 对每个元素延迟100ms；
3. 对每个元素进行打印（注`doOnNext`方法是“偷窥式”的方法，不会消费数据流）；
4. 验证是否发出了8个元素。

打印结果为`mfolnuox`，原因在于各个拆分后的小字符串都是间隔100ms发出的，因此会交叉。

`flatMap`通常用于每个元素又会引入数据流的情况，比如我们有一串url数据流，需要请求每个url并收集response数据。假设响应式的请求方法如下：

```java
Mono<HttpResponse> requestUrl(String url) {...}
```

而url数据流为一个`Flux<String> urlFlux`，那么为了得到所有的HttpResponse，就需要用到flatMap：

```java
urlFlux.flatMap(url -> requestUrl(url));
```

### filter - 过滤

`filter`操作可以对数据元素进行筛选。

![img](img/Reactor3快速入门.assets/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2dldF9zZXQ=,size_16,color_FFFFFF,t_70-16611753239836.png)

```java
public final Flux<T> filter(Predicate<? super T> tester)
public final Mono<T> filter(Predicate<? super T> tester) 
```

`filter`接受一个`Predicate`的函数式接口为参数，这个函数式的作用是进行判断并返回boolean。举例说明：

```java
StepVerifier.create(Flux.range(1, 6)
            .filter(i -> i % 2 == 1)    // 1
            .map(i -> i * i))
            .expectNext(1, 9, 25)   // 2
            .verifyComplete();
```

1. `filter`的lambda参数表示过滤操作将保留奇数；
2. 验证仅得到奇数的平方。

### zip - 一对一合并

看到`zip`这个词可能会联想到拉链，它能够将多个流一对一的合并起来。zip有多个方法变体，我们介绍一个最常见的二合一的。

![img](img/Reactor3快速入门.assets/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2dldF9zZXQ=,size_16,color_FFFFFF,t_70-16611754022799.png)

它对两个Flux/Mono流每次各取一个元素，合并为一个二元组（`Tuple2`）：

```java
public static <T1,T2> Flux<Tuple2<T1,T2>> zip(Publisher<? extends T1> source1,
                                          Publisher<? extends T2> source2)
public static <T1, T2> Mono<Tuple2<T1, T2>> zip(Mono<? extends T1> p1, Mono<? extends T2> p2) 
```

举个例子，假设我们有一个关于`zip`方法的说明：“Zip two sources together, that is to say wait for all the sources to emit one element and combine these elements once into a Tuple2.”，我们希望将这句话拆分为一个一个的单词并以每200ms一个的速度发出，除了前面flatMap的例子中用到的`delayElements`，可以如下操作：

```java
private Flux<String> getZipDescFlux() {
    String desc = "Zip two sources together, that is to say wait for all the sources to emit one element and combine these elements once into a Tuple2.";
    return Flux.fromArray(desc.split("\\s+"));  // 1
}

@Test
public void testSimpleOperators() throws InterruptedException {
    CountDownLatch countDownLatch = new CountDownLatch(1);  // 2
    Flux.zip(
            getZipDescFlux(),
            Flux.interval(Duration.ofMillis(200)))  // 3
            .subscribe(t -> System.out.println(t.getT1()), null, countDownLatch::countDown);    // 4
    countDownLatch.await(10, TimeUnit.SECONDS);     // 5
}

```

1. 将英文说明用空格拆分为字符串流；
2. 定义一个`CountDownLatch`，初始为1，则会等待执行1次`countDown`方法后结束，不使用它的话，测试方法所在的线程会直接返回而不会等待数据流发出完毕；
3. 使用`Flux.interval`声明一个每200ms发出一个元素的long数据流；因为zip操作是一对一的，故而将其与字符串流zip之后，字符串流也将具有同样的速度；
4. zip之后的流中元素类型为`Tuple2`，使用`getT1`方法拿到字符串流的元素；定义完成信号的处理为`countDown`;
5. `countDownLatch.await(10, TimeUnit.SECONDS)`会等待`countDown`倒数至0，最多等待10秒钟。

除了`zip`静态方法之外，还有`zipWith`等非静态方法，效果与之类似：

```java
getZipDescFlux().zipWith(Flux.interval(Duration.ofMillis(200)))
```

在异步条件下，数据流的流速不同，使用zip能够一对一地将两个或多个数据流的元素对齐发出。

### 更多

Reactor中提供了非常丰富的操作符，除了以上几个常见的，还有：

- 用于编程方式自定义生成数据流的`create`和`generate`等及其变体方法；
- 用于“无副作用的peek”场景的`doOnNext`、`doOnError`、`doOncomplete`、`doOnSubscribe`、`doOnCancel`等及其变体方法；
- 用于数据流转换的`when`、`and/or`、`merge`、`concat`、`collect`、`count`、`repeat`等及其变体方法；
- 用于过滤/拣选的`take`、`first`、`last`、`sample`、`skip`、`limitRequest`等及其变体方法；
- 用于错误处理的`timeout`、`onErrorReturn`、`onErrorResume`、`doFinally`、`retryWhen`等及其变体方法；
- 用于分批的`window`、`buffer`、`group`等及其变体方法；
- 用于线程调度的`publishOn`和`subscribeOn`方法。

使用这些操作符，你几乎可以搭建出能够进行任何业务需求的数据处理管道/流水线。

抱歉以上这些暂时不能一一介绍，更多详情请参考[JavaDoc](http://projectreactor.io/docs/core/release/api/reactor/core/publisher/Flux.html)，在下一章我们还会回头对Reactor从更深层次进行系统的分析。

此外，也可阅读博主翻译的[Reactor参考文档](https://htmlpreview.github.io/?https://github.com/get-set/reactor-core/blob/master-zh/src/docs/index.html)

## 调度器与线程模型

在Reactor中，对于多线程并发调度的处理变得异常简单。

在以往的多线程开发场景中，我们通常使用`Executors`工具类来创建线程池，通常有如下四种类型：

- `newCachedThreadPool`创建一个弹性大小缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程；
- `newFixedThreadPool`创建一个大小固定的线程池，可控制线程最大并发数，超出的线程会在队列中等待；
- `newScheduledThreadPool`创建一个大小固定的线程池，支持定时及周期性的任务执行；
- `newSingleThreadExecutor`创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。

此外，`newWorkStealingPool`还可以创建支持work-stealing的线程池。

说良心话，Java提供的`Executors`工具类使得我们对`ExecutorService`使用已经非常得心应手了。BUT~ Reactor让线程管理和任务调度更加“傻瓜”——调度器（Scheduler）帮助我们搞定这件事。`Scheduler`是一个拥有多个实现类的抽象接口。`Schedulers`类（按照通常的套路，最后为`s`的就是工具类咯）提供的静态方法可搭建以下几种线程执行环境：

- 当前线程（`Schedulers.immediate()`）；
- 可重用的单线程（`Schedulers.single()`）。注意，这个方法对所有调用者都提供同一个线程来使用， 直到该调度器被废弃。如果你想使用独占的线程，请使用`Schedulers.newSingle()`；
- 弹性线程池（`Schedulers.elastic()`）。它根据需要创建一个线程池，重用空闲线程。线程池如果空闲时间过长 （默认为 60s）就会被废弃。对于 I/O 阻塞的场景比较适用。`Schedulers.elastic()`能够方便地给一个阻塞 的任务分配它自己的线程，从而不会妨碍其他任务和资源；
- 固定大小线程池（`Schedulers.parallel()`），所创建线程池的大小与CPU个数等同；
- 自定义线程池（`Schedulers.fromExecutorService(ExecutorService)`）基于自定义的ExecutorService创建 Scheduler（虽然不太建议，不过你也可以使用Executor来创建）。

`Schedulers`类已经预先创建了几种常用的线程池：使用`single()`、`elastic()`和`parallel()`方法可以分别使用内置的单线程、弹性线程池和固定大小线程池。如果想创建新的线程池，可以使用`newSingle()`、`newElastic()`和`newParallel()`方法。

`Executors`提供的几种线程池在Reactor中都支持：

- `Schedulers.single()`和`Schedulers.newSingle()`对应`Executors.newSingleThreadExecutor()`；
- `Schedulers.elastic()`和`Schedulers.newElastic()`对应`Executors.newCachedThreadPool()`；
- `Schedulers.parallel()`和`Schedulers.newParallel()`对应`Executors.newFixedThreadPool()`；
- 下一章会介绍到，`Schedulers`提供的以上三种调度器底层都是基于`ScheduledExecutorService`的，因此都是支持任务定时和周期性执行的；
- `Flux`和`Mono`的调度操作符`subscribeOn`和`publishOn`支持work-stealing。

### 举例：将同步的阻塞调用变为异步的

前面介绍到`Schedulers.elastic()`能够方便地给一个阻塞的任务分配专门的线程，从而不会妨碍其他任务和资源。我们就可以利用这一点将一个同步阻塞的调用调度到一个自己的线程中，并利用订阅机制，待调用结束后异步返回。

假设我们有一个同步阻塞的调用方法：

```java
private String getStringSync() {
    try {
        TimeUnit.SECONDS.sleep(2);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    return "Hello, Reactor!";
}
```

正常情况下，调用这个方法会被阻塞2秒钟，然后同步地返回结果。我们借助elastic调度器将其变为异步，由于是异步的，为了保证测试方法所在的线程能够等待结果的返回，我们使用`CountDownLatch`：

```java
@Test
public void testSyncToAsync() throws InterruptedException {
    CountDownLatch countDownLatch = new CountDownLatch(1);
    Mono.fromCallable(() -> getStringSync())    // 1
            .subscribeOn(Schedulers.elastic())  // 2
            .subscribe(System.out::println, null, countDownLatch::countDown);
    countDownLatch.await(10, TimeUnit.SECONDS);
}

```

1. 使用`fromCallable`声明一个基于Callable的Mono；
2. 使用`subscribeOn`将任务调度到`Schedulers`内置的弹性线程池执行，弹性线程池会为Callable的执行任务分配一个单独的线程。

#### 总结

以上关于Reactor的介绍主要是概念层面和使用层面的介绍，不过应该也足以应对常见的业务环境了。

从命令式编程到响应式编程的切换并不是一件容易的事，需要一个适应的过程。不过相信你通过本节的了解和实操，已经可以体会到使用Reactor编程的一些特点：

- 相对于传统的基于回调和Future的异步开发方式，响应式编程更加具有**可编排性和可读性**，配合lambda表达式，代码更加简洁，处理逻辑的表达就像装配“流水线”，适用于对数据流的处理；
- 在**订阅（subscribe）时才触发数据流**，这种数据流叫做“冷”数据流，就像插座插上电器才会有电流一样，还有一种数据流不管是否有订阅者订阅它都会一直发出数据，称之为“热”数据流，Reactor中几乎都是“冷”数据流；
- **调度器对线程管理进行更高层次的抽象**，使得我们可以非常容易地切换线程执行环境；
- **灵活的错误处理机制**有利于编写健壮的程序；
- **“回压”机制**使得订阅者可以无限接受数据并让它的源头“满负荷”推送所有的数据，也可以通过使用`request`方法来告知源头它一次最多能够处理 n 个元素，从而将“推送”模式转换为“推送+拉取”混合的模式。