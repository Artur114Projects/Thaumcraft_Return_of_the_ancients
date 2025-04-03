package com.artur.returnoftheancients.energylegacy.tile;

import net.minecraft.util.EnumFacing;

public class TileEnergySource extends TileEnergyProviderBase {
    @Override
    public boolean isCanConnect(EnumFacing facing) {
        return true;
    }

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
}
