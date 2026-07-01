package com.artur114.thaumrota.client.event;

import com.artur114.thaumrota.client.event.managers.*;
import com.artur114.thaumrota.client.event.managers.movement.PlayerMovementManager;
import com.artur114.thaumrota.main.ThaumRotA;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = ThaumRotA.MODID)
public class ClientEventsHandler {
    public static final PlayerDistanceToPortalManager PLAYER_DISTANCE_TO_PORTAL_MANAGER = new PlayerDistanceToPortalManager();
    public static final ClientPlayerInBiomeManager PLAYER_IN_BIOME_MANAGER = new ClientPlayerInBiomeManager();
    public static final PlayerMovementManager PLAYER_MOVEMENT_MANAGER = new PlayerMovementManager();
    public static final CustomRainManager CUSTOM_RAIN_MANAGER = new CustomRainManager();
    public static final GlobalTickManager GLOBAL_TICK_MANAGER = new GlobalTickManager();
    public static final AreasDrawManager AREAS_DRAW_MANAGER = new AreasDrawManager();
    public static final CameraFXManager CAMERA_FX_MANAGER = new CameraFXManager();
    public static final SoundsManager SOUNDS_MANAGER = new SoundsManager();
    public static final FogManager FOG_MANAGER = new FogManager();

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void fogSetColor(EntityViewRenderEvent.FogColors e) {
        PLAYER_IN_BIOME_MANAGER.entityViewRenderEventFogColors(e);
        FOG_MANAGER.entityViewRenderEventFogColors(e);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void fogRender(EntityViewRenderEvent.RenderFogEvent e) {
        PLAYER_IN_BIOME_MANAGER.entityViewRenderEventRenderFogEvent(e);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void clientTick(TickEvent.ClientTickEvent e) {
        PLAYER_DISTANCE_TO_PORTAL_MANAGER.tickEventClientTickEvent(e);
        PLAYER_MOVEMENT_MANAGER.tickEventClientTickEvent(e);
        PLAYER_IN_BIOME_MANAGER.tickEventClientTickEvent(e);
        CUSTOM_RAIN_MANAGER.tickEventClientTickEvent(e);
        GLOBAL_TICK_MANAGER.tickEventClientTickEvent(e);
        AREAS_DRAW_MANAGER.tickEventClientTickEvent(e);
        CAMERA_FX_MANAGER.tickEventClientTickEvent(e);
        SOUNDS_MANAGER.tickEventClientTickEvent(e);
        FOG_MANAGER.tickEventClientTickEvent(e);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void PlayerTickEvent(TickEvent.PlayerTickEvent e) {
        PLAYER_MOVEMENT_MANAGER.tickEventPlayerTickEvent(e);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void cameraSetup(EntityViewRenderEvent.CameraSetup e) {
        CAMERA_FX_MANAGER.entityViewRenderEventCameraSetup(e);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void renderWorldLastEvent(RenderWorldLastEvent e) {
        AREAS_DRAW_MANAGER.renderWorldLastEvent(e);
    }
}
