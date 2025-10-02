package com.artur.returnoftheancients.blocks;

import com.artur.returnoftheancients.client.render.tile.TileEntityAncientDoorH4x4Render;
import com.artur.returnoftheancients.tileentity.BlockTileEntity;
import com.artur.returnoftheancients.tileentity.TileEntityAncientDoorH4x4;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockAncientDoorH4x4 extends BlockTileEntity<TileEntityAncientDoorH4x4> {
    public BlockAncientDoorH4x4(String name) {
        super(name, Material.ROCK, 2.0F, 10.0F, SoundType.STONE);

        this.setRenderer(new TileEntityAncientDoorH4x4Render());
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
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!playerIn.isCreative()) {
            return false;
        }

        TileEntity tileRaw = worldIn.getTileEntity(pos);
        if (tileRaw instanceof TileEntityAncientDoorH4x4) {
            TileEntityAncientDoorH4x4 tile = ((TileEntityAncientDoorH4x4) tileRaw);

            if (tile.isOpen()) {
                tile.close();
                return true;
            }

            if (tile.isClose()) {
                tile.open();
                return true;
            }
        }

        return false;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntityAncientDoorH4x4 tile = this.createTileEntity(worldIn, state);
        if (tile != null) {
            worldIn.setTileEntity(pos, tile);
            tile.fillDummy();
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileEntityAncientDoorH4x4) {
            ((TileEntityAncientDoorH4x4) tile).onBlockBreak();
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public Class<TileEntityAncientDoorH4x4> getTileEntityClass() {
        return TileEntityAncientDoorH4x4.class;
    }

    @Override
    public @Nullable TileEntityAncientDoorH4x4 createTileEntity(@NotNull World world, @NotNull IBlockState blockState) {
        return new TileEntityAncientDoorH4x4();
    }
}