package com.artur.returnoftheancients.energy.intefaces;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ITileEnergy {
    int getNetworkId();
    void setNetworkId(int id);
    BlockPos getPosE();
    World getWorldE();
    boolean isCanConnect(EnumFacing facing);
    boolean isEnergyLine();
}
