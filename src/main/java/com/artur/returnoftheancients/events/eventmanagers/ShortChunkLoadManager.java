package com.artur.returnoftheancients.events.eventmanagers;

import com.artur.returnoftheancients.events.ServerEventsHandler;
import com.artur.returnoftheancients.main.MainR;
import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.util.interfaces.IWriteToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;

public class ShortChunkLoadManager {
    private final HashMap<ForgeChunkManager.Ticket, HashMap<ChunkPos, TickingChunk>> LOADED_CHUNKS = new HashMap<>();
    private final HashMap<World, ForgeChunkManager.Ticket> TICKETS = new HashMap<>();


    public void tickEventServerTickEvent(TickEvent.ServerTickEvent e) {
        if (e.phase != TickEvent.Phase.START || TICKETS.isEmpty()) {
            return;
        }

        Iterator<ForgeChunkManager.Ticket> ticketIterator = TICKETS.values().iterator();

        while (ticketIterator.hasNext()) {
            ForgeChunkManager.Ticket ticket = ticketIterator.next();
            Collection<TickingChunk> chunks = LOADED_CHUNKS.get(ticket).values();

            if (chunks.isEmpty()) {
                ForgeChunkManager.releaseTicket(ticket);
                if (TRAConfigs.Any.debugMode) System.out.println("Released ticked " + ticket);
                ticketIterator.remove();
                continue;
            }

            Iterator<TickingChunk> iterator = chunks.iterator();
            boolean flag = false;

            while (iterator.hasNext()) {
                TickingChunk chunk = iterator.next();

                if (chunk.isDone()) {
                    chunk.unForce();
                    iterator.remove();
                    if (TRAConfigs.Any.debugMode) System.out.println("Un forced chunk " + chunk.pos);
                    flag = true;
                    continue;
                }

                chunk.tick();
            }

            if (flag) {
                this.updateTickedNBTData(ticket);
            }
        }
    }

    public void worldEventSave(WorldEvent.Save e) {
        ForgeChunkManager.Ticket ticket = TICKETS.get(e.getWorld());

        if (ticket != null) {
            this.updateTickedNBTData(ticket);
        }
    }

    public void unload() {
        LOADED_CHUNKS.clear();
        TICKETS.clear();
    }

    public void unloadArea(World world, ChunkPos pos, int radius) {
        ForgeChunkManager.Ticket ticket = TICKETS.get(world);

        if (ticket != null) {
            boolean flag = false;
            for (int x = pos.x - radius; x != pos.x + radius + 1; x++) {
                for (int z = pos.z - radius; z != pos.z + radius + 1; z++) {
                    flag |= this.unloadChunk(ticket, new ChunkPos(x, z));
                }
            }
            if (flag) {
                this.updateTickedNBTData(ticket);
            }
        }
    }

    public void unloadChunk(World world, ChunkPos pos) {
        ForgeChunkManager.Ticket ticket = TICKETS.get(world);

        if (ticket != null) {
            if (this.unloadChunk(ticket, pos)) {
                this.updateTickedNBTData(ticket);
            }
        }
    }

    private boolean unloadChunk(ForgeChunkManager.Ticket ticket, ChunkPos pos) {
        TickingChunk chunk = LOADED_CHUNKS.get(ticket).get(pos);
        if (chunk != null) {
            LOADED_CHUNKS.get(ticket).remove(pos);
            chunk.unForce();
            return true;
        }
        return false;
    }

    public void loadChunk(World world, ChunkPos pos, int time) {
        ForgeChunkManager.Ticket ticket = this.getOrCreateTicked(world);

        if (ticket != null) {
            HashMap<ChunkPos, TickingChunk> chunks = LOADED_CHUNKS.computeIfAbsent(ticket, k -> new HashMap<>());
            if (!chunks.containsKey(pos)) {
                ForgeChunkManager.forceChunk(ticket, pos);
                chunks.put(pos, new TickingChunk(ticket, pos, time));
                if (TRAConfigs.Any.debugMode) System.out.println("Forced chunk " + pos);
            }
        } else {
            System.out.println("It was not possible to load the chunk!");
        }
    }

    public void loadChunk(World world, ChunkPos pos) {
        this.loadChunk(world, pos, 2);
    }

    public void loadArea(World world, ChunkPos pos, int radius) {
        this.loadArea(world, pos, radius, 2);
    }

    public void loadArea(World world, ChunkPos pos, int radius, int time) {
        for (int x = pos.x - radius; x != pos.x + radius + 1; x++) {
            for (int z = pos.z - radius; z != pos.z + radius + 1; z++) {
                this.loadChunk(world, new ChunkPos(x, z), time);
            }
        }
    }

    public void loadArea(World world, ChunkPos start, ChunkPos end, int time) {
        int dx = end.x - start.x;
        int dz = end.z - start.z;

        EnumFacing.AxisDirection xDirections = dx > 0 ? EnumFacing.AxisDirection.POSITIVE : EnumFacing.AxisDirection.NEGATIVE;
        EnumFacing.AxisDirection zDirections = dz > 0 ? EnumFacing.AxisDirection.POSITIVE : EnumFacing.AxisDirection.NEGATIVE;

        dx = Math.abs(dx + xDirections.getOffset());
        dz = Math.abs(dz + zDirections.getOffset());

        for (int x = 0; x != dx; x++) {
            for (int z = 0; z != dz; z++) {
                this.loadChunk(world, new ChunkPos(start.x + x * xDirections.getOffset(), start.z + z * zDirections.getOffset()));
            }
        }
    }

    private ForgeChunkManager.Ticket getOrCreateTicked(World world) {
        ForgeChunkManager.Ticket ticket;

        if (TICKETS.containsKey(world)) {
            ticket = TICKETS.get(world);
        } else {
            ticket = ForgeChunkManager.requestTicket(MainR.INSTANCE, world, ForgeChunkManager.Type.NORMAL);
            if (ticket != null) {
                ticket.getModData().setString("userClassName", this.getClass().getName());
                TICKETS.put(world, ticket);
                if (TRAConfigs.Any.debugMode) System.out.println("Created ticked " + ticket);
            }
        }

        return ticket;
    }

    private void updateTickedNBTData(ForgeChunkManager.Ticket ticket) {
        if (ticket.getModData().hasKey("chunks")) {
            ticket.getModData().setTag("chunks", IWriteToNBT.objectsToNBT(LOADED_CHUNKS.get(ticket).values()));
        }
    }

    public static void loadingCallback(ForgeChunkManager.Ticket ticket, World world) {
        if (ticket.getModData().hasKey("chunks")) {
            ServerEventsHandler.SHORT_CHUNK_LOAD_MANAGER.TICKETS.put(world, ticket);
            ServerEventsHandler.SHORT_CHUNK_LOAD_MANAGER.LOADED_CHUNKS.put(ticket, TickingChunk.getChunksAsNBT(ticket));
        } else {
            ForgeChunkManager.releaseTicket(ticket);
        }
    }

    private static class TickingChunk implements IWriteToNBT {
        private final ForgeChunkManager.Ticket ticket;
        private final ChunkPos pos;
        private final int maxTime;
        private int time;

        private TickingChunk(ForgeChunkManager.Ticket ticket, ChunkPos pos, int maxTime) {
            this.maxTime = maxTime;
            this.ticket = ticket;
            this.pos = pos;

            if (maxTime < 8) {
                return;
            }

            NBTTagList list = ticket.getModData().getTagList("chunks", 10);
            list.appendTag(this.writeToNBT(new NBTTagCompound()));
            ticket.getModData().setTag("chunks", list);
        }

        private TickingChunk(ForgeChunkManager.Ticket ticket, ChunkPos pos, NBTTagCompound nbt) {
            this.maxTime = nbt.getInteger("maxTime");
            this.time = nbt.getInteger("time");
            this.ticket = ticket;
            this.pos = pos;
        }

        private void tick() {
            if (maxTime == -1) return;

            time++;
        }

        private boolean isDone() {
            return maxTime != -1 && time >= maxTime;
        }

        private void unForce() {
            ForgeChunkManager.unforceChunk(ticket, pos);
        }

        @Override
        public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
            nbt.setInteger("maxTime", maxTime);
            nbt.setInteger("time", time);
            nbt.setInteger("x", pos.x);
            nbt.setInteger("z", pos.z);
            return nbt;
        }

        private static HashMap<ChunkPos, TickingChunk> getChunksAsNBT(ForgeChunkManager.Ticket ticket) {
            NBTTagList list = ticket.getModData().getTagList("chunks", 10);
            HashMap<ChunkPos, TickingChunk> ret = new HashMap<>();

            for (int i = 0; i != list.tagCount(); i++) {
                NBTTagCompound chunk = list.getCompoundTagAt(i);
                ChunkPos pos = new ChunkPos(chunk.getInteger("x"), chunk.getInteger("z"));
                ret.put(pos, new TickingChunk(ticket, pos, chunk));

                ForgeChunkManager.forceChunk(ticket, pos);
            }

            return ret;
        }
    }
}