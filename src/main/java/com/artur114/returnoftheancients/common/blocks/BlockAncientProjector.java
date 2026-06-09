package com.artur114.returnoftheancients.common.blocks;

import com.artur114.returnoftheancients.client.render.tile.TileEntityAncientProjectorRender;
import com.artur114.returnoftheancients.common.tileentity.BaseBlockTileEntity;
import com.artur114.returnoftheancients.common.tileentity.TileEntityAncientProjector;
import com.artur114.returnoftheancients.common.util.MaterialArray;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockAncientProjector extends BaseBlockTileEntity<TileEntityAncientProjector> {
    public BlockAncientProjector(String name) {
        super(name, MaterialArray.ANCIENT_STONE_ARRAY);

        this.setRenderer(new TileEntityAncientProjectorRender());
        this.setTRACreativeTab();
        this.setForCreative();
    }

    @Override
    public Class<TileEntityAncientProjector> tileEntityClass() {
        return TileEntityAncientProjector.class;
    }

    @Override
    public @Nullable TileEntityAncientProjector createTileEntity(@NotNull World world, @NotNull IBlockState blockState) {
        return new TileEntityAncientProjector();
    }
}
