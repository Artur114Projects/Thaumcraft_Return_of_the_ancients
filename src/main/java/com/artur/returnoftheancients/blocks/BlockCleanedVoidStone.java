package com.artur.returnoftheancients.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import thaumcraft.api.items.ItemsTC;

import java.util.Random;

public class BlockCleanedVoidStone extends BaseBlock {
    public BlockCleanedVoidStone(String name, Material material, float hardness, float resistance, SoundType soundType) {
        super(name, material, hardness, resistance, soundType);
    }

    @Override
    protected ItemStack getSilkTouchDrop(IBlockState state) {
        return new ItemStack(ItemsTC.voidSeed, 2);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return ItemsTC.voidSeed;
    }

    @Override
    public int quantityDroppedWithBonus(int fortune, Random random) {
        if (fortune == 0) {
            return this.quantityDropped(random);
        }

        int i = random.nextInt(fortune + 2) - 1;
        if (i < 0) i = 0;

        return this.quantityDropped(random) * (i + 1);
    }

    @Override
    public int quantityDropped(Random random) {
        return 2 + random.nextInt(2);
    }
}
