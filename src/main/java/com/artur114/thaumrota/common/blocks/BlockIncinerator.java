package com.artur114.thaumrota.common.blocks;

import com.artur114.thaumrota.client.render.tile.TileEntityIncineratorRender;
import com.artur114.thaumrota.common.tileentity.TileEntityIncinerator;
import com.artur114.thaumrota.main.ThaumRotA;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockIncinerator extends BaseBlockTileEntity<TileEntityIncinerator> {

    public BlockIncinerator(String name) {
        super(name, MaterialArrays.ANCIENT_STONE_ARRAY);

        this.setTileRender(new TileEntityIncineratorRender());
        this.setCreativeTab(ThaumRotA.CREATIVE_TAB);
        this.setNotFillAndOpaqueCube();
        this.setForCreative();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }

    @Override
    public Class<TileEntityIncinerator> tileClass() {
        return TileEntityIncinerator.class;
    }

    @Override
    public @Nullable TileEntityIncinerator createTileEntity(@NotNull World world, @NotNull IBlockState blockState) {
        return new TileEntityIncinerator();
    }
}
