package com.artur.returnoftheancients.client;

import com.artur.returnoftheancients.handlers.ServerEventsHandler;
import com.artur.returnoftheancients.init.InitBiome;
import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.events.RenderEventHandler;

import static com.artur.returnoftheancients.init.InitDimensions.ancient_world_dim_id;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = Referense.MODID)
public class ClientEventsHandler {
    protected static final String startUpNBT = "startUpNBT";
    private static byte cpt = 0;

    // TODO: Сделать что нибуть с туманом (Починить)
    @SubscribeEvent
    public static void fogSetColor(EntityViewRenderEvent.FogColors e) {
        if (e.getEntity().getEntityWorld().provider.getDimension() == ancient_world_dim_id) {
            e.setRed(0);
            e.setBlue(0);
            e.setGreen(0);
            return;
        }
        if (e.getEntity().getEntityWorld().getBiome(e.getEntity().getPosition()).equals(InitBiome.TAINT)) {
            e.setRed(43.0F / 255.0F);
            e.setGreen(0.0F / 255.0F);
            e.setBlue(61.0F / 255.0F);

            RenderEventHandler.fogFiddled = true;
            if (RenderEventHandler.fogDuration < 40) {
                RenderEventHandler.fogDuration += 2;
            }
            return;
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void fogDensityEvent(EntityViewRenderEvent.RenderFogEvent event) {
        if (RenderEventHandler.fogDuration < 2 && RenderEventHandler.fogFiddled) {
            GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
            GL11.glFogf(GL11.GL_FOG_DENSITY, 0.04F);
            RenderEventHandler.fogFiddled = false;
        }
    }


    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void PlayerTickEvent(TickEvent.PlayerTickEvent e) {
        int playerDimension = e.player.dimension;
        if (e.player.getEntityData().getBoolean(startUpNBT)) {
            if (playerDimension != ancient_world_dim_id) {
                e.player.motionY += 2 - e.player.motionY;
            }
        }
        if (e.player.dimension == ancient_world_dim_id) {
            if (cpt >= 4) {
                cpt = 0;
            }
            cpt++;
            if (e.player.posY > 84 && !e.player.isCreative()) {
                e.player.fallDistance = 0;
                e.player.motionY += -1 - e.player.motionY;
            }
        }
        CameraShake.updateShake();
    }
}
