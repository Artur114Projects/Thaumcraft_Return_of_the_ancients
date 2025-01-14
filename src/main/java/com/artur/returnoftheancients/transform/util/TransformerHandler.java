package com.artur.returnoftheancients.transform.util;

import com.artur.returnoftheancients.client.misc.ClientEventsHandler;
import com.artur.returnoftheancients.generation.biomes.BiomeTaint;
import com.artur.returnoftheancients.handlers.HandlerR;
import com.artur.returnoftheancients.init.InitBiome;
import com.artur.returnoftheancients.misc.TRAConfigs;
import com.chaosthedude.naturescompass.util.BiomeSearchWorker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.world.biome.Biome;
import org.objectweb.asm.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class TransformerHandler {

    public static boolean isTaintLBiomeInPos(World world, BlockPos pos) {
        return HandlerR.arrayContainsAny(InitBiome.TAINT_BIOMES_L_ID, world.getChunkFromBlockCoords(pos).getBiomeArray()[(pos.getX() & 15) + (pos.getZ() & 15) * 16]);
    }

    public static boolean isClientPlayerInTaintBiome() {
        if (Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().world != null) {
            return ClientEventsHandler.isPlayerInTaintBiome();
        } else {
            return false;
        }
    }

    public static float getSunBrightnessInTaintBiome() {
        return ClientEventsHandler.getSunBrightnessInTaintBiome();
    }

    public static boolean isTaintEdgeBiome(World world, BlockPos pos) {
        return HandlerR.arrayContainsAny(InitBiome.TAINT_BIOMES_EDGE_ID, world.getChunkFromBlockCoords(pos).getBiomeArray()[(pos.getX() & 15) + (pos.getZ() & 15) * 16]);
    }

    public static void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        if (world.isRemote) {
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + rand.nextDouble(), pos.getY() + rand.nextDouble(), pos.getZ() + rand.nextDouble(), 0, 0.1, 0);
        }
    }

    public static boolean isNotCanSearchBiome(BiomeSearchWorker bsw) {
        return !TRAConfigs.Any.debugMode && HandlerR.arrayContainsAny(InitBiome.TAINT_BIOMES_ID, (byte) Biome.getIdForBiome(bsw.biome));
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
