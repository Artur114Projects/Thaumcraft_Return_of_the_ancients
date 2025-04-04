package com.artur.returnoftheancients.tileentity;

import com.artur.returnoftheancients.energy.bases.tile.TileEnergyBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityEnergyLine extends TileEnergyBase {

    @Override
    public boolean isEnergyLine() {
        return true;
    }

    @Override
    public boolean canConnect(EnumFacing facing) {
        return true;
    }

    @Override
    public float transferEnergy(float count) {
        if (this.world.isRemote) {
            this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.pos.getX() + this.world.rand.nextDouble(), pos.getY() + 1, this.pos.getZ() + this.world.rand.nextDouble(), 0, 0.1, 0);
        }
        return count;
    }
}
