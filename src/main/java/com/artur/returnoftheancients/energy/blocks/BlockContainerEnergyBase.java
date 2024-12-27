package com.artur.returnoftheancients.energy.blocks;

import com.artur.returnoftheancients.blocks.BaseBlockContainer;
import com.artur.returnoftheancients.energy.EnergySystem;
import com.artur.returnoftheancients.energy.EnergySystemsProvider;
import com.artur.returnoftheancients.energy.intefaces.ITileEnergy;
import com.artur.returnoftheancients.energy.intefaces.ITileEnergyProvider;
import com.artur.returnoftheancients.misc.TRAConfigs;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public abstract class BlockContainerEnergyBase<T extends TileEntity> extends BaseBlockContainer<T> implements IEnergyBlock {
    protected BlockContainerEnergyBase(String name, Material material, float hardness, float resistance, SoundType soundType) {
        super(name, material, hardness, resistance, soundType);
    }


    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            EnergySystemsProvider.onBlockDestroyed(worldIn, pos);
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tile = createTileEntity(worldIn, state);
        if (tile instanceof ITileEnergyProvider) {
            ((ITileEnergyProvider) tile).setAdding();
        }
        worldIn.setTileEntity(pos, tile);
        if (!worldIn.isRemote) {
            EnergySystemsProvider.onBlockAdded(worldIn, (ITileEnergy) tile);
        }
        super.onBlockAdded(worldIn, pos, state);
    }
}
