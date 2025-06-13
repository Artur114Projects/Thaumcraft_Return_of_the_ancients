package com.artur.returnoftheancients.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockAncientLamp extends BaseBlock {
    private boolean isFullCube = true;
    private boolean isOpaqueCube = true;

    public BlockAncientLamp(String name, float light) {
        super(name, Material.ROCK, 2.0F, 10.0F, SoundType.STONE);

        this.setLightLevel(light);
    }

    public BlockAncientLamp setNotFullCube() {
        this.isFullCube = false;
        return this;
    }


    public BlockAncientLamp setNotOpaqueCube() {
        this.isOpaqueCube = false;
        return this;
    }


    @Override
    public boolean isFullCube(IBlockState state) {
        return this.isFullCube;
    }


    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return this.isOpaqueCube;
    }
}
