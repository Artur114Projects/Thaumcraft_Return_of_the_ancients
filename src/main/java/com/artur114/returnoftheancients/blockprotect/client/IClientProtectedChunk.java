package com.artur114.returnoftheancients.blockprotect.client;

import com.artur114.returnoftheancients.blockprotect.IProtectedChunk;
import net.minecraft.nbt.NBTTagCompound;

public interface IClientProtectedChunk extends IProtectedChunk {
    void processSyncData(NBTTagCompound dataIn);

    default boolean isRemote() {return true;}
}
