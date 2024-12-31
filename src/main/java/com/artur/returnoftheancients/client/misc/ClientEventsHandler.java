package com.artur.returnoftheancients.client.misc;

import com.artur.returnoftheancients.init.InitBiome;
import com.artur.returnoftheancients.items.ItemGavno;
import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.events.RenderEventHandler;

import java.lang.ref.Reference;

import static com.artur.returnoftheancients.init.InitDimensions.ancient_world_dim_id;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = Referense.MODID)
public class ClientEventsHandler {
    protected static final String startUpNBT = "startUpNBT";
    private static byte cpt = 0;

    // TODO: Сделать что нибуть с туманом (Починить)
    // TODO: Сделать плавный переход со светом и цветом тумана
    @SubscribeEvent
    public static void fogSetColor(EntityViewRenderEvent.FogColors e) {
        if (e.getEntity().getEntityWorld().provider.getDimension() == ancient_world_dim_id) {
            e.setRed(0);
            e.setBlue(0);
            e.setGreen(0);
            return;
        }
        if (e.getEntity().getEntityWorld().getBiome(e.getEntity().getPosition()).equals(InitBiome.TAINT)) {
            e.setRed((43.0F / 4.0f) / 255.0F);
            e.setGreen(0.0F / 255.0F);
            e.setBlue((61.0F / 4.0f) / 255.0F);

            RenderEventHandler.fogFiddled = true;
            if (RenderEventHandler.fogDuration < 40) {
                RenderEventHandler.fogDuration += 2;
            }
        }
    }

    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Pre event) {
        EntityPlayer player = event.getEntityPlayer();
        ItemStack heldItem = player.getHeldItemMainhand();

        if (!heldItem.isEmpty() && heldItem.getItem() instanceof ItemGavno) {
            player.swingProgress = 0.0F;
            player.prevSwingProgress = 0.0F;

            player.limbSwingAmount = 0.0F;
        }
    }

    @SubscribeEvent
    public static void renderView(EntityViewRenderEvent.CameraSetup e) {


    }

//    @SubscribeEvent
//    public static void renderSpecificHandEvent(RenderSpecificHandEvent e) {
//        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(Referense.MODID, "aaaaaaaa"));
//    }


//    @SubscribeEvent
//    public static void fogRender(EntityViewRenderEvent.RenderFogEvent e) {
//        if (e.getEntity().dimension == ancient_world_dim_id) {
//            GlStateManager.color(0, 0, 0, 1);
//        }
//    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void fogDensityEvent(EntityViewRenderEvent.RenderFogEvent event) {
        if (RenderEventHandler.fogDuration < 2 && RenderEventHandler.fogFiddled) {
            GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
            GL11.glFogf(GL11.GL_FOG_DENSITY, 0.1F);
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
