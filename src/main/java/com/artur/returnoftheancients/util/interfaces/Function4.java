package com.artur.returnoftheancients.util.interfaces;

@FunctionalInterface
public interface Function4<A, B, C, D, R> {
    R apply(A a, B b, C c, D d);
}
