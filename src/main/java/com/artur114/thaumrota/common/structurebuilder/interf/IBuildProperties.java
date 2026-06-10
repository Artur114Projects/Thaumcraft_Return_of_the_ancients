package com.artur114.thaumrota.common.structurebuilder.interf;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public interface IBuildProperties {
    boolean isIgnoreAir();
    boolean isNeedProtect();
    boolean isUseEBSHook(IBlockState state);
    boolean isNeedMarkRenderUpdate();
    IBlockState blockStateHook(IBlockState state);
    boolean blockProtectHook(IBlockState state, BlockPos pos);
    TileEntity tileEntityHook(TileEntity tile, NBTTagCompound data);
    boolean isPosAsXZCenter();
    boolean isNeedLoadLightMap();
}
