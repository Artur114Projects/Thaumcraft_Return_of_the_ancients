package com.artur.returnoftheancients.util.context;

import java.util.Arrays;

public abstract class MethodParamsBase {
    public abstract Class<?>[] getParamsClasses();
    public abstract Object[] getParamsObjects();

    @Override
    public int hashCode() {
        int code = 0;
        for (Object obj : this.getParamsObjects()) {
            code += obj.hashCode();
        }
        return code;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MethodParamsBase) {
            MethodParamsBase methodParams = (MethodParamsBase) obj;
            return Arrays.equals(methodParams.getParamsObjects(), this.getParamsObjects());
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        for (Object obj : this.getParamsObjects()) {
            builder.append(obj.toString());
            builder.append(", ");
        }
        builder.delete(builder.capacity() - 3, builder.capacity());
        builder.append(']');
        return builder.toString();
    }
}
