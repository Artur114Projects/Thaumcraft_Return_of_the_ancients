package com.artur.returnoftheancients.util.multitread;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

public class AsyncCompileManager<T> implements Supplier<T> {
    private final Function<T, ? extends Supplier<T>> compilerFactory;
    private CompletableFuture<T> future = null;
    private T obj = null;

    public AsyncCompileManager(Function<T, ? extends Supplier<T>> compilerFactory) {
        this.compilerFactory = compilerFactory;
    }

    public void reCompile() {
        if (this.future != null) this.future.cancel(true);
        this.future = CompletableFuture.supplyAsync(compilerFactory.apply(this.obj));
        this.obj = null;
    }

    @Override
    public T get() {
        if (this.obj != null) {
            return this.obj;
        }

        if (this.future != null) {
            this.obj = Objects.requireNonNull(this.future.join());
            this.future = null;
            return obj;
        }

        this.reCompile();

        return this.get();
    }
}
