package com.artur.returnoftheancients.util.context;

import java.util.*;

public class MethodsContextManager<R, P extends MethodParamsBase> {
    private final Map<String, MethodContext<R, P>> methodInvokesMap = new HashMap<>();
    private final int contextDeep;


    public MethodsContextManager(int contextDeep) {
        this.contextDeep = contextDeep;
    }

    public void onMethodInvoke(String name, P params, R result) {
        MethodContext<R, P> methodContext = methodInvokesMap.get(name);

        if (methodContext == null) {
            methodContext = new MethodContext<>(contextDeep);
            methodInvokesMap.put(name, methodContext);
        }

        methodContext.addInvoke(result, params);
    }

    public boolean hasMethodContext(String name) {
        return methodInvokesMap.containsKey(name);
    }

    public MethodContext<R, P> getMethodContext(String name) {
        MethodContext<R, P> methodContext = methodInvokesMap.get(name);

        if (methodContext == null) {
            methodContext = new MethodContext<>(contextDeep);
            methodInvokesMap.put(name, methodContext);
        }

        return methodContext;
    }
}