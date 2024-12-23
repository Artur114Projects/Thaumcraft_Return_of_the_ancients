package com.artur.returnoftheancients.blocks;

import com.artur.returnoftheancients.energy.blocks.BlockContainerEnergyBase;
import com.artur.returnoftheancients.main.MainR;
import com.artur.returnoftheancients.tileentity.TileEntityAncientTeleport;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.client.gui.GuiResearchTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AncientTeleport extends BlockContainerEnergyBase<TileEntityAncientTeleport> {
    private final Random rand = new Random();


    public AncientTeleport(String name, Material material, float hardness, float resistance, SoundType soundType) {
        super(name, material, hardness, resistance, soundType);
        this.setCreativeTab(MainR.ReturnOfTheAncientsTab);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public Class<TileEntityAncientTeleport> getTileEntityClass() {
        return TileEntityAncientTeleport.class;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(worldIn.isRemote) return true;

        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityAncientTeleport)
        {
            playerIn.openGui(MainR.instance, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
        return false;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (worldIn.isRemote) return;

        List<ItemStack> drops = new ArrayList<>();

        TileEntity teRaw = worldIn.getTileEntity(pos);

        if (teRaw instanceof TileEntityAncientTeleport) {
            TileEntityAncientTeleport te = (TileEntityAncientTeleport) teRaw;
            IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

            for (int i = 0; i < TileEntityAncientTeleport.SIZE; i++) {
                if (itemHandler == null) {
                    break;
                }
                ItemStack stack = itemHandler.getStackInSlot(i);
                drops.add(stack.copy());
            }
        }

        for (ItemStack drop : drops) {
            EntityItem item = new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, (ItemStack) drop);
            item.setVelocity((rand.nextDouble() - 0.5) * 0.25, rand.nextDouble() * 0.5 * 0.25, (rand.nextDouble() - 0.5) * 0.25);
            worldIn.spawnEntity(item);
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityAncientTeleport();
    }
}
