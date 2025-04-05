package com.artur.returnoftheancients.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
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
        return this.writeSyncNBT(this.writeInternal(new NBTTagCompound()));
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        this.readSyncNBT(tag);
    }

    protected NBTTagCompound writeInternal(NBTTagCompound nbt) {
        ResourceLocation resourcelocation = TileEntity.getKey(this.getClass());

        if (resourcelocation == null) {
            throw new RuntimeException(this.getClass() + " is missing a mapping! This is a bug!");
        } else {
            nbt.setString("id", resourcelocation.toString());
            nbt.setInteger("x", this.pos.getX());
            nbt.setInteger("y", this.pos.getY());
            nbt.setInteger("z", this.pos.getZ());
            return nbt;
        }
    }

    public void syncTile(boolean render) {
        IBlockState state = this.world.getBlockState(this.pos);
        this.world.notifyBlockUpdate(this.pos, state, state, 2 + (render ? 4 : 0));
    }

    public void readSyncNBT(NBTTagCompound nbt) {}
    public NBTTagCompound writeSyncNBT(NBTTagCompound nbt) {return nbt;}
}
