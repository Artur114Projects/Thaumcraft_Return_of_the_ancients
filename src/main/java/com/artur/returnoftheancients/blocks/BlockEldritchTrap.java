package com.artur.returnoftheancients.blocks;

import com.artur.returnoftheancients.tileentity.BlockTileEntity;
import com.artur.returnoftheancients.tileentity.TileEntityEldritchTrap;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class BlockEldritchTrap extends BlockTileEntity<TileEntityEldritchTrap> {


    public BlockEldritchTrap(String name, Material material, float hardness, float resistance, SoundType soundType) {
        super(name, material, hardness, resistance, soundType);
    }

    @Override
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        super.randomDisplayTick(state, world, pos, rand);
        if (world.isRemote) {
            for (int i = 0; i < 2; i++) {
                world.spawnParticle(
                        EnumParticleTypes.PORTAL,
                        pos.getX() - 0.5 + rand.nextDouble(),
                        pos.getY() + rand.nextDouble(),
                        pos.getZ() - 0.5 + rand.nextDouble(),
                        0.0D, 0.0D, 0.0D
                );
            }
        }
    }

    public Class<TileEntityEldritchTrap> getTileEntityClass() {
        return TileEntityEldritchTrap.class;
    }

    public @Nullable TileEntityEldritchTrap createTileEntity(@NotNull World world, @NotNull IBlockState blockState) {
        return new TileEntityEldritchTrap();
    }
}
