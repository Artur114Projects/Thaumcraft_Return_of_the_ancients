package com.artur.returnoftheancients.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockRockBase extends BaseBlock {
    public BlockRockBase(String name) {
        super(name, Material.ROCK, -1, Integer.MAX_VALUE, SoundType.STONE);
    }
}
