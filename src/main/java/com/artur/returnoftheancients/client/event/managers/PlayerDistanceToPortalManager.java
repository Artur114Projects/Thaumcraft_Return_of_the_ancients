package com.artur.returnoftheancients.client.event.managers;

import com.artur.returnoftheancients.generation.portal.base.AncientPortalsProcessor;
import com.artur.returnoftheancients.main.MainR;
import com.artur.returnoftheancients.network.ServerPacketGetWeather;
import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

public class PlayerDistanceToPortalManager {
    public ChunkPos nearestPortalPos = new ChunkPos(0, 0);
    public float worldPrevPrevRainingStrength = 0;
    public boolean isServerRainUpdated = false;
    public float worldPrevRainingStrength = 0;
    public float serverThunderStrength = 0;
    public boolean useCustomRain = false;
    public float serverRainStrength = 0;
    public float prevRainStrength = 0;
    public int distanceToPortal = 0;
    public float rainStrength = 0;

    public void tickEventClientTickEvent(TickEvent.ClientTickEvent e) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        Minecraft mc = Minecraft.getMinecraft();

        if (e.phase != TickEvent.Phase.START || player == null || e.side == Side.SERVER || mc.isGamePaused()) {
            return;
        }

        calculateDistanceToPortal(mc, player);
        updateServerRainTck(mc, player);
        setRain(mc, player);
    }

    private void calculateDistanceToPortal(Minecraft mc, EntityPlayer player) {
        if (player.ticksExisted % 8 == 0 && AncientPortalsProcessor.hasPortalOnWorld(mc.world)) {
            UltraMutableBlockPos playerPos = UltraMutableBlockPos.getBlockPosFromPoll().setPos(player);
            UltraMutableBlockPos portalPos = UltraMutableBlockPos.getBlockPosFromPoll();

            this.nearestPortalPos = AncientPortalsProcessor.getNearestPortalPos(mc.world, playerPos);
            portalPos.setPos(this.nearestPortalPos).add(8, 0, 8);

            playerPos.setY(0);
            this.distanceToPortal = playerPos.distance(portalPos);

            UltraMutableBlockPos.returnBlockPosToPoll(playerPos);
            UltraMutableBlockPos.returnBlockPosToPoll(portalPos);
        }
    }

    private void setRain(Minecraft mc, EntityPlayer player) { // TODO: 23.04.2025 Переделать!
        if (distanceToPortal < 1024 && player.dimension == 0) {
            if (!useCustomRain) {
                isServerRainUpdated = false;
                updateServerRain();
            }
            useCustomRain = true;
            if (!isServerRainUpdated) {
                return;
            }
            if (rainStrength == 0) {
                rainStrength += serverRainStrength;
            }
            if (rainStrength < 1.0F) {
                rainStrength += 0.01F;
            }
        } else {
            if (useCustomRain) {
                if (serverRainStrength != 0) {
                    mc.world.setRainStrength(serverRainStrength);
                    mc.world.setThunderStrength(serverThunderStrength);
                    useCustomRain = false;
                    rainStrength = 0;
                } else {
                    if (rainStrength > 0.0F) {
                        rainStrength -= 0.01F;
                    } else {
                        useCustomRain = false;
                        rainStrength = 0;
                    }
                }
            }
        }

        if (useCustomRain) {
            if (prevRainStrength == mc.world.prevRainingStrength) {
                mc.world.setRainStrength(rainStrength);
                mc.world.setThunderStrength(rainStrength);
            } else if (mc.world.rainingStrength == mc.world.prevRainingStrength && mc.world.prevRainingStrength == worldPrevRainingStrength && worldPrevRainingStrength == worldPrevPrevRainingStrength) {
                mc.world.setRainStrength(rainStrength);
                mc.world.setThunderStrength(rainStrength);
            }
        }

        prevRainStrength = rainStrength;
        worldPrevPrevRainingStrength = worldPrevRainingStrength;
        worldPrevRainingStrength = mc.world.prevRainingStrength;
    }

    private void updateServerRainTck(Minecraft mc, EntityPlayer player) {
        if (useCustomRain && player.ticksExisted % 40 == 0) {
            updateServerRain();
        }
    }

    private void updateServerRain() {
        MainR.NETWORK.sendToServer(new ServerPacketGetWeather());
    }

    public void setServerRain(float rain, float thunder) {
        this.serverThunderStrength = thunder;
        this.serverRainStrength = rain;
        this.isServerRainUpdated = true;
    }
}
