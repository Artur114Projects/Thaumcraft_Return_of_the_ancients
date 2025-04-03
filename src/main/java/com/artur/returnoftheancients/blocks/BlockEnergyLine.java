package com.artur.returnoftheancients.blocks;

import com.artur.returnoftheancients.energylegacy.block.BlockEnergyBase;
import com.artur.returnoftheancients.init.InitItems;
import com.artur.returnoftheancients.main.MainR;
import com.artur.returnoftheancients.tileentity.TileEntityEnergyLine;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlockEnergyLine extends BlockEnergyBase<TileEntityEnergyLine> {
    public static final PropertyBool DOWN = PropertyBool.create("down");
    public static final PropertyBool UP = PropertyBool.create("up");
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");
    public static final PropertyBool EAST = PropertyBool.create("east");


    public BlockEnergyLine(String name, Material material, float hardness, float resistance, SoundType soundType) {
        super(name, material, hardness, resistance, soundType);
        InitItems.ITEMS.remove(item);
        item = new ItemBlockEnergyLine(this).setRegistryName(this.getRegistryName());
        InitItems.ITEMS.add(item);
        setCreativeTab(MainR.RETURN_OF_ANCIENTS_TAB);
    }


    @Override
    public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean isActualState) {
        addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.377, 0.377, 0.377, 0.623D, 0.623D, 0.623D));
        if (canConnectTo(world, pos, EnumFacing.DOWN)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes,
                    new AxisAlignedBB(0.377, 0, 0.377, 0.623D, 0.623D, 0.623D));
        }
        if (canConnectTo(world, pos, EnumFacing.UP)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes,
                    new AxisAlignedBB(0.377, 0.377, 0.377, 0.623D, 1, 0.623D));
        }
        if (canConnectTo(world, pos, EnumFacing.NORTH)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes,
                    new AxisAlignedBB(0.377, 0.377, 0, 0.623D, 0.623D, 0.623D));
        }
        if (canConnectTo(world, pos, EnumFacing.SOUTH)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes,
                    new AxisAlignedBB(0.377, 0.377, 0.377, 0.623D, 0.623D, 1));
        }
        if (canConnectTo(world, pos, EnumFacing.EAST)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes,
                    new AxisAlignedBB(0.377, 0.377, 0.377, 1, 0.623D, 0.623D));
        }
        if (canConnectTo(world, pos, EnumFacing.WEST)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes,
                    new AxisAlignedBB(0, 0.377, 0.377, 0.623D, 0.623D, 0.623D));
        }
    }

    @Override
    public @NotNull AxisAlignedBB getBoundingBox(@NotNull IBlockState state, @NotNull IBlockAccess world, @NotNull BlockPos pos) {
        double[] sideBound = new double[6];
        sideBound[0]= sideBound[1]= sideBound[2]=0.377D;
        sideBound[3]= sideBound[4]= sideBound[5]=0.6231D;

        if (canConnectTo(world, pos, EnumFacing.DOWN)) {
            sideBound[1]=0;
        }
        if (canConnectTo(world, pos, EnumFacing.UP)) {
            sideBound[4]=1;
        }
        if (canConnectTo(world, pos, EnumFacing.NORTH)) {
            sideBound[2]=0;
        }
        if (canConnectTo(world, pos, EnumFacing.SOUTH)) {
            sideBound[5]=1;
        }
        if (canConnectTo(world, pos, EnumFacing.WEST)) {
            sideBound[0]=0;
        }
        if (canConnectTo(world, pos, EnumFacing.EAST)) {
            sideBound[3]=1;
        }

        return new AxisAlignedBB(sideBound[0], sideBound[1], sideBound[2], sideBound[3], sideBound[4], sideBound[5]);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, UP, DOWN, NORTH, SOUTH, WEST, EAST);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state
                .withProperty(DOWN, canConnectTo(world, pos, EnumFacing.DOWN))
                .withProperty(UP, canConnectTo(world, pos, EnumFacing.UP))
                .withProperty(NORTH, canConnectTo(world, pos, EnumFacing.NORTH))
                .withProperty(SOUTH, canConnectTo(world, pos, EnumFacing.SOUTH))
                .withProperty(WEST, canConnectTo(world, pos, EnumFacing.WEST))
                .withProperty(EAST, canConnectTo(world, pos, EnumFacing.EAST));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public Class<TileEntityEnergyLine> getTileEntityClass() {
        return TileEntityEnergyLine.class;
    }

    @Override
    public @Nullable TileEntityEnergyLine createTileEntity(@NotNull World world, @NotNull IBlockState blockState) {
        return new TileEntityEnergyLine();
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
    }

    private static class ItemBlockEnergyLine extends ItemBlock {

        public ItemBlockEnergyLine(Block block) {
            super(block);
        }

        @Override
        public @NotNull IRarity getForgeRarity(@NotNull ItemStack stack) {
            return EnumRarity.UNCOMMON;
        }
    }
}
