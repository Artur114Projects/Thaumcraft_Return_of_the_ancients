package com.artur.returnoftheancients.client.event.managers;

import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.artur.returnoftheancients.init.InitDimensions.ancient_world_dim_id;

@SideOnly(Side.CLIENT)
public class PlayerMovementManager {

    public void tickEventPlayerTickEvent(TickEvent.PlayerTickEvent e) {
        if (e.phase != TickEvent.Phase.START || e.side == Side.SERVER) {
            return;
        }
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
