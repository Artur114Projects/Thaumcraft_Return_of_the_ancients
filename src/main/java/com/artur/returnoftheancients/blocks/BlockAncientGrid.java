package com.artur.returnoftheancients.blocks;

import com.artur.returnoftheancients.init.InitBlocks;
import com.artur.returnoftheancients.util.math.CoordinateMatrix;
import com.artur.returnoftheancients.util.math.MathUtils;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockAncientGrid extends BaseBlock {
    private static final CoordinateMatrix MATRIX = createMatrix();
    public static final PropertyDirection FACING = PropertyDirection.create("facing");
    public static final PropertyBool UP = PropertyBool.create("up");
    public static final PropertyBool DOWN = PropertyBool.create("down");
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");
    public static final PropertyBool EAST = PropertyBool.create("east");

    public BlockAncientGrid(String name, Material material, float hardness, float resistance, SoundType soundType) {
        super(name, material, hardness, resistance, soundType);

        this.setNotFillAndOpaqueCube();
        this.setTRACreativeTab();
        this.setForCreative();
        this.setLightLevel(0.75F);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer)), 4);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return MATRIX.child(state.getValue(FACING).getName()).getBoundingBox(0);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state
                .withProperty(UP, this.hasGridOn(world, pos, EnumFacing.UP))
                .withProperty(DOWN, this.hasGridOn(world, pos, EnumFacing.DOWN))
                .withProperty(WEST, this.hasGridOn(world, pos, EnumFacing.WEST))
                .withProperty(EAST, this.hasGridOn(world, pos, EnumFacing.EAST))
                .withProperty(NORTH, this.hasGridOn(world, pos, EnumFacing.NORTH))
                .withProperty(SOUTH, this.hasGridOn(world, pos, EnumFacing.SOUTH));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, UP, DOWN, WEST, EAST, NORTH, SOUTH);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).ordinal();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.values()[meta]);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    private boolean hasGridOn(IBlockAccess world, BlockPos pos, EnumFacing facing) {
        IBlockState state = world.getBlockState(pos);
        IBlockState stateN = world.getBlockState(pos.offset(facing));
        return stateN.getBlock() == InitBlocks.ANCIENT_GRID && stateN.getValue(FACING) == state.getValue(FACING);
    }

    private static CoordinateMatrix createMatrix() {
        AxisAlignedBB bb = MathUtils.createBoundingBox(0, 0, 0, 16, 4, 16);
        CoordinateMatrix matrix = new CoordinateMatrix();

        for (EnumFacing facing : EnumFacing.VALUES) {
            CoordinateMatrix child = matrix.child(facing.getName());
            child.putBoundingBox(bb, 0);
            child.translate(-0.5F, -0.5F, -0.5F);
            switch (facing) {
                case UP:
                    child.rotate(180.0F, 1.0F, 0.0F, 0.0F);
                    break;
                case NORTH:
                    child.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                    break;
                case SOUTH:
                    child.rotate(270.0F, 1.0F, 0.0F, 0.0F);
                    break;
                case WEST:
                    child.rotate(90.0F, 0.0F, 0.0F, 1.0F);
                    break;
                case EAST:
                    child.rotate(270.0F, 0.0F, 0.0F, 1.0F);
                    break;
            }
            child.translate(0.5F, 0.5F, 0.5F);
        }

        return matrix;
    }
}
