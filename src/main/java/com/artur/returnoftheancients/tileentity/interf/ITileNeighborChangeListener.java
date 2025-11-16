package com.artur.returnoftheancients.tileentity.interf;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface ITileNeighborChangeListener {
    void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor);
}
