package com.artur.returnoftheancients.ancientworld.system.base;

import com.artur.returnoftheancients.ancientworld.system.client.ClientAncientLayer1EM;
import com.artur.returnoftheancients.ancientworld.system.server.ServerAncientLayer1EM;
import com.artur.returnoftheancients.init.InitDimensions;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber
public class AncientLayer1EventsHandler {
    public static final ClientAncientLayer1EM CLIENT_MANAGER = new ClientAncientLayer1EM();
    public static final ServerAncientLayer1EM SERVER_MANAGER = new ServerAncientLayer1EM();

    @SubscribeEvent
    public static void attachCapabilitiesWorld(AttachCapabilitiesEvent<World> e) {
        if (e.getObject() != null &&  e.getObject().isRemote && e.getObject().provider.getDimension() == InitDimensions.ancient_world_dim_id) CLIENT_MANAGER.attachCapabilitiesEventWorld(e);
        if (e.getObject() != null && !e.getObject().isRemote && e.getObject().provider.getDimension() == InitDimensions.ancient_world_dim_id) SERVER_MANAGER.attachCapabilitiesEventWorld(e);
    }

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent e) {
        World world = Minecraft.getMinecraft().world; if (world != null && world.provider.getDimension() == InitDimensions.ancient_world_dim_id) CLIENT_MANAGER.tickEventClientTickEvent(e);
    }

    @SubscribeEvent
    public static void worldTick(TickEvent.WorldTickEvent e) {
        if (!e.world.isRemote && e.world.provider.getDimension() == InitDimensions.ancient_world_dim_id) SERVER_MANAGER.tickEventWorldTickEvent(e);
    }

    @SubscribeEvent
    public static void playerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent e) {
        if (e.player.dimension == InitDimensions.ancient_world_dim_id) SERVER_MANAGER.playerEventPlayerLoggedInEvent(e);
    }

    @SubscribeEvent
    public static void playerLoggedOutEvent(PlayerEvent.PlayerLoggedOutEvent e) {
        if (e.player.dimension == InitDimensions.ancient_world_dim_id) SERVER_MANAGER.playerEventPlayerLoggedOutEvent(e);
    }
}
