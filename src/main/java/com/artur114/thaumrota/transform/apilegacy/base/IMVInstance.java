package com.artur114.thaumrota.transform.apilegacy.base;

import org.objectweb.asm.MethodVisitor;

public interface IMVInstance {
    MethodVisitor getInstance(MethodVisitor mv);
    String[] getTargets();
}
