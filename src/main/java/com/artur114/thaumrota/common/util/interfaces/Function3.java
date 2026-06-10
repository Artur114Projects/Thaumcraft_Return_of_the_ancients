package com.artur114.thaumrota.common.util.interfaces;

@FunctionalInterface
public interface Function3<A, B, C, R> {
    R apply(A a, B b, C c);
}
