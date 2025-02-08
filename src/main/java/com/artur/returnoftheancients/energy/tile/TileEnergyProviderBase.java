package com.artur.returnoftheancients.energy.tile;

import com.artur.returnoftheancients.energy.EnergySystemsProvider;
import com.artur.returnoftheancients.energy.intefaces.ITileEnergyProvider;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.common.tiles.TileThaumcraft;

public abstract class TileEnergyProviderBase extends TileThaumcraft implements ITileEnergyProvider {
    private int energyNetworkId = -1;
    private boolean isAdding = false;

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (nbt.hasKey("NetworkId")) {
            energyNetworkId = nbt.getInteger("NetworkId");
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagCompound compound = super.writeToNBT(nbt);
        compound.setInteger("NetworkId", energyNetworkId);
        return compound;
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
    public void onLoad() {
        if (!isAdding && !world.isRemote) {
            EnergySystemsProvider.onTileLoad(this);
        }
        isAdding = false;
    }

    @Override
    public void setAdding() {
        isAdding = true;
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
    public boolean isLoaded() {
        return world.isBlockLoaded(pos);
    }
}
