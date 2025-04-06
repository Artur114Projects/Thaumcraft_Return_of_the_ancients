package com.artur.returnoftheancients.tileentity;

import com.artur.returnoftheancients.energy.bases.tile.TileEnergyBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;

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
        return count * this.currentEfficiency();
    }

    private float currentEfficiency() {
        return 0.9998F;
    }
}
