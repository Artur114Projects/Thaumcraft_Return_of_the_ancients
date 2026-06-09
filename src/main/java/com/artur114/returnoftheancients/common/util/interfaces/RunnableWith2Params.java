package com.artur114.returnoftheancients.common.util.interfaces;

@FunctionalInterface
public interface RunnableWith2Params<T, R> {
    void run(T object0, R object1);
}
