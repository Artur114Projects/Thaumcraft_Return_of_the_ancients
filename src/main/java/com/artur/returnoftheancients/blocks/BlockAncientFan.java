package com.artur.returnoftheancients.blocks;

import com.artur.returnoftheancients.client.render.tile.TileEntityAncientFanRender;
import com.artur.returnoftheancients.tileentity.BlockTileEntity;
import com.artur.returnoftheancients.tileentity.TileEntityAncientFan;
import com.artur.returnoftheancients.util.MaterialArray;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockAncientFan extends BlockTileEntity<TileEntityAncientFan> {
    public BlockAncientFan(String name) {
        super(name, MaterialArray.ANCIENT_STONE_ARRAY);

        this.setRenderer(new TileEntityAncientFanRender());
        this.setTRACreativeTab();
        this.setForCreative();
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
    public Class<TileEntityAncientFan> getTileEntityClass() {
        return TileEntityAncientFan.class;
    }

    @Override
    public @Nullable TileEntityAncientFan createTileEntity(@NotNull World world, @NotNull IBlockState blockState) {
        return new TileEntityAncientFan();
    }
}
