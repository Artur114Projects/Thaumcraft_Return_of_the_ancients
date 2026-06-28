package com.artur114.thaumrota.common.blocks;

import com.artur114.thaumrota.client.render.tile.TileEntityAncientPistonRender;
import com.artur114.thaumrota.common.tileentity.TileEntityAncientPiston;
import com.artur114.thaumrota.main.ThaumRotA;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockAncientPiston extends BaseBlockTile<TileEntityAncientPiston> {
    public BlockAncientPiston(String name) {
        super(name, MaterialArrays.ANCIENT_STONE_ARRAY);

        this.setCreativeTab(ThaumRotA.CREATIVE_TAB);
        this.setNotFillAndOpaqueCube();
        this.setForCreative();
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected @Nullable TileEntitySpecialRenderer<TileEntityAncientPiston> createTileRender() {
        return new TileEntityAncientPistonRender();
    }

    @Override
    public Class<TileEntityAncientPiston> tileClass() {
        return TileEntityAncientPiston.class;
    }

    @Override
    public @Nullable TileEntityAncientPiston createTileEntity(@NotNull World world, @NotNull IBlockState blockState) {
        return new TileEntityAncientPiston();
    }
}
