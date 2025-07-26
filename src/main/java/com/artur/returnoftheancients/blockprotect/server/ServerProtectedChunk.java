package com.artur.returnoftheancients.blockprotect.server;

import com.artur.returnoftheancients.blockprotect.BlockProtectEventsHandler;
import com.artur.returnoftheancients.blockprotect.ExtendedProtectStorage1bit;
import com.artur.returnoftheancients.blockprotect.IExtendedProtectStorage;
import com.artur.returnoftheancients.blockprotect.ProtectedChunk;
import com.artur.returnoftheancients.main.MainR;
import com.artur.returnoftheancients.network.ClientPacketSyncProtectedChunk;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

public class ServerProtectedChunk extends ProtectedChunk implements IServerProtectedChunk {
    private boolean isChanged = false;

    public ServerProtectedChunk(ChunkPos pos, int dimension) {
        super(pos, dimension);
    }

    @Override
    public void protect(BlockPos pos) {
        this.protect(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public void unProtect(BlockPos pos) {
        this.unProtect(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public void protect(int x, int y, int z) {
        if (y >> 4 >= 16 || y >> 4 < 0) return;
        IExtendedProtectStorage storage = this.storages[y >> 4];
        if (storage == null) {
            storage = new ExtendedProtectStorage1bit();
            this.storages[y >> 4] = storage;
            this.initStoragesCount++;
            this.isChanged = true;
        }

        boolean flag = storage.setProtect(x, y, z, true);

        if (flag) {
            BlockProtectEventsHandler.SERVER_MANAGER.addToSyncQueue(this);
        }
    }

    @Override
    public void unProtect(int x, int y, int z) {
        if (y >> 4 >= 16) {
            return;
        }
        IExtendedProtectStorage storage = this.storages[y >> 4];
        if (storage == null) {
            return;
        }

        boolean flag = storage.setProtect(x, y, z, false);

        if (storage.isEmpty()) {
            this.storages[y >> 4] = null;
            this.initStoragesCount--;
        }

        if (flag) {
            BlockProtectEventsHandler.SERVER_MANAGER.addToSyncQueue(this);
        }
    }

    @Override
    public void syncToClient(EntityPlayerMP clientIn) {
        if (!this.isEmpty() && clientIn != null) {
            MainR.NETWORK.sendTo(new ClientPacketSyncProtectedChunk(this.dimension, this.pos, this.serializeNBT()), clientIn);
        }
    }

    @Override
    public boolean isChanged() {
        return this.isChanged;
    }

    @Override
    public int getDimension() {
        return dimension;
    }

    @Override
    public ChunkPos getPos() {
        return pos;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        if (this.isEmpty()) {
            return new NBTTagCompound();
        }

        NBTTagCompound nbt = new NBTTagCompound();
        NBTTagList list = new NBTTagList();

        for (int y = 0; y != 16; y++) {
            IExtendedProtectStorage storage = storages[y];

            if (storage != null) {
                NBTTagCompound data = new NBTTagCompound();
                data.setInteger("storageIndex", y);
                list.appendTag(storage.writeToNBT(data));
            }
        }

        nbt.setTag("storages", list);
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        if (!nbt.hasKey("storages")) {
            return;
        }

        NBTTagList list = nbt.getTagList("storages", 10);

        for (int i = 0; i != list.tagCount(); i++) {
            NBTTagCompound data = list.getCompoundTagAt(i);
            this.storages[data.getInteger("storageIndex")] = new ExtendedProtectStorage1bit(data);
            this.initStoragesCount++;
            this.isChanged = true;
        }
    }
}
