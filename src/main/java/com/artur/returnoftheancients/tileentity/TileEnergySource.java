package com.artur.returnoftheancients.tileentity;

import com.artur.returnoftheancients.energy.bases.tile.ITileEnergyProvider;
import com.artur.returnoftheancients.energy.bases.tile.TileEnergyBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;

public class TileEnergySource extends TileEnergyBase implements ITileEnergyProvider {

    @Override
    public float canAdd(float count) {
        return 0;
    }

    @Override
    public float add(float count) {
        return 0;
    }

    @Override
    public float take(float count) {
        return count;
    }

    @Override
    public float canTake(float count) {
        return count;
    }

    @Override
    public float maxInput() {
        return 0;
    }

    @Override
    public float maxOutput() {
        return Float.MAX_VALUE;
    }


    @Override
    public boolean canAddFromFacing(EnumFacing facing) {
        return false;
    }

    @Override
    public boolean canTakeFromFacing(EnumFacing facing) {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean isNeedAdd() {
        return false;
    }

    @Override
    public boolean canConnect(EnumFacing facing) {
        return true;
    }
}
