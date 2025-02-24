package com.artur.returnoftheancients.blockprotect.client;

import com.artur.returnoftheancients.blockprotect.IProtectedChunk;
import net.minecraft.nbt.NBTTagCompound;

public interface IClientProtectedChunk extends IProtectedChunk {
    void processSyncData(NBTTagCompound dataIn);

    default boolean isRemote() {return true;}
}
