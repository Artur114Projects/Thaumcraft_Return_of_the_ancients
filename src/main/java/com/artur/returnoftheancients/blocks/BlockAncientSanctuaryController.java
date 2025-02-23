package com.artur.returnoftheancients.blocks;

import com.artur.returnoftheancients.client.render.tile.TileEntityAncientSanctuaryControllerRenderer;
import com.artur.returnoftheancients.init.InitItems;
import com.artur.returnoftheancients.main.MainR;
import com.artur.returnoftheancients.tileentity.BlockTileEntity;
import com.artur.returnoftheancients.tileentity.TileEntityAncientSanctuaryController;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlockAncientSanctuaryController extends BlockTileEntity<TileEntityAncientSanctuaryController> {
    protected static final AxisAlignedBB BASE_ABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 1.0D, 0.9375D);

    public BlockAncientSanctuaryController(String name, Material material, float hardness, float resistance, SoundType soundType) {
        super(name, material, hardness, resistance, soundType);

        this.setCreativeTab(MainR.RETURN_OF_ANCIENTS_TAB);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BASE_ABB;
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return BASE_ABB;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity tileRaw = worldIn.getTileEntity(pos);

        if (tileRaw instanceof TileEntityAncientSanctuaryController) {
            TileEntityAncientSanctuaryController tile = (TileEntityAncientSanctuaryController) tileRaw;
            if (!tile.hasItem()) {
                ItemStack stack = playerIn.getHeldItem(hand);
                if (stack.getItem() == InitItems.IMITATION_ANCIENT_FUSE) {
                    if (tile.isOpen()) {
                        tile.setHasItem(true);
                        tile.close();
                        stack.shrink(1);
                        return true;
                    }
                }
            } else {
                ItemStack stack = playerIn.getHeldItem(hand);
                if (stack.isEmpty()) {
                    if (tile.isClose()) {
                        tile.open();
                        return true;
                    } else if (tile.isOpen()){
                        tile.setHasItem(false);
                        playerIn.addItemStackToInventory(new ItemStack(InitItems.IMITATION_ANCIENT_FUSE));
                        return true;
                    }
                }
            }
        }

        return false;
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

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        this.addForCreativeOnlyTooltip(tooltip);
    }
}
