package com.potato;


@FunctionalInterface
public interface Callback<T>  {
    void accept(T i);
}
