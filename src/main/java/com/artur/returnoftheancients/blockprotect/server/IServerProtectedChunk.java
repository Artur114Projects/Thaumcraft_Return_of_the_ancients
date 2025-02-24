package com.artur.returnoftheancients.blockprotect.server;

import com.artur.returnoftheancients.blockprotect.IProtectedChunk;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.common.util.INBTSerializable;

public interface IServerProtectedChunk extends IProtectedChunk, INBTSerializable<NBTTagCompound> {
    void protect(BlockPos pos);
    void unProtect(BlockPos pos);
    void protect(int x, int y, int z);
    void unProtect(int x, int y, int z);
    void syncToClient(EntityPlayerMP clientIn);
    boolean isChanged();
    ChunkPos getPos();
    int getDimension();

    default boolean isRemote() {return false;}
}
