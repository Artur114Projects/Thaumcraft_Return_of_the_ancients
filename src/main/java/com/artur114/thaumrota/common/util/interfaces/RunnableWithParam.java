package com.artur114.thaumrota.common.util.interfaces;

@FunctionalInterface
public interface RunnableWithParam<T> {
    void run(T object);
}
