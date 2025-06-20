package com.artur.returnoftheancients.blocks;

import com.artur.returnoftheancients.client.render.tile.TileEntityPedestalActiveRender;
import com.artur.returnoftheancients.init.InitItems;
import com.artur.returnoftheancients.tileentity.BlockTileEntity;
import com.artur.returnoftheancients.tileentity.TileEntityDummy;
import com.artur.returnoftheancients.tileentity.TileEntityPedestalActive;
import com.artur.returnoftheancients.util.interfaces.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockPedestalActive extends BlockTileEntity<TileEntityPedestalActive> {
    public static final AxisAlignedBB BB_BASE = new AxisAlignedBB(1.0F / 16.0F, 0, 1.0F / 16.0F, 15.0F / 16.0F, 20.4F / 16.0F, 15.0F / 16.0F);
    public final static PropertyDirection DIRECTION = PropertyDirection.create("facing", (f) -> f.getAxis().isHorizontal());
    public final static PropertyBool ROTATE = PropertyBool.create("rotate");

    public BlockPedestalActive(String name) {
        super(name, Material.ROCK, 2.0F, 10.0F, SoundType.STONE);
        InitItems.ITEMS.remove(item);
        item = new ItemBlockPedestalActive(this).setRegistryName(this.getRegistryName());
        InitItems.ITEMS.add(item);

        this.setDefaultState(this.getDefaultState().withProperty(DIRECTION, EnumFacing.EAST).withProperty(ROTATE, false));
        this.bindTESR(new TileEntityPedestalActiveRender());
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, DIRECTION, ROTATE);
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        float rot = Math.abs((placer.rotationYaw * 4.0F / 360.0F) - ((int) (placer.rotationYaw * 4.0F / 360.0F)));
        return this.getDefaultState().withProperty(DIRECTION, placer.getHorizontalFacing().getOpposite()).withProperty(ROTATE, rot > 0.35 && rot < 0.75);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(DIRECTION, EnumFacing.getFront(meta & 7)).withProperty(ROTATE, (meta & 8) > 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i = i | state.getValue(DIRECTION).getIndex();

        if (state.getValue(ROTATE))
        {
            i |= 8;
        }

        return i;
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(DIRECTION, rot.rotate(state.getValue(DIRECTION)));
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(DIRECTION)));
    }

    @Override
    public Class<TileEntityPedestalActive> getTileEntityClass() {
        return TileEntityPedestalActive.class;
    }

    @Override
    public @Nullable TileEntityPedestalActive createTileEntity(@NotNull World world, @NotNull IBlockState blockState) {
        return new TileEntityPedestalActive();
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BB_BASE;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    private static class ItemBlockPedestalActive extends ItemBlock {

        public ItemBlockPedestalActive(Block block) {
            super(block);
        }

        @Override
        public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
            ItemStack stack = player.getHeldItem(hand);
            NBTTagCompound data = stack.getOrCreateSubCompound("parent");

            if (player.isSneaking()) {
                TileEntity tile = worldIn.getTileEntity(pos);

                if (tile instanceof TileEntityDummy) {
                    tile = ((TileEntityDummy) tile).parentTile();
                }

                if (tile instanceof TileEntityPedestalActive.ITileActivatedWithPedestal) {
                    data.setLong("pos", tile.getPos().toLong());
                    return EnumActionResult.SUCCESS;
                }
            }


            if (super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ) == EnumActionResult.SUCCESS) {
                if (data.hasKey("pos")) {
                    TileEntity tile = worldIn.getTileEntity(pos.offset(facing));
                    if (tile instanceof TileEntityPedestalActive) {
                        BlockPos parent = BlockPos.fromLong(data.getLong("pos"));
                        data.removeTag("pos");
                        ((TileEntityPedestalActive) tile).bindParent(parent);
                    }
                }
                return EnumActionResult.SUCCESS;
            }

            return EnumActionResult.FAIL;
        }

        @Override
        public int getItemEnchantability(ItemStack stack) {
            return stack.getOrCreateSubCompound("parent").hasKey("pos") ? 1 : 0;
        }
    }
}
