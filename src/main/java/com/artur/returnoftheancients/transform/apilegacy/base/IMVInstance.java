package com.artur.returnoftheancients.transform.apilegacy.base;

import org.objectweb.asm.MethodVisitor;

public interface IMVInstance {
    MethodVisitor getInstance(MethodVisitor mv);
    String[] getTargets();
}
