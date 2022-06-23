package com.potato;

import java.util.concurrent.Callable;

public class PriorityCallable<V> implements Callable<V> {

    private final int priority;

    public PriorityCallable(int priority) {
        this.priority = priority;
    }

    @Override
    public V call() throws Exception {
        return null;
    }

    public int getPriority() {
        return priority;
    }
    
}