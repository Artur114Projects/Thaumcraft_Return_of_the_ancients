package com.artur.returnoftheancients.client.event.managers;

import com.artur.returnoftheancients.client.event.ClientEventsHandler;
import com.artur.returnoftheancients.handlers.MiscHandler;
import com.artur.returnoftheancients.init.InitBiome;
import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.client.lib.events.RenderEventHandler;

import java.util.HashMap;
import java.util.Map;

import static com.artur.returnoftheancients.init.InitDimensions.ancient_world_dim_id;

@SideOnly(Side.CLIENT)
public class ClientPlayerInBiomeManager {
    private final TaintFog taintFogParams = new TaintFog(43.0F / 4.0f, 0, 61.0F / 4.0f, 20);
    public final UltraMutableBlockPos ultraBlockPos = new UltraMutableBlockPos();
    public Map<Biome, TaintFog> fogParamsMap = new HashMap<>();
    public boolean isCurrentBiomeTaint = false;
    public int maxPlayerInTaintBiomeTime = 80;
    public boolean isPrevBiomeTaint = false;
    public int playerInTaintBiomeTime = 0;
    public Runnable runOnGround = null;
    public Biome currentBiome = null;
    public byte currentBiomeId = -1;
    public float defaultSun = 0.0F;
    public Biome prevBiome = null;


    public ClientPlayerInBiomeManager() {
        fogParamsMap.put(InitBiome.TAINT_SEA, new TaintFog(70.0F / 7.0F, 90.0F / 8.0F, 100.0F / 7.0F, 40));
        fogParamsMap.put(InitBiome.TAINT_BEACH, new TaintFog(70.0F / 7.0F, 90.0F / 8.0F, 100.0F / 7.0F, 30));
        fogParamsMap.put(InitBiome.TAINT_MOUNTAINS, new TaintFog(43.0F / 5.0f, 0, 61.0F / 5.0f, 30));
        fogParamsMap.put(InitBiome.TAINT_EXTREME_MOUNTAINS, new TaintFog(43.0F / 8.0f, 0, 61.0F / 8.0f, 30));
        fogParamsMap.put(InitBiome.INFERNAL_CRATER, new TaintFog(255.0F * 0.5F, 255.0F * 0.2F, 0, 80));
        fogParamsMap.put(InitBiome.TAINT_WASTELAND, new TaintFog(255.0F * (0.5F / 8.0F), 255.0F * (0.2F / 8.0F), 2, 40));
        fogParamsMap.put(InitBiome.PRE_TERMAL_ZONE, new TaintFog(255.0F * (0.5F / 16.0F), 255.0F * (0.2F / 16.0F), 2, 40));
    }

    public void entityViewRenderEventFogColors(EntityViewRenderEvent.FogColors e) {
        if (e.getEntity().getEntityWorld().provider.getDimension() == ancient_world_dim_id) {
            e.setRed(1.0F * 0.8F);
            e.setGreen(0.4F * 0.8F);
            e.setBlue(0.0F);
        }
    }

    public void entityViewRenderEventRenderFogEvent(EntityViewRenderEvent.RenderFogEvent e) {
        if (e.getEntity().getEntityWorld().provider.getDimension() == ancient_world_dim_id) {
            GlStateManager.setFog(GlStateManager.FogMode.EXP);
            GlStateManager.setFogDensity(0.02F);

            if (RenderEventHandler.fogTarget > 0) {
                RenderEventHandler.fogTarget = 0;
                RenderEventHandler.fogDuration = 0;
            }
        }
    }

    public void tickEventClientTickEvent(TickEvent.ClientTickEvent e) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        WorldClient world = Minecraft.getMinecraft().world;
        Minecraft mc = Minecraft.getMinecraft();

        if (e.phase != TickEvent.Phase.START || player == null || e.side == Side.SERVER || mc.isGamePaused()) {
            return;
        }

        if (player.ticksExisted % 4 == 0) {
            byte lastId = currentBiomeId;
            currentBiomeId = MiscHandler.getBiomeIdOnPos(player.world, ultraBlockPos.setPos(player));
            prevBiome = currentBiome;
            currentBiome = Biome.getBiome(currentBiomeId);
            isPrevBiomeTaint = isCurrentBiomeTaint;
            isCurrentBiomeTaint = MiscHandler.arrayContains(InitBiome.TAINT_BIOMES_L_ID, currentBiomeId);
            if (lastId != currentBiomeId) {
                onBiomeChanged(lastId, currentBiomeId);
            }
        }
        if (isCurrentBiomeTaint) {
            if (playerInTaintBiomeTime < maxPlayerInTaintBiomeTime) {
                playerInTaintBiomeTime++;
            }
        } else {
            if (!isPlayerInTaintBiome()) {
                defaultSun = world.getSunBrightnessBody(1);
            }
            if (playerInTaintBiomeTime > 0) {
                playerInTaintBiomeTime--;
            }
        }

        if (player.onGround && this.runOnGround != null) {
            this.runOnGround.run(); this.runOnGround = null;
        }
    }

    public void onBiomeChanged(byte oldBiome, byte newBiome) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        WorldClient world = Minecraft.getMinecraft().world;
        FogManager.FogParams params = fogParamsMap.get(this.currentBiome);
        if (this.currentBiome == InitBiome.TAINT_EDGE) {
            params = new TaintEdgeFog();
        }
        if (params == null && this.isCurrentBiomeTaint) {
            params = this.taintFogParams;
        }
        if (this.prevBiome == InitBiome.TAINT_EDGE && this.isCurrentBiomeTaint && player.posY - ultraBlockPos.setPos(player).setWorldY(world).getY() > 6) {
            FogManager.FogParams finalParams = params;
            this.runOnGround = () -> ClientEventsHandler.FOG_MANAGER.setFogParams(finalParams);
            return;
        }
        ClientEventsHandler.FOG_MANAGER.setFogParams(params);
        this.runOnGround = null;
    }

    public float getSunBrightnessInTaintBiome() {
        return defaultSun - (defaultSun * ((float) playerInTaintBiomeTime / maxPlayerInTaintBiomeTime));
    }

    public boolean isPlayerInTaintBiome() {
        return playerInTaintBiomeTime != 0;
    }

    public static class TaintEdgeFog extends FogManager.FogParams implements FogManager.IDynamicFog {

        @Override
        public int update(EntityPlayer player, FogManager.FogParams fog, boolean isNew, int fogTime) {
            if (isNew && player.posY > 64) {
                int v = (int) MiscHandler.interpolate(10.0F, 200.0F, (float) MathHelper.clamp((player.posY - 80) / 48.0F, 0.0F, 1.0F));
                if (this.fogDuration < v) {
                    this.fogDuration = MathHelper.clamp(v, 10, 200);
                }
            }
            return fogTime;
        }

        @Override
        public void update(EntityViewRenderEvent.FogColors e, FogManager.FogParams fog, boolean isNew) {
            if (isNew) {
                float k = 1.0F - (MathHelper.clamp(((this.fogDuration - 100) / 100.0F), 0.0F, 1.0F) * 0.4F);
                this.r = e.getRed() * k;
                this.g = e.getGreen() * k;
                this.b = e.getBlue() * k;
            }
        }

        @Override
        public FogManager.FogParams copy(FogManager.MixedFog fog, int fogDuration) {
            TaintEdgeFog fog1 = new TaintEdgeFog();
            fog1.fogDuration = this.fogDuration;
            fog1.r = this.r;
            fog1.g = this.g;
            fog1.b = this.b;
            return fog1;
        }
    }

    public static class TaintFog extends FogManager.FogParams {

        public TaintFog(float r, float g, float b, int fogDuration) {
            super(r, g, b, fogDuration);
        }

        public TaintFog(FogManager.MixedFog fog, int fogDuration) {
            super(fog, fogDuration);
        }

        @Override
        public int fogChangeTime(FogManager.FogParams oldFog) {
            if (oldFog instanceof TaintEdgeFog) {
                return 20;
            }
            return 80;
        }

        @Override
        public FogManager.FogParams copy(FogManager.MixedFog fog, int fogDuration) {
            return new TaintFog(fog, fogDuration);
        }
    }
}
