package com.artur.returnoftheancients.transform.transformers.base;

import com.artur.returnoftheancients.misc.TRAConfigs;
import org.objectweb.asm.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class TransformerBase implements ITransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        ClassReader classReader = new ClassReader(basicClass);
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        classReader.accept(new ClassVisitor(Opcodes.ASM5, classWriter) {
            @Override
            public MethodVisitor visitMethod(int asses, String name, String desc, String signature, String[] exceptions) {
                MethodVisitor mv = super.visitMethod(asses, name, desc, signature, exceptions);
                for (IMVInstance imvInstance : getIMVInstances()) {
                    for (String target : imvInstance.getTargets()) {
                        if (name.contains(target)) {
                            return imvInstance.getInstance(mv);
                        }
                    }
                }
                return mv;
            }
        }, 0);

        return classWriter.toByteArray();
    }

    protected String createDescriptor(Class<?> returnValue, Class<?>... params) {
        StringBuilder builder = new StringBuilder("(");
        for (Class<?> clas : params) {
            builder.append(formatDescriptor(clas));
            builder.append(';');
        }
        builder.append(')');
        builder.append(formatDescriptor(returnValue));
        String res = builder.toString();
        if (TRAConfigs.Any.debugMode) System.out.println("Descriptor is created {" + res + "}");
        return res;
    }

    protected String createDescriptor(Class<?> methodClass, String methodName, Class<?>... params) {
        Method[] methods = findMethods(methodClass, methodName);
        if (methods.length == 0) {
            return "null";
        }

        Method findMethod = null;
        if (params.length == 0) {
            findMethod = methods[0];
        } else {
            for (Method method : methods) {
                if (Arrays.equals(method.getParameterTypes(), params)) {
                    findMethod = method;
                    break;
                }
            }
            if (findMethod == null) {
                return "null";
            }
        }

        StringBuilder builder = new StringBuilder("(");
        for (Class<?> clas : findMethod.getParameterTypes()) {
            builder.append(formatDescriptor(clas));
            builder.append(';');
        }
        builder.append(')');
        builder.append(formatDescriptor(findMethod.getReturnType()));
        String res = builder.toString();
        if (TRAConfigs.Any.debugMode) System.out.println("Descriptor is created {" + res + "}");
        return res;
    }

    protected String formatDescriptor(Class<?> param) {
        if (param == boolean.class) {
            return "Z";
        } else if (param == byte.class) {
            return "B";
        } else if (param == char.class) {
            return "C";
        } else if (param == double.class) {
            return "D";
        } else if (param == float.class) {
            return "F";
        } else if (param == int.class) {
            return "I";
        } else if (param == long.class) {
            return "J";
        } else if (param == short.class) {
            return "S";
        } else if (param == void.class) {
            return "V";
        }

        String name = param.getName();
        if (!name.contains("[L")) {
            name = "L" + name;
        }
        name = name.replaceAll("\\.", "/");
        return name;
    }

    protected Method[] findMethods(Class<?> methodClass, String methodName) {
        ArrayList<Method> methods = new ArrayList<>();
        for (Method method : methodClass.getDeclaredMethods()) {
            if (method.getName().equals(methodName)) {
                methods.add(method);
            }
        }
        Class<?> superClass = methodClass.getSuperclass();
        while (superClass != null) {
            for (Method method : superClass.getDeclaredMethods()) {
                if (method.getName().equals(methodName)) {
                    methods.add(method);
                }
            }
            superClass = superClass.getSuperclass();
        }
        return methods.toArray(new Method[0]);
    }

    protected abstract IMVInstance[] getIMVInstances();
}
