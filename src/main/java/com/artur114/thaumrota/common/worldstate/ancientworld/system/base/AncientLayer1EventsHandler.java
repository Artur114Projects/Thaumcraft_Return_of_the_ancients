package com.artur114.thaumrota.common.worldstate.ancientworld.system.base;

import com.artur114.thaumrota.common.worldstate.ancientworld.system.client.ClientAncientLayer1EM;
import com.artur114.thaumrota.common.worldstate.ancientworld.system.server.ServerAncientLayer1EM;
import com.artur114.thaumrota.common.init.InitDimensions;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import thaumcraft.api.blocks.BlocksTC;

import static com.artur114.thaumrota.common.init.InitDimensions.ANCIENT_WORLD_ID;

@Mod.EventBusSubscriber
public class AncientLayer1EventsHandler {
    public static final ClientAncientLayer1EM CLIENT_MANAGER = new ClientAncientLayer1EM();
    public static final ServerAncientLayer1EM SERVER_MANAGER = new ServerAncientLayer1EM();

    @SubscribeEvent
    public static void attachCapabilitiesWorld(AttachCapabilitiesEvent<World> e) {
        if (e.getObject() != null &&  e.getObject().isRemote && e.getObject().provider.getDimension() == InitDimensions.ANCIENT_WORLD_ID) CLIENT_MANAGER.attachCapabilitiesEventWorld(e);
        if (e.getObject() != null && !e.getObject().isRemote && e.getObject().provider.getDimension() == InitDimensions.ANCIENT_WORLD_ID) SERVER_MANAGER.attachCapabilitiesEventWorld(e);
    }

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent e) {
        World world = Minecraft.getMinecraft().world; if (world != null && world.provider.getDimension() == InitDimensions.ANCIENT_WORLD_ID) CLIENT_MANAGER.tickEventClientTickEvent(e);
    }

    @SubscribeEvent
    public static void worldTick(TickEvent.WorldTickEvent e) {
        if (!e.world.isRemote && e.world.provider.getDimension() == InitDimensions.ANCIENT_WORLD_ID) SERVER_MANAGER.tickEventWorldTickEvent(e);
    }

    @SubscribeEvent
    public static void playerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent e) {
        if (e.player.dimension == InitDimensions.ANCIENT_WORLD_ID) SERVER_MANAGER.playerEventPlayerLoggedInEvent(e);
    }

    @SubscribeEvent
    public static void playerLoggedOutEvent(PlayerEvent.PlayerLoggedOutEvent e) {
        if (e.player.dimension == InitDimensions.ANCIENT_WORLD_ID) SERVER_MANAGER.playerEventPlayerLoggedOutEvent(e);
    }

    @SubscribeEvent
    public static void chunkUnloadEvent(ChunkEvent.Unload e) {
        if (!e.getWorld().isRemote && e.getWorld().provider.getDimension() == InitDimensions.ANCIENT_WORLD_ID) SERVER_MANAGER.chunkEventUnload(e);
    }

    @SubscribeEvent
    public static void entityJoinToWorld(EntityJoinWorldEvent e) {
        if (e.getEntity().dimension == InitDimensions.ANCIENT_WORLD_ID && !e.getWorld().isRemote) e.setCanceled(!SERVER_MANAGER.entityJoinWorldEvent(e));
    }

    @SubscribeEvent
    public static void livingDeathEvent(LivingDeathEvent e) {
        if (e.getEntity().dimension == InitDimensions.ANCIENT_WORLD_ID && !e.getEntity().world.isRemote) SERVER_MANAGER.livingDeathEvent(e);
    }

    @SubscribeEvent
    public static void playerInteractRight(PlayerInteractEvent.RightClickBlock e) {
        if (e.getSide() == Side.CLIENT && e.getEntityPlayer() != null && e.getEntityPlayer().dimension == InitDimensions.ANCIENT_WORLD_ID) CLIENT_MANAGER.playerInteractEventRightClickBlock(e);
        if (e.getSide() == Side.SERVER && e.getEntityPlayer() != null && e.getEntityPlayer().dimension == InitDimensions.ANCIENT_WORLD_ID) SERVER_MANAGER.playerInteractEventRightClickBlock(e);
    }

    @SubscribeEvent
    public static void playerInteractLeft(PlayerInteractEvent.LeftClickBlock e) {
        if (e.getSide() == Side.CLIENT && e.getEntityPlayer() != null && e.getEntityPlayer().dimension == InitDimensions.ANCIENT_WORLD_ID) CLIENT_MANAGER.playerInteractEventLeftClickBlock(e);
        if (e.getSide() == Side.SERVER && e.getEntityPlayer() != null && e.getEntityPlayer().dimension == InitDimensions.ANCIENT_WORLD_ID) SERVER_MANAGER.playerInteractEventLeftClickBlock(e);
    }

    @SubscribeEvent
    public static void breakEvent(BlockEvent.BreakEvent e) {
        if (e.getPlayer() != null &&  e.getPlayer().world.isRemote && e.getPlayer().dimension == InitDimensions.ANCIENT_WORLD_ID) CLIENT_MANAGER.blockEventBreakEvent(e);
        if (e.getPlayer() != null && !e.getPlayer().world.isRemote && e.getPlayer().dimension == InitDimensions.ANCIENT_WORLD_ID) SERVER_MANAGER.blockEventBreakEvent(e);
    }

    @SubscribeEvent
    public static void livingDropsEvent(LivingDropsEvent e) {
        if (e.getEntityLiving() != null &&  e.getEntityLiving().world.isRemote && e.getEntityLiving().dimension == InitDimensions.ANCIENT_WORLD_ID) CLIENT_MANAGER.livingDropsEvent(e);
        if (e.getEntityLiving() != null && !e.getEntityLiving().world.isRemote && e.getEntityLiving().dimension == InitDimensions.ANCIENT_WORLD_ID) SERVER_MANAGER.livingDropsEvent(e);
    }

    @SubscribeEvent
    public static void livingDamageEvent(LivingDamageEvent e) {
        if (e.getEntityLiving() != null &&  e.getEntityLiving().world.isRemote && e.getEntityLiving().dimension == InitDimensions.ANCIENT_WORLD_ID) CLIENT_MANAGER.livingDamageEvent(e);
        if (e.getEntityLiving() != null && !e.getEntityLiving().world.isRemote && e.getEntityLiving().dimension == InitDimensions.ANCIENT_WORLD_ID) SERVER_MANAGER.livingDamageEvent(e);
    }

    public static void canDeSpawn(LivingSpawnEvent.AllowDespawn e) {
        if (e.getEntityLiving() != null && !e.getEntityLiving().world.isRemote && e.getEntityLiving().dimension == InitDimensions.ANCIENT_WORLD_ID) SERVER_MANAGER.livingSpawnEventAllowDespawn(e);
    }
}
