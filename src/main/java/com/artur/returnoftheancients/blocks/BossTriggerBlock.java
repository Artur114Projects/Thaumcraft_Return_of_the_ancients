package com.artur.returnoftheancients.blocks;

import com.artur.returnoftheancients.ancientworldgeneration.main.AncientWorld;
import com.artur.returnoftheancients.init.InitDimensions;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class BossTriggerBlock extends BaseBlock {

    public BossTriggerBlock(String name, Material material, float hardness, float resistanse, SoundType soundType) {
        super(name, material, hardness, resistanse, soundType);
    }

    @Override
    public void onBlockAdded(World worldIn, @NotNull BlockPos pos, @NotNull IBlockState state) {
        if (!worldIn.isRemote) {
            if (worldIn.provider.getDimension() == InitDimensions.ancient_world_dim_id) {
                if (((int) (pos.getX() + 300) / 10000L) > 0) {
                    AncientWorld.onBossTriggerBlockAdd((pos.getX() + 300) / 10000, pos);
                }
            }
        }
    }
}
