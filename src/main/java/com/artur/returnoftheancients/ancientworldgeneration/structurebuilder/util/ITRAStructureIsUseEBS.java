package com.artur.returnoftheancients.ancientworldgeneration.structurebuilder.util;

import net.minecraft.block.state.IBlockState;

public interface ITRAStructureIsUseEBS {
    boolean isUseEBS(int x, int y, int z, IBlockState state);
}