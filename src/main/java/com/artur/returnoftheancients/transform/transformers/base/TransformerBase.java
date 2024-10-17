package com.artur.returnoftheancients.transform.transformers.base;

import org.objectweb.asm.*;

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

    protected abstract IMVInstance[] getIMVInstances();
}
