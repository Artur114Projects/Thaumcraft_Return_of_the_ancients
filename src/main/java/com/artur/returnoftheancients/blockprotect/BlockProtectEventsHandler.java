package com.artur.returnoftheancients.blockprotect;

import com.artur.returnoftheancients.blockprotect.client.ClientBlockProtectManager;
import com.artur.returnoftheancients.blockprotect.server.IServerProtectedChunk;
import com.artur.returnoftheancients.blockprotect.server.ServerBlockProtectManager;
import com.artur.returnoftheancients.blockprotect.server.ServerProtectedChunk;
import com.artur.returnoftheancients.capabilities.PlayerTimer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@Mod.EventBusSubscriber
public class BlockProtectEventsHandler {
    public static final ClientBlockProtectManager CLIENT_MANAGER = new ClientBlockProtectManager();
    public static final ServerBlockProtectManager SERVER_MANAGER = new ServerBlockProtectManager();

    @SubscribeEvent
    public static void playerInteractEventLeftClickBlock(PlayerInteractEvent.LeftClickBlock e) {
        if (e.getSide() == Side.CLIENT) CLIENT_MANAGER.playerInteractEventLeftClickBlock(e);
        if (e.getSide() == Side.SERVER) SERVER_MANAGER.playerInteractEventLeftClickBlock(e);
    }

    @SubscribeEvent
    public static void attachCapabilitiesChunk(AttachCapabilitiesEvent<Chunk> e) {
        if (e.getObject() != null &&  e.getObject().getWorld().isRemote && !e.getObject().isEmpty()) CLIENT_MANAGER.attachCapabilitiesEventChunk(e);
        if (e.getObject() != null && !e.getObject().getWorld().isRemote && !e.getObject().isEmpty()) SERVER_MANAGER.attachCapabilitiesEventChunk(e);
    }

    @SubscribeEvent
    public static void tickEventServerTickEvent(TickEvent.ServerTickEvent e) {
        if (e.side == Side.SERVER) SERVER_MANAGER.tickEventServerTickEvent(e);
    }

    @SubscribeEvent
    public static void chunkWatchEventWatch(ChunkWatchEvent.Watch e) {
        if (!Objects.requireNonNull(e.getChunkInstance()).getWorld().isRemote) SERVER_MANAGER.chunkWatchEventWatch(e);
    }

    @SubscribeEvent
    public static void PlayerBlockBreakEvent(BlockEvent.BreakEvent e) {
        if ( e.getWorld().isRemote) CLIENT_MANAGER.blockEventBreakEvent(e);
        if (!e.getWorld().isRemote) SERVER_MANAGER.blockEventBreakEvent(e);
    }
}
