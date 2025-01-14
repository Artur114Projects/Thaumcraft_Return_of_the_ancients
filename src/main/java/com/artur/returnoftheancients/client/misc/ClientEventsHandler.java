package com.artur.returnoftheancients.client.misc;

import com.artur.returnoftheancients.handlers.HandlerR;
import com.artur.returnoftheancients.init.InitBiome;
import com.artur.returnoftheancients.items.ItemGavno;
import com.artur.returnoftheancients.referense.Referense;
import com.artur.returnoftheancients.utils.math.UltraMutableBlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
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
    protected static final UltraMutableBlockPos ultraBlockPos = new UltraMutableBlockPos();
    protected static final String startUpNBT = "startUpNBT";
    public static float defaultSun = 0.0F;
    public static int playerInTaintBiomeTime = 0;
    public static int maxPlayerInTaintBiomeTime = 160;


    // TODO: Сделать что нибудь с туманом (Починить)
    @SubscribeEvent
    public static void fogSetColor(EntityViewRenderEvent.FogColors e) {
        if (e.getEntity().getEntityWorld().provider.getDimension() == ancient_world_dim_id) {
            e.setRed(0);
            e.setBlue(0);
            e.setGreen(0);
            return;
        }
        if (isPlayerInTaintBiome()) {
            float scaleBase = (float) playerInTaintBiomeTime / maxPlayerInTaintBiomeTime;
            float r = e.getRed() * 255.0F;
            float g = e.getGreen() * 255.0F;
            float b = e.getBlue() * 255.0F;

            e.setRed((r + (43.0F /  4.0f - r) * scaleBase) / 255.0F);
            e.setGreen((g + (0 - g) * scaleBase) / 255.0F);
            e.setBlue((b + (61.0F / 4.0f - b) * scaleBase) / 255.0F);

            RenderEventHandler.fogFiddled = true;
            if (RenderEventHandler.fogDuration < 40) {
                RenderEventHandler.fogDuration++;
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void fogDensityEvent(EntityViewRenderEvent.RenderFogEvent event) {
        if (RenderEventHandler.fogDuration < 2 && RenderEventHandler.fogFiddled) {
            GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
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
            if (e.player.posY > 84 && !e.player.isCreative()) {
                e.player.fallDistance = 0;
                e.player.motionY += -1 - e.player.motionY;
            }
        }
        ultraBlockPos.setPos(e.player);
        if (HandlerR.arrayContainsAny(InitBiome.TAINT_BIOMES_L_ID, e.player.world.getChunkFromBlockCoords(ultraBlockPos).getBiomeArray()[(ultraBlockPos.getX() & 15) + (ultraBlockPos.getZ() & 15) * 16])) {
            if (playerInTaintBiomeTime < maxPlayerInTaintBiomeTime) {
                playerInTaintBiomeTime++;
            }
        } else {
            if (!isPlayerInTaintBiome()) {
                defaultSun = e.player.world.getSunBrightnessBody(1);
            }
            if (playerInTaintBiomeTime > 0) {
                playerInTaintBiomeTime--;
            }
        }
        CameraShake.updateShake();
    }

    public static float getSunBrightnessInTaintBiome() {
        return defaultSun - (defaultSun * ((float) playerInTaintBiomeTime / maxPlayerInTaintBiomeTime));
    }

    public static boolean isPlayerInTaintBiome() {
        return playerInTaintBiomeTime != 0;
    }
}
