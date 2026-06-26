package com.artur114.thaumrota.common.blocks;

import com.artur114.thaumrota.client.render.tile.TileEntityAncientPistonRender;
import com.artur114.thaumrota.common.tileentity.TileEntityAncientPiston;
import com.artur114.thaumrota.main.ThaumRotA;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockAncientPiston extends BaseBlockTileEntity<TileEntityAncientPiston> {
    public BlockAncientPiston(String name) {
        super(name, MaterialArrays.ANCIENT_STONE_ARRAY);

        this.setCreativeTab(ThaumRotA.CREATIVE_TAB);
        this.setNotFillAndOpaqueCube();
        this.setForCreative();

        this.setTileRender(new TileEntityAncientPistonRender());
    }

    @Override
    public Class<TileEntityAncientPiston> tileClass() {
        return TileEntityAncientPiston.class;
    }

    @Override
    public @Nullable TileEntityAncientPiston createTileEntity(@NotNull World world, @NotNull IBlockState blockState) {
        return new TileEntityAncientPiston();
    }
}
