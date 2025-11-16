package com.artur.returnoftheancients.blocks;

import com.artur.returnoftheancients.client.render.tile.TileEntityAncientDoor4X3Render;
import com.artur.returnoftheancients.tileentity.BaseBlockTileEntity;
import com.artur.returnoftheancients.tileentity.TileEntityAncientDoor4X3;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockAncientDoor4X3 extends BaseBlockTileEntity<TileEntityAncientDoor4X3> {
    public BlockAncientDoor4X3(String name) {
        super(name, Material.ROCK, 2.0F, 10.0F, SoundType.STONE);

        this.setRenderer(new TileEntityAncientDoor4X3Render());
        this.setForCreative();
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
        if (tileRaw instanceof TileEntityAncientDoor4X3) {
            TileEntityAncientDoor4X3 tile = ((TileEntityAncientDoor4X3) tileRaw);

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
        TileEntityAncientDoor4X3 tile = this.createTileEntity(worldIn, state);
        if (tile != null) {
            worldIn.setTileEntity(pos, tile);
            tile.setAxis(placer.getHorizontalFacing().rotateY().getAxis());
            tile.fillDummy();
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileEntityAncientDoor4X3) {
            ((TileEntityAncientDoor4X3) tile).onBlockBreak();
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public Class<TileEntityAncientDoor4X3> tileEntityClass() {
        return TileEntityAncientDoor4X3.class;
    }

    @Override
    public @Nullable TileEntityAncientDoor4X3 createTileEntity(@NotNull World world, @NotNull IBlockState blockState) {
        return new TileEntityAncientDoor4X3();
    }

    @Override
    protected Item createItemBlock() {
        return super.createItemBlock().setMaxStackSize(1);
    }
}
