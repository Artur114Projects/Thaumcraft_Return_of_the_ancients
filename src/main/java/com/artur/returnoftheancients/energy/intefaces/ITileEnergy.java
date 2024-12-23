package com.artur.returnoftheancients.energy.intefaces;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public interface ITileEnergy {
    int getNetworkId();
    void setNetworkId(int id);
    BlockPos getPos();
    boolean isCanConnect(EnumFacing facing);
    boolean isEnergyLine();
}
