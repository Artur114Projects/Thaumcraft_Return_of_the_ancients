package com.artur114.thaumrota.common.util.interfaces;

@FunctionalInterface
public interface RunnableWith2Params<T, R> {
    void run(T object0, R object1);
}
