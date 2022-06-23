package com.potato;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
class PriorityThreadPoolExecutorTest {
    AtomicInteger count = new AtomicInteger();
    PriorityThreadPoolExecutor executor = new PriorityThreadPoolExecutor();
    @Test
    void execute() {

    }

    @Test
    void testExecute() {
    }

    @Test
    void submit() {
        executor.submit(this::job,1).whenComplete((re,ex)->{
            log.info("任务0，结束");
        });
        executor.submit(this::job,1).whenComplete((re,ex)->{
            log.info("任务1，结束");
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        });
        executor.submit(this::job,1).exceptionally((ex)->{
            log.info("任务2，结束");
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            return null;//Void类型的job return null就行
        });
        //任务3取消
        executor.submit(this::job,1).cancel(true);
        sleep(30000);
    }

    //Runnning的线程，在执行CPU是无法取消的，只有blocked/new/Runnable的线程，才可以尝试取消；
    @Test
    void cancel() {
        CompletableFuture<Void> future = executor.submit(this::job, 1);
        sleep(500);
        //future.cancel(true);无法取消正在运行的，只能取消没有开始运行的
        try {
            future.get(1, TimeUnit.MILLISECONDS);//还是无法取消正在运行的
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
        sleep(30000);
    }

    @Test
    void testSubmit() {
    }

    @Test
    void testSubmit1() {
    }

    @Test
    void testSubmit2() {
    }

    @Test
    void removeTask() {
    }

    void job() {
        int i = count.getAndIncrement();
        log.info("任务开始:{}",i);
        sleep(2000);
        if (i == 1) {
            throw new RuntimeException("任务："+i+",抛异常");
        }
        if (i == 2) {
            throw new RuntimeException("任务："+i+",抛异常");
        }
        log.info("任务结束:{}",i);
    }

    void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}