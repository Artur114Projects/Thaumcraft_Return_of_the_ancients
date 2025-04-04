package com.artur.returnoftheancients.util;

import com.artur.returnoftheancients.energy.system.EnergySystem;

import java.util.Objects;

public class Tuple<A, B> extends net.minecraft.util.Tuple<A, B> {
    public Tuple(A aIn, B bIn) {
        super(aIn, bIn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getFirst(), this.getSecond());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Tuple)) return false;

        Tuple<?, ?> tuple = (Tuple<?, ?>) obj;

        return Objects.equals(tuple.getFirst(), this.getFirst()) && Objects.equals(tuple.getSecond(), this.getSecond());
    }
}
