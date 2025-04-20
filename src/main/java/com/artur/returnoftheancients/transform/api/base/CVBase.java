package com.artur.returnoftheancients.transform.api.base;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class CVBase extends ClassVisitor {
    protected final IMVInstance[] imvInstances;
    public CVBase(ClassVisitor cv, IMVInstance[] imvInstances) {
        super(Opcodes.ASM5, cv);

        this.imvInstances = imvInstances;
    }

    @Override
    public MethodVisitor visitMethod(int asses, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(asses, name, desc, signature, exceptions);
        for (IMVInstance imvInstance : this.imvInstances) {
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
}
