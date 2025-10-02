package com.artur.returnoftheancients.blocks;

import com.artur.returnoftheancients.client.render.tile.TileEntityAncientPistonRender;
import com.artur.returnoftheancients.tileentity.BlockTileEntity;
import com.artur.returnoftheancients.tileentity.TileEntityAncientPiston;
import com.artur.returnoftheancients.util.MaterialArray;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockAncientPiston extends BlockTileEntity<TileEntityAncientPiston> {
    public BlockAncientPiston(String name) {
        super(name, MaterialArray.ANCIENT_STONE_ARRAY);

        this.setNotFillAndOpaqueCube();
        this.setTRACreativeTab();
        this.setForCreative();

        this.setRenderer(new TileEntityAncientPistonRender());
    }

    @Override
    public Class<TileEntityAncientPiston> getTileEntityClass() {
        return TileEntityAncientPiston.class;
    }

    @Override
    public @Nullable TileEntityAncientPiston createTileEntity(@NotNull World world, @NotNull IBlockState blockState) {
        return new TileEntityAncientPiston();
    }
}
