package com.artur.returnoftheancients.util.interfaces;

@FunctionalInterface
public interface RunnableWith2Params<T, R> {
    void run(T object0, R object1);
}
