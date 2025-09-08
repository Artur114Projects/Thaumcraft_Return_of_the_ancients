package com.artur.returnoftheancients.blocks;

import com.artur.returnoftheancients.client.render.tile.TileEntityIncineratorRender;
import com.artur.returnoftheancients.tileentity.BlockTileEntity;
import com.artur.returnoftheancients.tileentity.TileEntityIncinerator;
import com.artur.returnoftheancients.util.MaterialArray;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlockIncinerator extends BlockTileEntity<TileEntityIncinerator> {

    public BlockIncinerator(String name) {
        super(name, MaterialArray.ANCIENT_STONE_ARRAY);

        this.bindTESR(new TileEntityIncineratorRender());
        this.setNotFillAndOpaqueCube();
        this.setTRACreativeTab();
        this.setForCreative();
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
        Block.addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.0 / 16.0, 0.0 / 16.0, 0.0 / 16.0, 16.0 / 16.0, 2.0 / 16.0, 16.0 / 16.0));
        Block.addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(1.0 / 16.0, 2.0 / 16.0, 1.0 / 16.0, 15.0 / 16.0, 4.0 / 16.0, 15.0 / 16.0));
        Block.addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(3.0 / 16.0, 4.0 / 16.0, 3.0 / 16.0, 13.0 / 16.0, 16.0 / 16.0, 13.0 / 16.0));
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }

    @Override
    public Class<TileEntityIncinerator> getTileEntityClass() {
        return TileEntityIncinerator.class;
    }

    @Override
    public @Nullable TileEntityIncinerator createTileEntity(@NotNull World world, @NotNull IBlockState blockState) {
        return new TileEntityIncinerator();
    }
}
