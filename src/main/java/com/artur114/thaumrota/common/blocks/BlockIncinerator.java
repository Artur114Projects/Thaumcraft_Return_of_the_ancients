package com.artur114.thaumrota.common.blocks;

import com.artur114.thaumrota.client.render.tile.TileEntityIncineratorRender;
import com.artur114.thaumrota.common.tileentity.BaseBlockTileEntity;
import com.artur114.thaumrota.common.tileentity.TileEntityIncinerator;
import com.artur114.thaumrota.common.util.MaterialArray;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockIncinerator extends BaseBlockTileEntity<TileEntityIncinerator> {

    public BlockIncinerator(String name) {
        super(name, MaterialArray.ANCIENT_STONE_ARRAY);

        this.setRenderer(new TileEntityIncineratorRender());
        this.setNotFillAndOpaqueCube();
        this.setTRACreativeTab();
        this.setForCreative();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }

    @Override
    public Class<TileEntityIncinerator> tileEntityClass() {
        return TileEntityIncinerator.class;
    }

    @Override
    public @Nullable TileEntityIncinerator createTileEntity(@NotNull World world, @NotNull IBlockState blockState) {
        return new TileEntityIncinerator();
    }
}
