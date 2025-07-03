package com.artur.returnoftheancients.blocks;

import com.artur.returnoftheancients.client.render.tile.TileEntityAncientProjectorRender;
import com.artur.returnoftheancients.tileentity.BlockTileEntity;
import com.artur.returnoftheancients.tileentity.TileEntityAncientProjector;
import com.artur.returnoftheancients.tileentity.interf.ITileBlockPlaceListener;
import com.artur.returnoftheancients.util.MaterialArray;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockAncientProjector extends BlockTileEntity<TileEntityAncientProjector> {
    public BlockAncientProjector(String name) {
        super(name, MaterialArray.ANCIENT_STONE_ARRAY);

        this.bindTESR(new TileEntityAncientProjectorRender());
        this.setTRACreativeTab();
        this.setForCreative();
    }

    @Override
    public Class<TileEntityAncientProjector> getTileEntityClass() {
        return TileEntityAncientProjector.class;
    }

    @Override
    public @Nullable TileEntityAncientProjector createTileEntity(@NotNull World world, @NotNull IBlockState blockState) {
        return new TileEntityAncientProjector();
    }
}
