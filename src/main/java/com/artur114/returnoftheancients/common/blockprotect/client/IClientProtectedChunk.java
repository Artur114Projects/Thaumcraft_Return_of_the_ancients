package com.artur114.returnoftheancients.common.blockprotect.client;

import com.artur114.returnoftheancients.common.blockprotect.IProtectedChunk;
import net.minecraft.nbt.NBTTagCompound;

public interface IClientProtectedChunk extends IProtectedChunk {
    void processSyncData(NBTTagCompound dataIn);

    default boolean isRemote() {return true;}
}
