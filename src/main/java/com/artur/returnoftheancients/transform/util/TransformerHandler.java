package com.artur.returnoftheancients.transform.util;

import com.artur.returnoftheancients.generation.biomes.BiomeTaint;
import com.artur.returnoftheancients.init.InitBiome;
import com.artur.returnoftheancients.misc.TRAConfigs;
import com.chaosthedude.naturescompass.util.BiomeSearchWorker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import org.objectweb.asm.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class TransformerHandler {

    public static boolean isTaintBiome(World world, BlockPos pos) {
        return world.getBiome(pos).equals(InitBiome.TAINT);
    }
    public static boolean isClientPlayerInTaintBiome() {
        if (Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().world != null) {
            EntityPlayerSP player = Minecraft.getMinecraft().player;
            World world = Minecraft.getMinecraft().world;
            return world.getBiome(player.getPosition()).equals(InitBiome.TAINT);
        } else {
            return false;
        }
    }

    public static boolean isTaintEdgeBiome(World world, BlockPos pos) {
        return world.getBiome(pos).equals(InitBiome.TAINT_EDGE);
    }

    public static void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        if (world.isRemote) {
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + rand.nextDouble(), pos.getY() + rand.nextDouble(), pos.getZ() + rand.nextDouble(), 0, 0.1, 0);
        }
    }


    public static boolean isTaintBiome(BiomeSearchWorker bsw) {
        return !TRAConfigs.Any.debugMode && bsw.biome instanceof BiomeTaint;
    }

    public static class ReturnFloatVisitor extends MethodVisitor {

        private final float ret;

        public ReturnFloatVisitor(MethodVisitor methodVisitor, float value) {
            super(Opcodes.ASM5, methodVisitor);
            ret = value;
        }

        @Override
        public void visitCode() {
            super.visitCode();

            mv.visitLdcInsn(ret);
            mv.visitInsn(Opcodes.FRETURN);
        }
    }
}
