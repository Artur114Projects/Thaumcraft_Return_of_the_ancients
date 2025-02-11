package com.artur.returnoftheancients.transform.transformers.base;

import com.artur.returnoftheancients.handlers.HandlerR;
import org.objectweb.asm.*;

public abstract class MVWithDescCreator extends MethodVisitor {
    protected final String[] names;
    protected final String[] desc;

    public MVWithDescCreator(MethodVisitor mv, Class<?> methodsClass, String... names) {
        super(Opcodes.ASM5, mv);
        this.names = names;
        this.desc = new String[names.length];
        for (int i = 0; i != names.length; i++) {
            this.desc[i] = HandlerR.createDescriptor(methodsClass, names[i]);
        }
    }

    public MVWithDescCreator(MethodVisitor mv, String... names) {
        this(mv, ITransformer.HANDLER_CLASS, names);
    }
}
