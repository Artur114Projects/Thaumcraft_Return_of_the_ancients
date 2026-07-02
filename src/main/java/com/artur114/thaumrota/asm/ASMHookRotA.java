package com.artur114.thaumrota.asm;

import com.artur114.bananalib.mc.BananaMC;
import com.artur114.thaumrota.client.event.ClientEventsHandler;
import com.artur114.thaumrota.common.config.RotAConfig;
import com.artur114.thaumrota.common.init.InitBiomes;
import com.artur114.thaumrota.common.init.InitDimensions;
import com.artur114.thaumrota.main.ThaumRotA;
import com.chaosthedude.naturescompass.util.BiomeSearchWorker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import thaumcraft.client.lib.events.RenderEventHandler;

public class ASMHookRotA {
    public static float hookGammaSetting() {
        Minecraft mc = Minecraft.getMinecraft();
        if (!RotAConfig.client.doInterceptGammaSetting) {
            return mc.gameSettings.gammaSetting;
        }
        if (mc.player != null && (mc.player.dimension == InitDimensions.ANCIENT_WORLD_ID || BananaMC.biomeHasType(ClientEventsHandler.PLAYER_IN_BIOME_MANAGER.currentBiome, InitBiomes.TAINT_TYPE_L))) {
            return 0;
        } else {
            return mc.gameSettings.gammaSetting;
        }
    }

    public static boolean isTaintLBiomeInPos(World world, BlockPos pos) {
        return BananaMC.biomeHasType(world.getChunkFromBlockCoords(pos).getBiomeArray()[(pos.getX() & 15) + (pos.getZ() & 15) * 16], InitBiomes.TAINT_TYPE_L);
    }

    public static boolean isClientPlayerInTaintBiome() {
        Minecraft mc = Minecraft.getMinecraft();
        if (Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().world != null) {
            return ClientEventsHandler.PLAYER_IN_BIOME_MANAGER.isPlayerInTaintBiome();
        } else {
            return false;
        }
    }

    public static float getSunBrightnessInTaintBiome() {
        return ClientEventsHandler.PLAYER_IN_BIOME_MANAGER.getSunBrightnessInTaintBiome();
    }

    public static boolean isNotCanSearchBiome(BiomeSearchWorker bsw) {
        return !ThaumRotA.isDevEnv() && BananaMC.biomeHasType(bsw.biome, InitBiomes.TAINT_TYPE);
    }

    public static void fixedFogDensityEvent(EntityViewRenderEvent.RenderFogEvent event) {
        if (Minecraft.getMinecraft().player.dimension == InitDimensions.ANCIENT_WORLD_ID) {
            return;
        }
        if (RenderEventHandler.fogFiddled && RenderEventHandler.fogTarget > 0.0F) {
            GlStateManager.setFog(GlStateManager.FogMode.EXP);
            GlStateManager.setFogDensity(RenderEventHandler.fogTarget);
        }
    }
}
