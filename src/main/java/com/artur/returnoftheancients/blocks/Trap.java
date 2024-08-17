package com.artur.returnoftheancients.blocks;

import com.artur.returnoftheancients.tileentity.TileEntityFireTrap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Trap {


    public Class<TileEntityFireTrap> getTileEntityClass() {
        return TileEntityFireTrap.class;
    }

    public @Nullable TileEntityFireTrap createTileEntity(@NotNull World world, @NotNull IBlockState blockState) {
        return new TileEntityFireTrap();
    }
}
