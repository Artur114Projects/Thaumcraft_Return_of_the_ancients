package com.artur.returnoftheancients.transform.api.base;

import org.objectweb.asm.*;

public abstract class TransformerBase implements ITransformer, Opcodes {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        System.out.println("Transform class [" + transformedName + "]");

        ClassReader classReader = new ClassReader(basicClass);
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classReader.accept(this.getCWInstance(classWriter), 0);

        return classWriter.toByteArray();
    }

    protected abstract IMVInstance[] getIMVInstances();
    protected ClassVisitor getCWInstance(ClassVisitor cv) {return new CVBase(cv, this.getIMVInstances());}
}
