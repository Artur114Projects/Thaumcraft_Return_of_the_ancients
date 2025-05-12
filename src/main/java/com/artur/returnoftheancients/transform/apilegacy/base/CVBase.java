package com.artur.returnoftheancients.transform.apilegacy.base;

import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class CVBase extends ClassVisitor {
    protected final IMVInstance[] imvInstances;
    protected final String owner;
    public CVBase(ClassVisitor cv, IMVInstance[] imvInstances, String owner) {
        super(Opcodes.ASM5, cv);

        this.imvInstances = imvInstances;
        this.owner = owner;
    }

    @Override
    public MethodVisitor visitMethod(int asses, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(asses, name, desc, signature, exceptions);
        for (IMVInstance imvInstance : this.imvInstances) {
            for (String target : imvInstance.getTargets()) {
                String[] splitTarget = target.split("\\|");
                if (name.equals(splitTarget[0]) && (splitTarget.length == 1 || desc.equals(splitTarget[1]))) {
                    System.out.println("Transform method [" + FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(FMLDeobfuscatingRemapper.INSTANCE.unmap(owner), name, desc) + "], desc:[" + desc + "]");
                    return imvInstance.getInstance(mv);
                }
                System.out.println(FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(FMLDeobfuscatingRemapper.INSTANCE.unmap(owner), name, desc));
                System.out.println("name: " + name + ", desc: " + desc);
            }
        }
        return mv;
    }
}
