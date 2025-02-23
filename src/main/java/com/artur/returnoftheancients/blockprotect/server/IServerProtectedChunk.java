package com.artur.returnoftheancients.blockprotect.server;

import com.artur.returnoftheancients.blockprotect.IProtectedChunk;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

public interface IServerProtectedChunk extends IProtectedChunk, INBTSerializable<NBTTagCompound> {
    void protect(BlockPos pos);

    default boolean isRemote() {return false;}
}
