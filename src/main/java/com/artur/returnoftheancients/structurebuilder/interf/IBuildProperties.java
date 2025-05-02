package com.artur.returnoftheancients.structurebuilder.interf;

import net.minecraft.block.state.IBlockState;

public interface IBuildProperties {
    boolean isIgnoreAir();
    boolean isNeedProtect();
    boolean isUseEBSHook(IBlockState state);
    boolean isNeedMarkRenderUpdate();
    IBlockState blockStateHook(IBlockState state);
}
