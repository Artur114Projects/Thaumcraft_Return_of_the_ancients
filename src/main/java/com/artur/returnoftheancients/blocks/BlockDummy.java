package com.artur.returnoftheancients.blocks;

import com.artur.returnoftheancients.tileentity.BlockTileEntity;
import com.artur.returnoftheancients.tileentity.TileEntityDummy;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class BlockDummy extends BlockTileEntity<TileEntityDummy> {
    public static boolean SAFE_BREAK = false;

    public BlockDummy(String name, Material material, float hardness, float resistance, SoundType soundType) {
        super(name, material, hardness, resistance, soundType);
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }

    @Override
    protected ItemStack getSilkTouchDrop(IBlockState state) {
        return new ItemStack(Blocks.AIR);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(Blocks.AIR);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (!SAFE_BREAK) {
            TileEntity dummy = worldIn.getTileEntity(pos);
            if (dummy instanceof TileEntityDummy) {
                ((TileEntityDummy) dummy).onBreak();
            }
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public Class<TileEntityDummy> getTileEntityClass() {
        return TileEntityDummy.class;
    }

    @Override
    public @Nullable TileEntityDummy createTileEntity(@NotNull World world, @NotNull IBlockState blockState) {
        return new TileEntityDummy();
    }
}
