package com.artur.returnoftheancients.ancientworld.system.base;

import com.artur.returnoftheancients.ancientworld.system.client.ClientAncientLayer1EM;
import com.artur.returnoftheancients.ancientworld.system.server.ServerAncientLayer1EM;
import com.artur.returnoftheancients.init.InitDimensions;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
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

    @SubscribeEvent
    public static void chunkUnloadEvent(ChunkEvent.Unload e) {
        if (!e.getWorld().isRemote && e.getWorld().provider.getDimension() == InitDimensions.ancient_world_dim_id) SERVER_MANAGER.chunkEventUnload(e);
    }

    @SubscribeEvent
    public static void entityJoinToWorld(EntityJoinWorldEvent e) {
        if (e.getEntity().dimension == InitDimensions.ancient_world_dim_id && !e.getWorld().isRemote) e.setCanceled(!SERVER_MANAGER.entityJoinWorldEvent(e));
    }

    @SubscribeEvent
    public static void livingDeathEvent(LivingDeathEvent e) {
        if (e.getEntity().dimension == InitDimensions.ancient_world_dim_id && !e.getEntity().world.isRemote) SERVER_MANAGER.livingDeathEvent(e);
    }
}
