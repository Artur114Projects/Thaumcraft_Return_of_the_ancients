package com.artur.returnoftheancients.main;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Oooo {
    public boolean meow(World world, BlockPos pos) {
        if (!world.isAirBlock(pos) && !world.isAnyPlayerWithinRangeAt(pos.getX(), pos.getY(), pos.getZ(), 20)) {
            return false;
        } else {
            return world.getBlockState(pos.down()).isFullBlock();
        }
    }

    /*
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitVarInsn(ALOAD, 2);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/util/math/BlockPos", "getX", "()I", false);
            methodVisitor.visitInsn(I2D);
            methodVisitor.visitVarInsn(ALOAD, 2);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/util/math/BlockPos", "getY", "()I", false);
            methodVisitor.visitInsn(I2D);
            methodVisitor.visitVarInsn(ALOAD, 2);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/util/math/BlockPos", "getZ", "()I", false);
            methodVisitor.visitInsn(I2D);
            methodVisitor.visitLdcInsn(new Double("20.0"));
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/world/World", "isAnyPlayerWithinRangeAt", "(DDDD)Z", false);
            methodVisitor.visitJumpInsn(IFEQ, label0);

            {
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "meow", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z", null, null);
            methodVisitor.visitCode();
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitVarInsn(ALOAD, 2);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/world/World", "isAirBlock", "(Lnet/minecraft/util/math/BlockPos;)Z", false);
            Label label0 = new Label();
            methodVisitor.visitJumpInsn(IFNE, label0);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitVarInsn(ALOAD, 2);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/util/math/BlockPos", "getX", "()I", false);
            methodVisitor.visitInsn(I2D);
            methodVisitor.visitVarInsn(ALOAD, 2);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/util/math/BlockPos", "getY", "()I", false);
            methodVisitor.visitInsn(I2D);
            methodVisitor.visitVarInsn(ALOAD, 2);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/util/math/BlockPos", "getZ", "()I", false);
            methodVisitor.visitInsn(I2D);
            methodVisitor.visitLdcInsn(new Double("20.0"));
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/world/World", "isAnyPlayerWithinRangeAt", "(DDDD)Z", false);
            methodVisitor.visitJumpInsn(IFEQ, label0);
            methodVisitor.visitInsn(ICONST_0);
            methodVisitor.visitInsn(IRETURN);
            methodVisitor.visitLabel(label0);
            methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitVarInsn(ALOAD, 2);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/util/math/BlockPos", "down", "()Lnet/minecraft/util/math/BlockPos;", false);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/world/World", "getBlockState", "(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;", false);
            methodVisitor.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/block/state/IBlockState", "isFullBlock", "()Z", true);
            methodVisitor.visitInsn(IRETURN);
            methodVisitor.visitMaxs(9, 3);
            methodVisitor.visitEnd();
        }
     */
}
