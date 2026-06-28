package com.artur114.thaumrota.common.blocks;

import com.artur114.thaumrota.client.render.tile.TileEntityPhantomPedestalRender;
import com.artur114.thaumrota.common.tileentity.TileEntityPhantomPedestal;
import com.artur114.thaumrota.main.ThaumRotA;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockPhantomPedestal extends BaseBlockTile<TileEntityPhantomPedestal> {
    public BlockPhantomPedestal(String name) {
        super(name, Material.GLASS, 2.0F, 10.0F, SoundType.GLASS);

        this.setCreativeTab(ThaumRotA.CREATIVE_TAB);
        this.setNotFillAndOpaqueCube();
        this.setForCreative();
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected @Nullable TileEntitySpecialRenderer<TileEntityPhantomPedestal> createTileRender() {
        return new TileEntityPhantomPedestalRender();
    }

    @Override
    public Class<TileEntityPhantomPedestal> tileClass() {
        return TileEntityPhantomPedestal.class;
    }

    @Override
    public @Nullable TileEntityPhantomPedestal createTileEntity(@NotNull World world, @NotNull IBlockState blockState) {
        return new TileEntityPhantomPedestal();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }
}
