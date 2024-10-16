package com.artur.returnoftheancients.transform.transformers;

import com.artur.returnoftheancients.transform.util.MappingsProcessor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.objectweb.asm.*;
import thaumcraft.common.blocks.world.taint.BlockTaint;

import java.util.Random;

public class TransformerBlockTaint implements ITransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        ClassReader classReader = new ClassReader(basicClass);
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        classReader.accept(new ClassVisitor(Opcodes.ASM5, classWriter) {
            @Override
            public MethodVisitor visitMethod(int asses, String name, String desc, String signature, String[] exceptions) {
                MethodVisitor mv = super.visitMethod(asses, name, desc, signature, exceptions);
                if (name.contains("randomDisplayTick")) {
                    System.out.println("add Transform (randomDisplayTick)");
                    return new RandomDisplayTickVisitor(mv);
                }
                return mv;
            }
        }, 0);

        return classWriter.toByteArray();
    }

    public static class RandomDisplayTickVisitor extends MethodVisitor {
        public RandomDisplayTickVisitor(MethodVisitor methodVisitor) {
            super(Opcodes.ASM5, methodVisitor);
        }

        @Override
        public void visitCode() {
            super.visitCode();

            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitVarInsn(Opcodes.ALOAD, 1);
            mv.visitVarInsn(Opcodes.ALOAD, 2);
            mv.visitVarInsn(Opcodes.ALOAD, 3);
            mv.visitVarInsn(Opcodes.ALOAD, 4);

            mv.visitMethodInsn(Opcodes.INVOKESTATIC, HANDLER_PATH, "randomDisplayTick", "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Ljava/util/Random;)V", false);
        }
    }

    @Override
    public String getTarget() {
        return "thaumcraft.common.blocks.world.taint.BlockTaint";
    }
}
