package com.artur.returnoftheancients.util;

import com.artur.returnoftheancients.energy.system.EnergySystem;

import java.util.Objects;

public class Tuple<A, B> {
    private final boolean immutable;
    private A a;
    private B b;

    public Tuple(A aIn, B bIn, boolean immutable) {
        this.immutable = immutable;
        this.a = aIn;
        this.b = bIn;
    }

    public Tuple(A aIn, B bIn) {
        this(aIn, bIn, true);
    }

    public A getFirst() {
        return this.a;
    }

    public B getSecond() {
        return this.b;
    }

    public void setFirst(A aIn) {
        if (!this.immutable) {
            this.a = aIn;
        } else {
            throw new IllegalStateException("Attempt mutable immutable object!");
        }
    }

    public void setSecond(B bIn) {
        if (!this.immutable) {
            this.b = bIn;
        } else {
            throw new IllegalStateException("Attempt mutable immutable object!");
        }
    }

    public boolean isImmutable() {
        return immutable;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.a, this.b);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Tuple)) return false;

        Tuple<?, ?> tuple = (Tuple<?, ?>) obj;

        return Objects.equals(tuple.getFirst(), this.getFirst()) && Objects.equals(tuple.getSecond(), this.getSecond());
    }
}
