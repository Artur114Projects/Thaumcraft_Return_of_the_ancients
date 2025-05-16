package com.artur.returnoftheancients.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockAncientLamp extends BaseBlock {
    public BlockAncientLamp(String name, float light) {
        super(name, Material.ROCK, 2.0F, 10.0F, SoundType.STONE);

        this.setLightLevel(light);
    }
}
