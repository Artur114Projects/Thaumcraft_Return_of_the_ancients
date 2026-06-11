package com.artur114.thaumrota.common.blocks;

import com.artur114.thaumrota.client.render.tile.TileEntityAncientProjectorRender;
import com.artur114.thaumrota.common.tileentity.TileEntityAncientProjector;
import com.artur114.thaumrota.main.ThaumRotA;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockAncientProjector extends BaseBlockTileEntity<TileEntityAncientProjector> {
    public BlockAncientProjector(String name) {
        super(name, MaterialArrays.ANCIENT_STONE_ARRAY);

        this.setTileRender(new TileEntityAncientProjectorRender());
        this.setCreativeTab(ThaumRotA.CREATIVE_TAB);
        this.setForCreative();
    }

    @Override
    public Class<TileEntityAncientProjector> tileClass() {
        return TileEntityAncientProjector.class;
    }

    @Override
    public @Nullable TileEntityAncientProjector createTileEntity(@NotNull World world, @NotNull IBlockState blockState) {
        return new TileEntityAncientProjector();
    }
}
