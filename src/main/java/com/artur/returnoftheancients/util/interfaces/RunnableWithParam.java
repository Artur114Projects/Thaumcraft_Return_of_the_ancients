package com.artur.returnoftheancients.util.interfaces;

@FunctionalInterface
public interface RunnableWithParam<T> {
    void run(T object);
}
