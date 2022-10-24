package org.example;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class Test1 {
    @Test
    public void flux_mono() {
        Flux.just(1, 2, 3, 4, 5, 6).subscribe(System.out::print);
        System.out.println();
        Mono.just(1).subscribe(System.out::print);

        //Flux的声明方式
        Integer[] array = new Integer[]{1, 2, 3, 4, 5, 6};
        List<Integer> list = Arrays.asList(array);
        Stream<Integer> stream = list.stream();
        Flux.fromArray(array);
        Flux.fromIterable(list);
        Flux.fromStream(stream);
    }

    @Test
    public void empty_error() {
        // 只有完成信号的空数据流
        Flux.just();
        Flux.empty();
        Mono.empty();
        Mono.justOrEmpty(Optional.empty());
        // 只有错误信号的数据流
        Flux.error(new Exception("some error"));
        Mono.error(new Exception("some error"));
    }

    @Test
    public void subscribe() {
        Flux.just(1, 2, 3, 4, 5, 6).subscribe(System.out::println, System.err::println, () -> System.out.println("Completed!"));

    }

    @Test
    public void subscribe_error() {
        Mono.error(new Exception("some error")).subscribe(System.out::println, System.err::println, () -> System.out.println("Completed!"));
    }

    private Flux<Integer> generateFluxFrom1To6() {
        return Flux.just(1, 2, 3, 4, 5, 6);
    }

    private Mono<Integer> generateMonoWithError() {
        return Mono.error(new Exception("some error"));
    }

    @Test
    public void testViaStepVerifier() {
        StepVerifier.create(generateFluxFrom1To6()).expectNext(1, 2, 3, 4, 5, 6).expectComplete().verify();
        StepVerifier.create(generateMonoWithError()).expectErrorMessage("some error").verify();
    }

    @Test
    public void map() {
        StepVerifier.create(Flux.range(1, 6)    // 1
                        .map(i -> i * i))   // 2
                .expectNext(1, 4, 9, 16, 25, 36)    //3
                .expectComplete();  // 4
    }

    @Test
    public void flatMap() {
        StepVerifier.create(
                        Flux.just("flux", "mono")
                                .flatMap(s -> Flux.fromArray(s.split("\\s*"))   // 1
                                        .delayElements(Duration.ofMillis(100))) // 2
                                .doOnNext(System.out::print)) // 3
                .expectNextCount(8) // 4
                .verifyComplete();
    }

    @Test
    public void filter() {
        StepVerifier.create(Flux.range(1, 6)
                        .filter(i -> i % 2 == 1)    // 1
                        .map(i -> i * i))
                .expectNext(1, 9, 25)   // 2
                .verifyComplete();
    }

    private Flux<String> getZipDescFlux() {
        String desc = "Zip two sources together, that is to say wait for all the sources to emit one element and combine these elements once into a Tuple2.";
        return Flux.fromArray(desc.split("\\s+"));  // 1
    }

    @Test
    public void zip() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);  // 2
        Flux.zip(getZipDescFlux(), Flux.interval(Duration.ofMillis(200)))  // 3
                .subscribe(t -> System.out.println(t.getT1()), null, countDownLatch::countDown);    // 4
        countDownLatch.await(10, TimeUnit.SECONDS);     // 5
    }

    //有一个同步阻塞的调用方法
    private String getStringSync() {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Hello, Reactor!";
    }

    @Test
    public void testSyncToAsync() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Mono.fromCallable(() -> getStringSync())    // 1
                .subscribeOn(Schedulers.elastic())  // 2
                .subscribe(System.out::println, null, countDownLatch::countDown);
        countDownLatch.await(10, TimeUnit.SECONDS);
    }
}
