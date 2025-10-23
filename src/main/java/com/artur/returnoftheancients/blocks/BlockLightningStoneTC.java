package com.artur.returnoftheancients.blocks;

import com.artur.returnoftheancients.util.MaterialArray;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.blocks.basic.BlockStoneTC;

import java.util.Random;

public class BlockLightningStoneTC extends BaseBlock {
    public static final PropertyInteger LIGHT = PropertyInteger.create("light", 0, 15);
    private final String name;

    public BlockLightningStoneTC(String name) {
        super(name + "_lightning", MaterialArray.ANCIENT_STONE_ARRAY);

        this.name = name;
        this.setForCreative();
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        Item item = Item.getByNameOrId("thaumcraft:" + name);
        return item != null ? item : super.getItemDropped(state, rand, fortune);
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return false;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, LIGHT);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(LIGHT);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(LIGHT, meta);
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return 15 - state.getValue(LIGHT);
    }
}
