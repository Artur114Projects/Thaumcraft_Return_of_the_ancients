package com.artur.returnoftheancients.blockprotect.server;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class ServerProtectedChunk implements IServerProtectedChunk {
    @Override
    public boolean hasProtect(BlockPos pos) {
        return false;
    }

    @Override
    public void protect(BlockPos pos) {

    }

    @Override
    public NBTTagCompound serializeNBT() {
        return null;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {

    }
}
