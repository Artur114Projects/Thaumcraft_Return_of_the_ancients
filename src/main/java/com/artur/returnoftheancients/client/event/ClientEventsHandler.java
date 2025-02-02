package com.artur.returnoftheancients.client.event;

import com.artur.returnoftheancients.client.event.managers.movement.PlayerMovementManager;
import com.artur.returnoftheancients.client.fx.misc.CameraShake;
import com.artur.returnoftheancients.client.event.managers.*;
import com.artur.returnoftheancients.referense.Referense;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = Referense.MODID)
public class ClientEventsHandler {

    public static final PlayerDistanceToPortalManager PLAYER_DISTANCE_TO_PORTAL_MANAGER = new PlayerDistanceToPortalManager();
    public static final ClientPlayerInBiomeManager PLAYER_IN_BIOME_MANAGER = new ClientPlayerInBiomeManager();
    public static final PlayerMovementManager PLAYER_MOVEMENT_MANAGER = new PlayerMovementManager();
    public static final ThaumcraftFogFixer THAUMCRAFT_FOG_FIXER = new ThaumcraftFogFixer();
    public static final CustomRainManager CUSTOM_RAIN_MANAGER = new CustomRainManager();
    public static final FogManager FOG_MANAGER = new FogManager();


    @SubscribeEvent
    public static void fogSetColor(EntityViewRenderEvent.FogColors e) {
        PLAYER_IN_BIOME_MANAGER.entityViewRenderEventFogColors(e);
        FOG_MANAGER.entityViewRenderEventFogColors(e);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void fogDensityEvent(EntityViewRenderEvent.RenderFogEvent e) {
        THAUMCRAFT_FOG_FIXER.entityViewRenderEventRenderFogEvent(e);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void clientTick(TickEvent.ClientTickEvent e) {
        PLAYER_DISTANCE_TO_PORTAL_MANAGER.tickEventClientTickEvent(e);
        PLAYER_MOVEMENT_MANAGER.tickEventClientTickEvent(e);
        PLAYER_IN_BIOME_MANAGER.tickEventClientTickEvent(e);
        CUSTOM_RAIN_MANAGER.tickEventClientTickEvent(e);
        FOG_MANAGER.tickEventClientTickEvent(e);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void PlayerTickEvent(TickEvent.PlayerTickEvent e) {
        PLAYER_MOVEMENT_MANAGER.tickEventPlayerTickEvent(e);
        CameraShake.updateShake();
    }
}
