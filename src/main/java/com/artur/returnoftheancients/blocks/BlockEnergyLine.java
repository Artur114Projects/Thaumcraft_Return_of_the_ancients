package com.artur.returnoftheancients.blocks;


import com.artur.returnoftheancients.client.render.tile.TileEntityEnergyLineRenderer;
import com.artur.returnoftheancients.energy.bases.block.BlockEnergyBase;
import com.artur.returnoftheancients.energy.bases.tile.ITileEnergy;
import com.artur.returnoftheancients.init.InitItems;
import com.artur.returnoftheancients.main.MainR;
import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.tileentity.TileEntityEnergyLine;
import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlockEnergyLine extends BlockEnergyBase<TileEntityEnergyLine> {
    public static final PropertyBool DOWN = PropertyBool.create("down");
    public static final PropertyBool UP = PropertyBool.create("up");
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");
    public static final PropertyBool EAST = PropertyBool.create("east");


    public BlockEnergyLine(String name, Material material, float hardness, float resistance, SoundType soundType) {
        super(name, material, hardness, resistance, soundType);
        InitItems.ITEMS.remove(item);
        item = new ItemBlockEnergyLine(this).setRegistryName(this.getRegistryName());
        InitItems.ITEMS.add(item);

        this.setForCreative().setTRACreativeTab();
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(world, pos, neighbor);

        this.getTileAndCallRunnable((World) world, pos, TileEntityEnergyLine::notifyAboutNeighborChanged);
    }

    @Override
    public boolean getWeakChanges(IBlockAccess world, BlockPos pos) {
        return true;
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean isActualState) {
        addCollisionBoxToList(pos, entityBox, collidingBoxes, new AxisAlignedBB(0.377, 0.377, 0.377, 0.623D, 0.623D, 0.623D));
        if (canConnectTo(world, pos, EnumFacing.DOWN)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes,
                    new AxisAlignedBB(0.377, 0, 0.377, 0.623D, 0.623D, 0.623D));
        }
        if (canConnectTo(world, pos, EnumFacing.UP)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes,
                    new AxisAlignedBB(0.377, 0.377, 0.377, 0.623D, 1, 0.623D));
        }
        if (canConnectTo(world, pos, EnumFacing.NORTH)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes,
                    new AxisAlignedBB(0.377, 0.377, 0, 0.623D, 0.623D, 0.623D));
        }
        if (canConnectTo(world, pos, EnumFacing.SOUTH)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes,
                    new AxisAlignedBB(0.377, 0.377, 0.377, 0.623D, 0.623D, 1));
        }
        if (canConnectTo(world, pos, EnumFacing.EAST)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes,
                    new AxisAlignedBB(0.377, 0.377, 0.377, 1, 0.623D, 0.623D));
        }
        if (canConnectTo(world, pos, EnumFacing.WEST)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes,
                    new AxisAlignedBB(0, 0.377, 0.377, 0.623D, 0.623D, 0.623D));
        }
    }

    @Override
    public @NotNull AxisAlignedBB getBoundingBox(@NotNull IBlockState state, @NotNull IBlockAccess world, @NotNull BlockPos pos) {
        double[] sideBound = new double[6];
        sideBound[0]= sideBound[1]= sideBound[2]=0.377D;
        sideBound[3]= sideBound[4]= sideBound[5]=0.6231D;

        if (canConnectTo(world, pos, EnumFacing.DOWN)) {
            sideBound[1]=0;
        }
        if (canConnectTo(world, pos, EnumFacing.UP)) {
            sideBound[4]=1;
        }
        if (canConnectTo(world, pos, EnumFacing.NORTH)) {
            sideBound[2]=0;
        }
        if (canConnectTo(world, pos, EnumFacing.SOUTH)) {
            sideBound[5]=1;
        }
        if (canConnectTo(world, pos, EnumFacing.WEST)) {
            sideBound[0]=0;
        }
        if (canConnectTo(world, pos, EnumFacing.EAST)) {
            sideBound[3]=1;
        }

        return new AxisAlignedBB(sideBound[0], sideBound[1], sideBound[2], sideBound[3], sideBound[4], sideBound[5]);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, UP, DOWN, NORTH, SOUTH, WEST, EAST);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state
                .withProperty(UP, this.canConnectTo(world, pos, EnumFacing.UP))
                .withProperty(DOWN, this.canConnectTo(world, pos, EnumFacing.DOWN))
                .withProperty(WEST, this.canConnectTo(world, pos, EnumFacing.WEST))
                .withProperty(EAST, this.canConnectTo(world, pos, EnumFacing.EAST))
                .withProperty(NORTH, this.canConnectTo(world, pos, EnumFacing.NORTH))
                .withProperty(SOUTH, this.canConnectTo(world, pos, EnumFacing.SOUTH));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
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
    public Class<TileEntityEnergyLine> getTileEntityClass() {
        return TileEntityEnergyLine.class;
    }

    @Override
    public @Nullable TileEntityEnergyLine createTileEntity(@NotNull World world, @NotNull IBlockState blockState) {
        return new TileEntityEnergyLine();
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!TRAConfigs.Any.debugMode) {
            return false;
        }

        TileEntity tileRaw = worldIn.getTileEntity(pos);
        if (!(tileRaw instanceof ITileEnergy)) {
            return false;
        }

        ITileEnergy tile = (ITileEnergy) tileRaw;
        playerIn.sendMessage(new TextComponentString("Network:" + tile.networkId() + " is client:" + worldIn.isRemote));
        return true;
    }

    @Override
    public void registerModels() {
        super.registerModels();
        ClientRegistry.bindTileEntitySpecialRenderer(this.getTileEntityClass(), new TileEntityEnergyLineRenderer());
    }

    private boolean canConnectTo(IBlockAccess world, BlockPos pos, EnumFacing facing){
        TileEntity tileOffset = world.getTileEntity(pos.offset(facing));
        TileEntity tile = world.getTileEntity(pos);
        return tileOffset instanceof ITileEnergy && ((ITileEnergy) (tileOffset)).canConnect(facing.getOpposite()) && tile instanceof ITileEnergy && ((ITileEnergy) (tile)).canConnect(facing);
    }

    private static class ItemBlockEnergyLine extends ItemBlock {

        public ItemBlockEnergyLine(Block block) {
            super(block);
        }

        @Override
        public @NotNull IRarity getForgeRarity(@NotNull ItemStack stack) {
            return EnumRarity.UNCOMMON;
        }
    }
}
