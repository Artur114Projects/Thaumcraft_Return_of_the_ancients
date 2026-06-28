package com.artur114.thaumrota.common.blocks;

import com.artur114.thaumrota.client.render.tile.TileEntityIncineratorRender;
import com.artur114.thaumrota.common.tileentity.TileEntityIncinerator;
import com.artur114.thaumrota.main.ThaumRotA;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockIncinerator extends BaseBlockTile<TileEntityIncinerator> {

    public BlockIncinerator(String name) {
        super(name, MaterialArrays.ANCIENT_STONE_ARRAY);

        this.setCreativeTab(ThaumRotA.CREATIVE_TAB);
        this.setNotFillAndOpaqueCube();
        this.setForCreative();
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected @Nullable TileEntitySpecialRenderer<TileEntityIncinerator> createTileRender() {
        return new TileEntityIncineratorRender();
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
