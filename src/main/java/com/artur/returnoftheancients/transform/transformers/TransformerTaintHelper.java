package com.artur.returnoftheancients.transform.transformers;

import com.artur.returnoftheancients.init.InitBiome;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.objectweb.asm.*;


// TODO: Добавить трансформер для spreadFibres чтобы он не заражал TAINT_EDGE.
public class TransformerTaintHelper implements ITransformer {

    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        ClassReader classReader = new ClassReader(basicClass);
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        classReader.accept(new ClassVisitor(Opcodes.ASM5, classWriter) {
            @Override
            public MethodVisitor visitMethod(int asses, String name, String desc, String signature, String[] exceptions) {
                MethodVisitor mv = super.visitMethod(asses, name, desc, signature, exceptions);
                if (name.equals("isNearTaintSeed")) {
                    return new TransformerTaintHelper.VisitorIsNearTaintSeed(mv);
                }

                return mv;
            }
        }, 0);

        return classWriter.toByteArray();
    }

    @Override
    public String getTarget() {
        return "thaumcraft.common.blocks.world.taint.TaintHelper";
    }

    public static class VisitorIsNearTaintSeed extends MethodVisitor {
        public VisitorIsNearTaintSeed(MethodVisitor methodVisitor) {
            super(Opcodes.ASM5, methodVisitor);
        }

        @Override
        public void visitCode() {
            super.visitCode();

            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitVarInsn(Opcodes.ALOAD, 1);

            mv.visitMethodInsn(Opcodes.INVOKESTATIC, HANDLER_PATH, "isTaintBiome", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z", false);

            Label continueLabel = new Label();
            mv.visitJumpInsn(Opcodes.IFEQ, continueLabel);

            mv.visitInsn(Opcodes.ICONST_1);
            mv.visitInsn(Opcodes.IRETURN);

            mv.visitLabel(continueLabel);
        }
    }


}
