package com.artur.returnoftheancients.structurebuilder.interf;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

public interface IBuildProperties {
    boolean isIgnoreAir();
    boolean isNeedProtect();
    boolean isUseEBSHook(IBlockState state);
    boolean isNeedMarkRenderUpdate();
    IBlockState blockStateHook(IBlockState state);
    boolean blockProtectHook(IBlockState state, BlockPos pos);
    boolean isPosAsXZCenter();
    boolean isNeedLoadLightMap();
}
