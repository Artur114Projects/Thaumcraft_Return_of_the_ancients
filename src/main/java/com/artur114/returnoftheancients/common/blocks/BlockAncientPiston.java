package com.artur114.returnoftheancients.common.blocks;

import com.artur114.returnoftheancients.client.render.tile.TileEntityAncientPistonRender;
import com.artur114.returnoftheancients.common.tileentity.BaseBlockTileEntity;
import com.artur114.returnoftheancients.common.tileentity.TileEntityAncientPiston;
import com.artur114.returnoftheancients.common.util.MaterialArray;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockAncientPiston extends BaseBlockTileEntity<TileEntityAncientPiston> {
    public BlockAncientPiston(String name) {
        super(name, MaterialArray.ANCIENT_STONE_ARRAY);

        this.setNotFillAndOpaqueCube();
        this.setTRACreativeTab();
        this.setForCreative();

        this.setRenderer(new TileEntityAncientPistonRender());
    }

    @Override
    public Class<TileEntityAncientPiston> tileEntityClass() {
        return TileEntityAncientPiston.class;
    }

    @Override
    public @Nullable TileEntityAncientPiston createTileEntity(@NotNull World world, @NotNull IBlockState blockState) {
        return new TileEntityAncientPiston();
    }
}
