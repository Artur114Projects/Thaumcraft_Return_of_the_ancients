package com.artur.returnoftheancients.transform.transformers.base;

import org.objectweb.asm.*;

public abstract class TransformerBase implements ITransformer, Opcodes {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        System.out.println("Transform class [" + getTarget() + "]");

        ClassReader classReader = new ClassReader(basicClass);
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        classReader.accept(new ClassVisitor(ASM5, classWriter) {
            @Override
            public MethodVisitor visitMethod(int asses, String name, String desc, String signature, String[] exceptions) {
                MethodVisitor mv = super.visitMethod(asses, name, desc, signature, exceptions);
                for (IMVInstance imvInstance : getIMVInstances()) {
                    for (String target : imvInstance.getTargets()) {
                        String[] splitTarget = target.split("\\|");
                        if (name.equals(splitTarget[0]) && (splitTarget.length == 1 || desc.equals(splitTarget[1]))) {
                            System.out.println("Transform method [" + name + "], desc:[" + desc + "]");
                            return imvInstance.getInstance(mv);
                        }
                    }
                }
                return mv;
            }
        }, 0);

        return classWriter.toByteArray();
    }

    protected abstract IMVInstance[] getIMVInstances();
}
