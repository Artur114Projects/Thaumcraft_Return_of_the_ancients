package com.artur.returnoftheancients.energy.blocks;

import com.artur.returnoftheancients.energy.EnergySystem;
import com.artur.returnoftheancients.energy.EnergySystemsProvider;
import com.artur.returnoftheancients.energy.intefaces.ITileEnergy;
import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.tileentity.BlockTileEntity;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BlockEnergyBase<T extends TileEntity> extends BlockTileEntity<T> implements IEnergyBlock {
    protected BlockEnergyBase(String name, Material material, float hardness, float resistance, SoundType soundType) {
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
        worldIn.setTileEntity(pos, tile);
        if (!worldIn.isRemote) {
            EnergySystemsProvider.onBlockAdded(worldIn, (ITileEnergy) tile);
        }
        super.onBlockAdded(worldIn, pos, state);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return false;
        }

        if (!TRAConfigs.Any.debugMode) {
            return false;
        }

        TileEntity tileRaw = worldIn.getTileEntity(pos);
        if (!(tileRaw instanceof ITileEnergy)) {
            return false;
        }

        ITileEnergy tile = (ITileEnergy) tileRaw;
        if (playerIn.isSneaking()) {
            for (EnergySystem system : EnergySystemsProvider.energySystems.values()){
                playerIn.sendMessage(new TextComponentString("Network " + system.id + " " + system));
            }
        } else {
            playerIn.sendMessage(new TextComponentString("Network id " + tile.getNetworkId()));
        }
        return true;
    }
}
