package com.artur.returnoftheancients.tileentity;

import com.artur.returnoftheancients.energylegacy.intefaces.ITileEnergy;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityEnergyLine extends TileEntity implements ITileEnergy {
    private int energyNetworkId = -1;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagCompound nbt = super.writeToNBT(compound);
        nbt.setInteger("NetworkId", energyNetworkId);
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        energyNetworkId = compound.getInteger("NetworkId");
    }

    @Override
    public int getNetworkId() {
        return energyNetworkId;
    }

    @Override
    public void setNetworkId(int id) {
        energyNetworkId = id;
    }

    @Override
    public BlockPos getPosE() {
        return this.pos;
    }

    @Override
    public World getWorldE() {
        return this.world;
    }

    @Override
    public boolean isCanConnect(EnumFacing facing) {
        return true;
    }

    @Override
    public boolean isEnergyLine() {
        return true;
    }
}
