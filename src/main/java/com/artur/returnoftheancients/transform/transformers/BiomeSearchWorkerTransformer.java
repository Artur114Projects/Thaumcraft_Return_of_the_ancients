package com.artur.returnoftheancients.transform.transformers;

import com.artur.returnoftheancients.generation.biomes.BiomeTaint;
import com.artur.returnoftheancients.init.InitBiome;
import com.artur.returnoftheancients.misc.TRAConfigs;
import com.chaosthedude.naturescompass.util.BiomeSearchWorker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.objectweb.asm.*;

public class BiomeSearchWorkerTransformer implements ITransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        ClassReader classReader = new ClassReader(basicClass);
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        classReader.accept(new ClassVisitor(Opcodes.ASM5, classWriter) {
            @Override
            public MethodVisitor visitMethod(int asses, String name, String desc, String signature, String[] exceptions) {
                MethodVisitor mv = super.visitMethod(asses, name, desc, signature, exceptions);
                if (name.equals("doWork")) {
                    return new DoWorkVisitor(mv);
                }

                return mv;
            }
        }, 0);

        return classWriter.toByteArray();

    }

    @Override
    public String getTarget() {
        return "com.chaosthedude.naturescompass.util.BiomeSearchWorker";
    }

    private static class DoWorkVisitor extends MethodVisitor {

        public DoWorkVisitor(MethodVisitor mv) {
            super(Opcodes.ASM5, mv);
        }

        @Override
        public void visitInsn(int opcode) {
            super.visitInsn(opcode);
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/artur/returnoftheancients/transform/transformers/BiomeSearchWorkerTransformer", "isTaintBiome", "(Lcom/chaosthedude/naturescompass/util/BiomeSearchWorker;)Z", false);

            Label continueLabel = new Label();
            mv.visitJumpInsn(Opcodes.IFEQ, continueLabel);

            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitInsn(Opcodes.ICONST_0);

            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "com/chaosthedude/naturescompass/util/BiomeSearchWorker", "finish", "(Z)V", false);

            mv.visitInsn(Opcodes.ICONST_0);
            mv.visitInsn(Opcodes.IRETURN);

            mv.visitLabel(continueLabel);
        }
    }

    public static boolean isTaintBiome(BiomeSearchWorker bsw) {
        return !TRAConfigs.Any.debugMode && bsw.biome instanceof BiomeTaint;
    }
}
