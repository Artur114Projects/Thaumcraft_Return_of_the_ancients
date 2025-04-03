package com.artur.returnoftheancients.energy.bases.tile;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ITileEnergy {
    boolean canConnect(EnumFacing facing);
    void setNetworkId(long id);
    boolean isEnergyLine();
    boolean isInvalid();
    long networkId();
    BlockPos pos();
    World world();
}
