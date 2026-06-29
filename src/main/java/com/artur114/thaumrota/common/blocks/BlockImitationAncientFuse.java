package com.artur114.thaumrota.common.blocks;

import com.artur114.bananalib.mc.BananaMC;
import com.artur114.bananalib.mc.base.MaterialArray;
import com.artur114.thaumrota.client.render.tile.TileImitationAncientFuseRenderer;
import com.artur114.thaumrota.common.init.InitItems;
import com.artur114.thaumrota.common.tileentity.TileImitationAncientFuse;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class BlockImitationAncientFuse extends BaseBlockTile<TileImitationAncientFuse> {
    private static final AxisAlignedBB BOX = BananaMC.createAABBFromPixels(5, 0, 3, 11, 4, 13);

    public BlockImitationAncientFuse(String name, Material material, MapColor mapColor, float hardness, float resistance, SoundType soundType) {
        super(name, material, mapColor, hardness, resistance, soundType);

        this.setNotFillAndOpaqueCube();
    }

    public BlockImitationAncientFuse(String name, Material material, float hardness, float resistance, SoundType soundType) {
        this(name, material, material.getMaterialMapColor(), hardness, resistance, soundType);
    }

    public BlockImitationAncientFuse(String name, MaterialArray mat) {
        this(name, mat.material(), mat.hardness(), mat.resistance(), mat.soundType());
    }

    @Override
    public boolean onBlockActivated(@NotNull World worldIn, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull EntityPlayer playerIn, @NotNull EnumHand hand, @NotNull EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (hand == EnumHand.MAIN_HAND && playerIn.getHeldItem(hand).isEmpty()) {
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
            playerIn.setHeldItem(hand, new ItemStack(InitItems.IMITATION_ANCIENT_FUSE));
            if (!worldIn.isRemote) {
                Random rand = new Random();
                playerIn.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.2F, (rand.nextFloat() - rand.nextFloat()) * 1.4F + 2.0F);
            }
            return true;
        }

        return false;
    }

    @Override
    public int quantityDropped(@NotNull Random random) {
        return 1;
    }

    @Override
    public @NotNull Item getItemDropped(@NotNull IBlockState state, @NotNull Random rand, int fortune) {
        return InitItems.IMITATION_ANCIENT_FUSE;
    }

    @Override
    public @NotNull EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected @Nullable TileEntitySpecialRenderer<TileImitationAncientFuse> createTileRender() {
        return new TileImitationAncientFuseRenderer();
    }

    @Override
    public @NotNull Class<TileImitationAncientFuse> tileClass() {
        return TileImitationAncientFuse.class;
    }

    @Override
    public @Nullable TileImitationAncientFuse createTileEntity(@NotNull World world, @NotNull IBlockState blockState) {
        return new TileImitationAncientFuse();
    }

    @Override
    public @NotNull AxisAlignedBB getBoundingBox(@NotNull IBlockState state, @NotNull IBlockAccess source, @NotNull BlockPos pos) {
        return BOX;
    }
}
