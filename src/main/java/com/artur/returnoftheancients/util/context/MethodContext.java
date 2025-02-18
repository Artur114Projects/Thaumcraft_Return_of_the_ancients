package com.artur.returnoftheancients.util.context;

import java.util.*;
import java.util.stream.Collectors;

public class MethodContext<R, P extends MethodParamsBase> {
    private final Map<P, R> retFromParamsMap;
    private final LinkedList<R> results;
    private final LinkedList<P> params;
    private final int contextDeep;

    protected MethodContext(int contextDeep) {
        this.retFromParamsMap = new HashMap<>(contextDeep);
        this.results = new LinkedList<>();
        this.params = new LinkedList<>();
        this.contextDeep = contextDeep;
    }

    protected void addInvoke(R result, P param) {
        retFromParamsMap.put(param, result);
        results.addFirst(result);
        params.addFirst(param);

        if (params.size() > contextDeep) {
            retFromParamsMap.remove(params.pollLast());
        }
        if (results.size() > contextDeep) {
            results.removeLast();
        }
    }

    public boolean hasResultFromParam(P param) {
        return retFromParamsMap.containsKey(param);
    }

    public R getResultFromParam(P param) {
          return retFromParamsMap.get(param);
    }

    public boolean hasInvokes() {
        return !retFromParamsMap.isEmpty();
    }

    public R getLastReturn() {
        return results.getFirst();
    }

    public P getLastParam() {
        return params.getFirst();
    }

    public R getReturn(int index) {
        if (index >= params.size() || index < 0) {
            throw new IllegalArgumentException("Out of bound! Index:" + index + " Results size:" + params.size());
        }
        return results.get(index);
    }

    public P getParam(int index) {
        if (index >= params.size() || index < 0) {
            throw new IllegalArgumentException("Out of bound! Index:" + index + " Params size:" + params.size());
        }
        return params.get(index);
    }

    public List<R> getLastReturns(int count) {
        if (count > results.size()) {
            throw new IllegalArgumentException("Out of bound! Count:" + count + " Results size:" + params.size());
        }

        Iterator<R> iterator = results.iterator();
        List<R> ret = new ArrayList<>(results.size());

        while (count > 0) {
            ret.add(iterator.next());
            count--;
        }

        return ret;
    }

    public List<P> getLastParams(int count) {
        if (count > params.size()) {
            throw new IllegalArgumentException("Out of bound! Count:" + count + " Params size:" + params.size());
        }

        Iterator<P> iterator = params.iterator();
        List<P> ret = new ArrayList<>(params.size());

        while (count > 0) {
            ret.add(iterator.next());
            count--;
        }
 
        return ret;
    }

    public List<R> getAllReturns() {
        return new ArrayList<>(results);
    }

    public List<P> getAllParams() {
        return new ArrayList<>(params);
    }
}
