package com.artur.returnoftheancients.blockprotect.client;

import com.artur.returnoftheancients.blockprotect.ExtendedProtectStorage1bit;
import com.artur.returnoftheancients.blockprotect.ProtectedChunk;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.util.Arrays;

public class ClientProtectedChunk extends ProtectedChunk implements IClientProtectedChunk {

    public ClientProtectedChunk(ChunkPos pos, int dimension) {
        super(pos, dimension);
    }

    @Override
    public void processSyncData(NBTTagCompound dataIn) {
        Arrays.fill(this.storages, null);
        this.initStoragesCount = 0;

        if (!dataIn.hasKey("storages")) {
            return;
        }

        NBTTagList list = dataIn.getTagList("storages", 10);

        for (int i = 0; i != list.tagCount(); i++) {
            NBTTagCompound data = list.getCompoundTagAt(i);
            this.storages[data.getInteger("storageIndex")] = new ExtendedProtectStorage1bit(data);
            this.initStoragesCount++;
        }
    }
}
