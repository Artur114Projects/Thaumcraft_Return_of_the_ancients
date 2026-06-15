package com.artur114.thaumrota.asm;

import com.artur114.bananalib.mc.BananaMC;
import com.artur114.thaumrota.client.event.ClientEventsHandler;
import com.artur114.thaumrota.common.config.RotAConfigs;
import com.artur114.thaumrota.common.init.InitBiomes;
import com.chaosthedude.naturescompass.util.BiomeSearchWorker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import thaumcraft.client.lib.events.RenderEventHandler;

public class ASMHookRotA {
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
        return !RotAConfigs.Any.debugMode && BananaMC.biomeHasType(bsw.biome, InitBiomes.TAINT_TYPE);
    }

    public static void fixedFogDensityEvent(EntityViewRenderEvent.RenderFogEvent event) {
        if (RenderEventHandler.fogFiddled && RenderEventHandler.fogTarget > 0.0F) {
            GlStateManager.setFog(GlStateManager.FogMode.EXP);
            GlStateManager.setFogDensity(RenderEventHandler.fogTarget);
        }
    }
}
