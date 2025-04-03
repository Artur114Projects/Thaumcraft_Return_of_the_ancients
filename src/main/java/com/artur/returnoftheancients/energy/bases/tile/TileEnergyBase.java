package com.artur.returnoftheancients.energy.bases.tile;

import com.artur.returnoftheancients.tileentity.TileBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class TileEnergyBase extends TileBase implements ITileEnergy {
    private long networkId = -1L;

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (nbt.hasKey("networkId")) {
            networkId = nbt.getLong("networkId");
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagCompound compound = super.writeToNBT(nbt);
        compound.setLong("networkId", networkId);
        return compound;
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onChunkUnload() {

    }

    @Override
    public void setNetworkId(long id) {
        this.networkId = id;
    }

    @Override
    public long networkId() {
        return networkId;
    }

    @Override
    public BlockPos pos() {
        return this.pos;
    }

    @Override
    public World world() {
        return this.world;
    }
}
