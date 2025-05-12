package com.artur.returnoftheancients.transform.apilegacy.base;

import org.objectweb.asm.*;

public abstract class TransformerBase implements ITransformer, Opcodes {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        System.out.println("Transform class [" + transformedName + "]");

//        ClassReader reader = new ClassReader(basicClass);
//        ClassNode classNode = new ClassNode(ASM5);
//        reader.accept(classNode, 0);
//        ClassWriter writer = new ClassWriter(0);
//        classNode.accept(writer);
//
//        for (MethodNode method : classNode.methods) {
//            System.out.println(method.name);
//            System.out.println(method.desc);
//        }

        ClassReader classReader = new ClassReader(basicClass);
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classReader.accept(this.getCWInstance(classWriter), 0);

        return classWriter.toByteArray();
    }

    protected abstract IMVInstance[] getIMVInstances();
    protected ClassVisitor getCWInstance(ClassVisitor cv) {return new CVBase(cv, this.getIMVInstances(), this.getTarget().replaceAll("\\.", "/"));}
}
