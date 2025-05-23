package com.artur.returnoftheancients.tileentity;

import com.artur.returnoftheancients.blocks.BaseBlock;
import com.artur.returnoftheancients.init.InitTileEntity;
import com.artur.returnoftheancients.util.interfaces.RunnableWithParam;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public abstract class BlockTileEntity<T extends TileEntity> extends BaseBlock {

    public BlockTileEntity(String name, Material material, float hardness, float resistance, SoundType soundType) {
        super(name, material, hardness, resistance, soundType);
        InitTileEntity.TILE_ENTITIES.add(this);
    }

    public abstract Class<T> getTileEntityClass();

    public T getTileEntity(IBlockAccess world, BlockPos position) {
        return (T) world.getTileEntity(position);
    }

    @Override
    public boolean hasTileEntity(@NotNull IBlockState blockState) {
        return true;
    }

    @Nullable
    @Override
    public abstract T createTileEntity(@NotNull World world, @NotNull IBlockState blockState);

    protected void getTileAndCallRunnable(World world, BlockPos pos, RunnableWithParam<T> run) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile != null && this.getTileEntityClass().isAssignableFrom(tile.getClass())) {
            run.run(this.getTileEntityClass().cast(tile));
        }
    }
}