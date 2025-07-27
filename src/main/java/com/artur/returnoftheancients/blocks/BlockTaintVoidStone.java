package com.artur.returnoftheancients.blocks;

import com.artur.returnoftheancients.init.InitBlocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumBlockRenderType;

import java.util.Random;

public class BlockTaintVoidStone extends BaseBlock {
    public BlockTaintVoidStone(String name, Material material, float hardness, float resistance, SoundType soundType) {
        super(name, material, hardness, resistance, soundType);

        this.setHarvestLevel("pickaxe", 3);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(InitBlocks.TAINT_VOID_COBBLESTONE);
    }

}
