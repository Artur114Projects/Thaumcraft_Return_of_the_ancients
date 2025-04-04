package com.artur.returnoftheancients.blockprotect.server;

import com.artur.returnoftheancients.blockprotect.BlockProtectHandler;
import com.artur.returnoftheancients.capabilities.GenericCapProviderS;
import com.artur.returnoftheancients.capabilities.TRACapabilities;
import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ServerBlockProtectManager {
    private static final Set<IServerProtectedChunk> syncQueue = new HashSet<>();

    /*--------------------------------------EVENTS--------------------------------------*/

    public void tickEventServerTickEvent(TickEvent.ServerTickEvent e) {
        if (e.phase == TickEvent.Phase.END) {
            return;
        }

        this.processSyncQueue();
    }

    public void playerInteractEventLeftClickBlock(PlayerInteractEvent.LeftClickBlock e) {
        if (e.getEntityPlayer().isCreative() && e.getEntityPlayer().isSneaking()) {
            return;
        }

        if (BlockProtectHandler.hasProtect(e.getWorld(), e.getPos())) {
            e.setCanceled(true);
        }
    }

    public void blockEventBreakEvent(BlockEvent.BreakEvent e) {
        if (e.getPlayer().isCreative() && e.getPlayer().isSneaking()) {
            BlockProtectHandler.unProtect(e.getWorld(), e.getPos());
            return;
        }

        if (BlockProtectHandler.hasProtect(e.getWorld(), e.getPos())) {
            e.setCanceled(true);
        }
    }

    public void attachCapabilitiesEventChunk(AttachCapabilitiesEvent<Chunk> e) {
        e.addCapability(new ResourceLocation(Referense.MODID, "protected_chunk"), new GenericCapProviderS<>(new ServerProtectedChunk(e.getObject().getPos(), e.getObject().getWorld().provider.getDimension()), TRACapabilities.PROTECTED_CHUNK));
    }

    public void chunkWatchEventWatch(ChunkWatchEvent.Watch e) {
        Chunk chunk = e.getChunkInstance();
        if (chunk != null && !chunk.isEmpty()) {
            IServerProtectedChunk protectedChunk = (IServerProtectedChunk) chunk.getCapability(TRACapabilities.PROTECTED_CHUNK, null);
            if (protectedChunk != null && protectedChunk.isChanged()) {
                protectedChunk.syncToClient(e.getPlayer());
            }
        }
    }

    /*--------------------------------------UTILS--------------------------------------*/

    private void processSyncQueue() {
        if (syncQueue.isEmpty()) return;

        Iterator<IServerProtectedChunk> iterator = syncQueue.iterator();
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();

        while (iterator.hasNext()) {
            IServerProtectedChunk chunk = iterator.next();
            World world = server.getWorld(chunk.getDimension());
            for (EntityPlayer player : world.playerEntities) {
                if (isPlayerWatch((EntityPlayerMP) player, world, chunk.getPos())) {
                    chunk.syncToClient((EntityPlayerMP) player);
                }
            }
            iterator.remove();
        }
    }

    private boolean isPlayerWatch(EntityPlayerMP player, World world, ChunkPos pos) {
        return ((WorldServer) world).getPlayerChunkMap().isPlayerWatchingChunk(player, pos.x, pos.z);
    }

    /*--------------------------------------PUBLIC METHODS--------------------------------------*/

    public void addToSyncQueue(IServerProtectedChunk chunk) {
        syncQueue.add(chunk);
    }
}
