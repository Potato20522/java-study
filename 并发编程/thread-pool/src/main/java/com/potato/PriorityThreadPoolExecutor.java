package com.potato;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * execute()方法：不带返回值
 * 主线程调用本类的execute、submit之类的方法时，先将priority存入线程的局部变量，
 * 然后execute(Runnable command)时，再取出这个局部变量，创建PriorityRunnable对象，进入阻塞队列
 * 参考：https://www.jianshu.com/p/6c3021779015
 */
@Slf4j
public class PriorityThreadPoolExecutor extends ThreadPoolExecutor {
    private ThreadLocal<Integer> local = ThreadLocal.withInitial(() -> 0);
    private Map<Long, PriorityRunnable> map = new ConcurrentHashMap<>();

    public PriorityThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public PriorityThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public PriorityThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public PriorityThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    public PriorityThreadPoolExecutor() {
        super(1, 1, 60, TimeUnit.SECONDS, new PriorityBlockingQueue<>());
        super.allowCoreThreadTimeOut(false);
    }

    public Map<Long, PriorityRunnable> getMap() {
        return map;
    }

    /**
     * 其他所有的execute和submit方法最终都会调到这里，
     * 然后调用下面的那个重载，创建PriorityRunnable
     * 然后调用父类的execute，然后才真正入阻塞队列或者执行
     *
     * @param task 需要执行的函数,无入参、无返回值
     */
    @Override
    public void execute(Runnable task) {
        int priority = local.get();
        try {
            this.execute(task, priority);
        } finally {
            local.set(0);
        }
    }

    /**
     * @param task     需要执行的函数,无入参、无返回值
     * @param priority 优先级
     */
    public void execute(Runnable task, int priority) {
        //任务函数在这里正式入队列或者被执行
        PriorityRunnable runnable = new PriorityRunnable(task, priority);
        //map.put(runnable.seqNum, runnable);//todo 这里put之后，在哪里移除是个问题，没有回调函数了
        super.execute(runnable);
    }


    /**
     * @param task     需要执行的函数,无入参无返回
     * @param priority 优先级
     * @return 可添加回调
     */
    public CompletableFuture<Void> submit(Runnable task, int priority) {
        //todo PriorityRunnable 放到这里new? new完了通过ThreadLocal传递到execute里
        local.set(priority);//将优先级存入线程的局部变量
        return CompletableFuture.runAsync(task, this);
    }

    public CompletableFuture<Void> submit(Runnable task) {
        return this.submit(task, 0);
    }


    /**
     * @param task     需要执行的函数,无入参有返回值
     * @param priority 优先级
     * @param <T>      返回值类型
     * @return 可添加回调
     */
    public <T> CompletableFuture<T> submit(Supplier<T> task, int priority) {
        local.set(priority);//将优先级存入线程的局部变量
        return CompletableFuture.supplyAsync(task, this);
    }

    public <T> CompletableFuture<T> submit(Supplier<T> task) {
        return this.submit(task, 0);
    }


    /**
     * @param task     需要执行的函数,有入参无返回
     * @param priority 优先级
     * @param <T>      返回值类型
     * @return 可添加回调
     */
    public <T> CompletableFuture<Void> submit(Consumer<? super T> task, int priority) {
        local.set(priority);//将优先级存入线程的局部变量
        CompletableFuture<T> future = new CompletableFuture<>();
        return future.thenAcceptAsync(task, this);
    }

    public <T> CompletableFuture<Void> submit(Consumer<? super T> task) {
        return this.submit(task, 0);
    }

    /**
     * @param task     需要执行的函数,有入参有返回
     * @param priority 优先级
     * @param <T>      返回值类型
     * @return 可添加回调
     */
    public <T, U> CompletableFuture<? extends U> submit(Function<? super T, ? extends U> task, int priority) {
        local.set(priority);//将优先级存入线程的局部变量
        CompletableFuture<T> future = new CompletableFuture<>();
        return future.thenApplyAsync(task, this);
    }

    public <T, U> CompletableFuture<? extends U> submit(Function<? super T, ? extends U> task) {
        return this.submit(task, 0);
    }

    public Runnable removeTask(long id) {
        if (map.containsKey(id)) {
            return super.getQueue().remove();
        }
        return null;
    }

    static class PriorityRunnable implements Runnable, Comparable<PriorityRunnable> {
        private final static AtomicLong seq = new AtomicLong();
        public final long seqNum;
        private Runnable run;
        public final int priority;

        PriorityRunnable(Runnable run, int priority) {
            seqNum = seq.getAndIncrement();
            this.run = run;
            this.priority = priority;
        }

        @Override
        public void run() {
            this.run.run();
//            map.remove(seqNum);
        }

        @Override
        public int compareTo(PriorityRunnable other) {
            int res = 0;
            if (this.priority == other.priority) {
                if (other.run != this.run) {
                    // ASC
                    res = (seqNum < other.seqNum ? -1 : 1);
                }
            } else {
                // DESC
                res = this.priority > other.priority ? -1 : 1;
            }
            return res;
        }
    }

}
