package com.artur114.thaumrota.common.blocks;

import com.artur114.thaumrota.client.render.tile.TileEntityAncientFanRender;
import com.artur114.thaumrota.common.tileentity.TileEntityAncientFan;
import com.artur114.thaumrota.main.ThaumRotA;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockAncientFan extends BaseBlockTile<TileEntityAncientFan> {
    public BlockAncientFan(String name) {
        super(name, MaterialArrays.ANCIENT_STONE_ARRAY);

        this.setCreativeTab(ThaumRotA.CREATIVE_TAB);
        this.setForCreative();
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected @Nullable TileEntitySpecialRenderer<TileEntityAncientFan> createTileRender() {
        return new TileEntityAncientFanRender();
    }

    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileEntityAncientFan) {
            return ((TileEntityAncientFan) tile).canConnectRedstone(side);
        }
        return super.canConnectRedstone(state, world, pos, side);
    }

    @Override
    public Class<TileEntityAncientFan> tileClass() {
        return TileEntityAncientFan.class;
    }

    @Override
    public @Nullable TileEntityAncientFan createTileEntity(@NotNull World world, @NotNull IBlockState blockState) {
        return new TileEntityAncientFan();
    }
}
