package com.artur.returnoftheancients.handlers;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class GraphHandler {
    public static <T> void startBFS(T startElemet, Function<T, Collection<T>> getNeighbors, Function<T, Boolean> needProcess, Function<T, Boolean> perform) {
        ArrayDeque<T> queue = new ArrayDeque<>(20);
        HashSet<T> checked = new HashSet<>(20);

        queue.addLast(startElemet);
        checked.add(startElemet);

        while (!queue.isEmpty()) {
            T obj = queue.poll();

            for (T neighbor : getNeighbors.apply(obj)) {
                if (checked.contains(neighbor)) continue;
                if (!needProcess.apply(neighbor)) continue;
                if (perform.apply(neighbor)) return;
                queue.addLast(neighbor);
                checked.add(neighbor);
            }
        }
    }
}
