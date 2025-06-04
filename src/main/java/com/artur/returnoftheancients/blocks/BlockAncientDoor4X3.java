package com.artur.returnoftheancients.blocks;

import com.artur.returnoftheancients.client.render.tile.TileEntityAncientDoor4X3Render;
import com.artur.returnoftheancients.client.render.tile.TileEntityAncientSanctuaryControllerRenderer;
import com.artur.returnoftheancients.tileentity.BlockTileEntity;
import com.artur.returnoftheancients.tileentity.TileEntityAncientDoor4X3;
import com.artur.returnoftheancients.tileentity.TileEntityDummy;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockAncientDoor4X3 extends BlockTileEntity<TileEntityAncientDoor4X3> {
    public BlockAncientDoor4X3(String name) {
        super(name, Material.ROCK, 2.0F, 10.0F, SoundType.STONE);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        TileEntityAncientDoor4X3 tile = this.createTileEntity(worldIn, state);
        if (tile != null) {
            worldIn.setTileEntity(pos, tile);
            tile.fillDummy();
        }
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }

    @Override
    public Class<TileEntityAncientDoor4X3> getTileEntityClass() {
        return TileEntityAncientDoor4X3.class;
    }

    @Override
    public void registerModels() {
        super.registerModels();
        ClientRegistry.bindTileEntitySpecialRenderer(this.getTileEntityClass(), new TileEntityAncientDoor4X3Render());
    }

    @Override
    public @Nullable TileEntityAncientDoor4X3 createTileEntity(@NotNull World world, @NotNull IBlockState blockState) {
        return new TileEntityAncientDoor4X3();
    }
}
