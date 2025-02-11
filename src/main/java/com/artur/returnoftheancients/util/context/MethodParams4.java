package com.artur.returnoftheancients.util.context;

public class MethodParams4<A, B, C, E> extends MethodParamsBase {
    private final A a;
    private final B b;
    private final C c;
    private final E e;

    public MethodParams4(A a, B b, C c, E e) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.e = e;
    }

    public A getParam1() {
        return a;
    }

    public B getParam2() {
        return b;
    }

    public C getParam3() {
        return c;
    }

    public E getParam4() {
        return e;
    }

    @Override
    public Class<?>[] getParamsClasses() {
        return new Class[] {a.getClass(), b.getClass(), c.getClass(), e.getClass()};
    }

    @Override
    public Object[] getParamsObjects() {
        return new Object[] {a, b, c, e};
    }
}
