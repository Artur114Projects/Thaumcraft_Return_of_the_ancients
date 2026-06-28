package com.artur114.thaumrota.common.blocks;

import com.artur114.thaumrota.client.render.tile.TileEntityAncientProjectorRender;
import com.artur114.thaumrota.common.tileentity.TileEntityAncientProjector;
import com.artur114.thaumrota.main.ThaumRotA;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockAncientProjector extends BaseBlockTile<TileEntityAncientProjector> {
    public BlockAncientProjector(String name) {
        super(name, MaterialArrays.ANCIENT_STONE_ARRAY);

        this.setForCreative();
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected @Nullable TileEntitySpecialRenderer<TileEntityAncientProjector> createTileRender() {
        return new TileEntityAncientProjectorRender();
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
