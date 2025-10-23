package com.artur.returnoftheancients.blocks;

import com.artur.returnoftheancients.util.math.MathUtils;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMovingLava extends BaseBlock {
    public static final PropertyDirection DIRECTION = PropertyDirection.create("dir", (f) -> f.getAxis().isHorizontal());

    public BlockMovingLava(String name) {
        super(name, Material.ROCK, 1.0F, 10.0F, SoundType.STONE);

        this.setNotFillAndOpaqueCube();
        this.setLightLevel(1.0F);
        this.setForCreative();
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(DIRECTION, placer.getHorizontalFacing()), 4);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return MathUtils.createBoundingBox(0, 0, 0, 16, 14, 16);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, DIRECTION);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(DIRECTION).ordinal() - 2;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(DIRECTION, EnumFacing.values()[meta + 2]);
    }
}
