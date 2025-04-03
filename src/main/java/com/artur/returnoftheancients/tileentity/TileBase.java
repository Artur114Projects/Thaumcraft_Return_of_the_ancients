package com.artur.returnoftheancients.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public abstract class TileBase extends TileEntity {

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, -9, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(@NotNull NetworkManager net, SPacketUpdateTileEntity pkt) {
        this.readSyncNBT(pkt.getNbtCompound());
    }

    @Override
    public @NotNull NBTTagCompound getUpdateTag() {
        return this.writeSyncNBT(new NBTTagCompound());
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        this.readSyncNBT(tag);
    }

    public void readSyncNBT(NBTTagCompound nbt) {}
    public NBTTagCompound writeSyncNBT(NBTTagCompound nbt) {return nbt;}
}
