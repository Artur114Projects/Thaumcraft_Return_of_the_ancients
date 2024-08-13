package com.artur.returnoftheancients.main;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtils {

    public static Method getPrivateMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
        Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method;
    }

    public static Object invokePrivateMethod(Object instance, Method method, Object... args) throws Exception {
        return method.invoke(instance, args);
    }

    public static Field getPrivateField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field;
    }

    public static Object getPrivateFieldValue(Object instance, Field field) throws IllegalAccessException {
        return field.get(instance);
    }

    public static void setPrivateFieldValue(Object instance, Field field, Object value) throws IllegalAccessException {
        field.set(instance, value);
    }
}
