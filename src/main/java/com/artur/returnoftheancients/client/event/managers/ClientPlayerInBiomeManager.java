package com.artur.returnoftheancients.client.event.managers;

import com.artur.returnoftheancients.client.event.ClientEventsHandler;
import com.artur.returnoftheancients.handlers.HandlerR;
import com.artur.returnoftheancients.init.InitBiome;
import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

import static com.artur.returnoftheancients.init.InitDimensions.ancient_world_dim_id;

@SideOnly(Side.CLIENT)
public class ClientPlayerInBiomeManager {
    private final FogManager.FogParams taintFogParams = new FogManager.FogParams(43.0F / 4.0f, 0, 61.0F / 4.0f, 20);
    public final UltraMutableBlockPos ultraBlockPos = new UltraMutableBlockPos();
    public Map<Biome, FogManager.FogParams> fogParamsMap = new HashMap<>();
    public boolean isCurrentBiomeTaint = false;
    public int maxPlayerInTaintBiomeTime = 80;
    public boolean isPrevBiomeTaint = false;
    public int playerInTaintBiomeTime = 0;
    public Biome currentBiome = null;
    public byte currentBiomeId = -1;
    public float defaultSun = 0.0F;


    public ClientPlayerInBiomeManager() {
        fogParamsMap.put(InitBiome.TAINT_SEA, new FogManager.FogParams(70.0F / 7.0F, 90.0F / 8.0F, 100.0F / 7.0F, 40));
        fogParamsMap.put(InitBiome.TAINT_MOUNTAINS, new FogManager.FogParams(43.0F / 5.0f, 0, 61.0F / 5.0f, 30));
        fogParamsMap.put(InitBiome.TAINT_WASTELAND, new FogManager.FogParams(6, 2, 2, 30));
    }

    public void entityViewRenderEventFogColors(EntityViewRenderEvent.FogColors e) {
        if (e.getEntity().getEntityWorld().provider.getDimension() == ancient_world_dim_id) {
            e.setRed(0);
            e.setBlue(0);
            e.setGreen(0);
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
            currentBiomeId = HandlerR.getBiomeIdOnPos(player.world, ultraBlockPos.setPos(player));
            currentBiome = Biome.getBiome(currentBiomeId);
            isPrevBiomeTaint = isCurrentBiomeTaint;
            isCurrentBiomeTaint = HandlerR.arrayContains(InitBiome.TAINT_BIOMES_L_ID, currentBiomeId);
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
    }

    public void onBiomeChanged(byte oldBiome, byte newBiome) {
        WorldClient world = Minecraft.getMinecraft().world;
        FogManager.FogParams params = fogParamsMap.get(currentBiome);
        if (params == null && isCurrentBiomeTaint) {
            params = taintFogParams;
        }
        ClientEventsHandler.FOG_MANAGER.setFogParams(params);
    }

    public float getSunBrightnessInTaintBiome() {
        return defaultSun - (defaultSun * ((float) playerInTaintBiomeTime / maxPlayerInTaintBiomeTime));
    }

    public boolean isPlayerInTaintBiome() {
        return playerInTaintBiomeTime != 0;
    }
}
