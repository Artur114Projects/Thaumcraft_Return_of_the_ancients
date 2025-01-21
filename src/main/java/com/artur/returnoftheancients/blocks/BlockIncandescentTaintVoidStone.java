package com.artur.returnoftheancients.blocks;

import net.minecraft.block.BlockMagma;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockIncandescentTaintVoidStone extends BaseBlock {
    public BlockIncandescentTaintVoidStone(String name, Material material, float hardness, float resistance, SoundType soundType) {
        super(name, material, hardness, resistance, soundType);
        setLightOpacity(1);
        setLightLevel(1);
        setTickRandomly(true);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        worldIn.checkLight(pos);
    }
}
