package com.artur.returnoftheancients.handlers;

import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.client.lib.events.RenderEventHandler;

import static com.artur.returnoftheancients.init.InitDimensions.ancient_world_dim_id;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = Referense.MODID)
public class ClientEventsHandler extends ServerEventsHandler {

    private static byte cpt = 0;

    @SubscribeEvent
    public static void fogSetColor(EntityViewRenderEvent.FogColors e) {
        if (e.getEntity().getEntityWorld().provider.getDimension() == ancient_world_dim_id) {
            e.setRed(0);
            e.setBlue(0);
            e.setGreen(0);
        }
    }

    @SubscribeEvent
    public static void PlayerTickEvent(TickEvent.PlayerTickEvent e) {
        int playerDimension = e.player.dimension;
        if (e.player.getEntityData().getBoolean(startUpNBT)) {
            if (playerDimension != ancient_world_dim_id) {
                if (e.player instanceof EntityPlayerSP) {
                    EntityPlayerSP playerSP = (EntityPlayerSP) e.player;
                    playerSP.motionY += 2 - playerSP.motionY;
                }
            }
        }
        if (e.player.dimension == ancient_world_dim_id) {
            if (cpt >= 4) {
                cpt = 0;
//                if (e.player instanceof EntityPlayerSP && !e.player.isCreative()) {
//                    RenderEventHandler.fogFiddled = true;
//                    if (RenderEventHandler.fogDuration < 200) {
//                        RenderEventHandler.fogDuration = 200;
//                    }
//                }
            }
            cpt++;
            if (e.player.posY > 84 && !e.player.isCreative()) {
                e.player.fallDistance = 0;
                e.player.motionY += -1 - e.player.motionY;
            }
        }
    }
}
