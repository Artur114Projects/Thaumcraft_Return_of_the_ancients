package com.artur.returnoftheancients.util.interfaces;

@FunctionalInterface
public interface Function2<A, B, R> {
    R apply(A a, B b);
}
