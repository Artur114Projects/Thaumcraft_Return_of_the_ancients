package com.artur.returnoftheancients.blocks;

import com.artur.returnoftheancients.client.render.tile.TileEntityPhantomPedestalRender;
import com.artur.returnoftheancients.tileentity.BlockTileEntity;
import com.artur.returnoftheancients.tileentity.TileEntityPhantomPedestal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockPhantomPedestal extends BlockTileEntity<TileEntityPhantomPedestal> {
    public BlockPhantomPedestal(String name) {
        super(name, Material.GLASS, 2.0F, 10.0F, SoundType.GLASS);

        this.bindTESR(new TileEntityPhantomPedestalRender());
        this.setNotFillAndOpaqueCube();
        this.setTRACreativeTab();
        this.setForCreative();
    }

    @Override
    public Class<TileEntityPhantomPedestal> getTileEntityClass() {
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
