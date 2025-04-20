package com.artur.returnoftheancients.transform.api.base;

import org.objectweb.asm.MethodVisitor;

public interface IMVInstance {
    MethodVisitor getInstance(MethodVisitor mv);
    String[] getTargets();
}
