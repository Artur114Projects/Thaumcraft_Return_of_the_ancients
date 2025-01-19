package com.artur.returnoftheancients.client.misc;

import com.artur.returnoftheancients.client.misc.eventmanagers.ClientPlayerInBiomeManager;
import com.artur.returnoftheancients.client.misc.eventmanagers.FogManager;
import com.artur.returnoftheancients.client.misc.eventmanagers.PlayerMovementManager;
import com.artur.returnoftheancients.client.misc.eventmanagers.ThaumcraftFogFixer;
import com.artur.returnoftheancients.referense.Referense;
import com.artur.returnoftheancients.utils.math.UltraMutableBlockPos;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.artur.returnoftheancients.init.InitDimensions.ancient_world_dim_id;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = Referense.MODID)
public class ClientEventsHandler {

    public static final ClientPlayerInBiomeManager PLAYER_IN_BIOME_MANAGER = new ClientPlayerInBiomeManager();
    public static final PlayerMovementManager PLAYER_MOVEMENT_MANAGER = new PlayerMovementManager();
    public static final ThaumcraftFogFixer THAUMCRAFT_FOG_FIXER = new ThaumcraftFogFixer();
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
    public static void PlayerTickEvent(TickEvent.PlayerTickEvent e) {

        PLAYER_IN_BIOME_MANAGER.tickEventPlayerTickEvent(e);
        PLAYER_MOVEMENT_MANAGER.tickEventPlayerTickEvent(e);
        FOG_MANAGER.tickEventPlayerTickEvent(e);

        CameraShake.updateShake();
    }
}
