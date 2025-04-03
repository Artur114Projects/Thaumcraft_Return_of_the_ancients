package com.artur.returnoftheancients.util.multitread;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;

public class TreadGarbageCollector<O> extends Thread {
    private final Function<O, Boolean> isGarbage;
    private final Collection<O>[] collections;
    private final int waitingTimeMs;

    @SafeVarargs
    public TreadGarbageCollector(int waitingTimeMs, Function<O, Boolean> isGarbage, Collection<O>... collections) {
        this.waitingTimeMs = waitingTimeMs;
        this.collections = collections;
        this.isGarbage = isGarbage;
    }

    @Override
    public void run() {
        for (Collection<O> collection : this.collections) {

            Iterator<O> iterator = collection.iterator();

            while (iterator.hasNext()) {
                O obj = iterator.next();

                if (this.isGarbage.apply(obj)) {
                    iterator.remove();
                }

                try {
                    Thread.sleep(waitingTimeMs);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
