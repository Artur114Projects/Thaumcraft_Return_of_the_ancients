package com.artur.returnoftheancients.blocks;

import com.artur.returnoftheancients.client.render.tile.TileEntityAncientSanctuaryControllerRenderer;
import com.artur.returnoftheancients.tileentity.BlockTileEntity;
import com.artur.returnoftheancients.tileentity.TileEntityAncientSanctuaryController;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockAncientSanctuaryController extends BlockTileEntity<TileEntityAncientSanctuaryController> {
    public BlockAncientSanctuaryController(String name, Material material, float hardness, float resistance, SoundType soundType) {
        super(name, material, hardness, resistance, soundType);
    }

    @Override
    public Class<TileEntityAncientSanctuaryController> getTileEntityClass() {
        return TileEntityAncientSanctuaryController.class;
    }

    @Override
    public @Nullable TileEntityAncientSanctuaryController createTileEntity(@NotNull World world, @NotNull IBlockState blockState) {
        return new TileEntityAncientSanctuaryController();
    }

    @Override
    public void registerModels() {
        super.registerModels();
        ClientRegistry.bindTileEntitySpecialRenderer(getTileEntityClass(), new TileEntityAncientSanctuaryControllerRenderer());
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }
}
