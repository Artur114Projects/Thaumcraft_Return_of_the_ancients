package com.artur.returnoftheancients.client.misc.eventmanagers;

import net.minecraftforge.fml.common.gameevent.TickEvent;

import static com.artur.returnoftheancients.init.InitDimensions.ancient_world_dim_id;

public class PlayerMovementManager {

    public void tickEventPlayerTickEvent(TickEvent.PlayerTickEvent e) {
        int playerDimension = e.player.dimension;
        if (e.player.getEntityData().getBoolean("startUpNBT")) {
            if (playerDimension != ancient_world_dim_id) {
                e.player.motionY += 2 - e.player.motionY;
            }
        }
        if (e.player.dimension == ancient_world_dim_id) {
            if (e.player.posY > 84 && !e.player.isCreative()) {
                e.player.fallDistance = 0;
                e.player.motionY += -1 - e.player.motionY;
            }
        }
    }
}
