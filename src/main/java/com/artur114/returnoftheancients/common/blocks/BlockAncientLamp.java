package com.artur114.returnoftheancients.common.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockAncientLamp extends BaseBlock {
    public BlockAncientLamp(String name, float light) {
        super(name, Material.ROCK, 2.0F, 10.0F, SoundType.STONE);

        this.setLightLevel(light);
    }
}
