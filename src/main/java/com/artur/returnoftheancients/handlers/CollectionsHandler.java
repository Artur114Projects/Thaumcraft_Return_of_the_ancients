package com.artur.returnoftheancients.handlers;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

public class CollectionsHandler {
    public static <T extends Collection<Long>, O> T toLongCollection(T ret, Collection<O> collection, Function<O, Long> getLong) {
        for (O obj : collection) {
            ret.add(getLong.apply(obj));
        }
        return ret;
    }

    public static <T extends Map<O, K>, O, K> T toMap(T map, Collection<O> collection, Function<O, K> extractor) {
        for (O obj : collection) {
            map.put(obj, extractor.apply(obj));
        }
        return map;
    }

}
