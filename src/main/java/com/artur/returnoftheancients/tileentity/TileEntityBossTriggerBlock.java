package com.artur.returnoftheancients.tileentity;

import net.minecraft.block.BlockFurnace;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class TileEntityBossTriggerBlock extends TileEntity {

    private int count;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {

        tagCompound.setInteger("count", this.count);

        return super.writeToNBT(tagCompound);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {

        this.count = tagCompound.getInteger("count");

        super.readFromNBT(tagCompound);
    }

    public int getCount() {

        return this.count;
    }

    public void incrementCount() {

        this.count++;

        this.markDirty();
    }

    public void decrementCount() {

        this.count--;

        this.markDirty();
    }

    @Override
    public BlockPos getPos() {
        return pos;
    }
}