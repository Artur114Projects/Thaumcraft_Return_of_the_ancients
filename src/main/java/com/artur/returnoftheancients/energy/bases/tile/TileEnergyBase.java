package com.artur.returnoftheancients.energy.bases.tile;

import com.artur.returnoftheancients.capabilities.TRACapabilities;
import com.artur.returnoftheancients.energy.system.EnergySystemsManager;
import com.artur.returnoftheancients.tileentity.TileBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class TileEnergyBase extends TileBase implements ITileEnergy {
    private boolean isLoaded = false;
    private boolean isAdded = false;
    private long networkId = -1L;

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (nbt.hasKey("networkId")) {
            this.networkId = nbt.getLong("networkId");
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagCompound compound = super.writeToNBT(nbt);
        compound.setLong("networkId", this.networkId);
        return compound;
    }

    @Override
    public NBTTagCompound writeSyncNBT(NBTTagCompound nbt) {
        if (this.isAdded) {
            nbt.setLong("networkId", -1L);
        } else {
            nbt.setLong("networkId", this.networkId);
        }
        nbt.setBoolean("isLoaded", this.isLoaded);
        this.isLoaded = false;
        this.isAdded = false;
        return nbt;
    }

    @Override
    public void readSyncNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("networkId")) {
            this.networkId = nbt.getLong("networkId");
        }

        if (nbt.getBoolean("isLoaded")) {
            EnergySystemsManager manager = this.world.getCapability(TRACapabilities.ENERGY_SYSTEMS_MANAGER, null);
            if (manager != null) manager.onTileLoad(this);
        }
    }

    @Override
    public void onLoad() {
        if (!this.world.isRemote) {
            if (this.networkId == -1) this.isAdded = true;
            EnergySystemsManager manager = this.world.getCapability(TRACapabilities.ENERGY_SYSTEMS_MANAGER, null);
            if (manager != null) manager.onTileLoad(this);
            this.isLoaded = true;
        }
    }

    @Override
    public void onChunkUnload() {
        EnergySystemsManager manager = this.world.getCapability(TRACapabilities.ENERGY_SYSTEMS_MANAGER, null);
        if (manager != null) manager.onTileUnload(this);
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if (this.world.isRemote) {
            EnergySystemsManager manager = this.world.getCapability(TRACapabilities.ENERGY_SYSTEMS_MANAGER, null);
            if (manager != null) manager.onBlockDestroyed(this);
        }
    }

    @Override
    public void setNetworkId(long id) {
        this.networkId = id;
    }

    @Override
    public long networkId() {
        return this.networkId;
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
