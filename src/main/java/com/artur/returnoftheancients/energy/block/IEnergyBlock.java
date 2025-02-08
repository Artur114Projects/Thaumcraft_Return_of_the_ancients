package com.artur.returnoftheancients.energy.block;

import com.artur.returnoftheancients.energy.intefaces.ITileEnergy;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public interface IEnergyBlock {

    default void calculateNeighborChange(World world, BlockPos pos) {
//        List<ITileEnergy> tileEnergies = EnergySystemsProvider.getNeighbors(world, pos);
//        for (ITileEnergy energy : tileEnergies) {
//            if (energy.getNetworkId() == -1) EnergySystemsProvider.onBlockAdded(world, energy);
//        }
    }

    default boolean canConnectTo(IBlockAccess world, BlockPos pos, EnumFacing facing){
        TileEntity tileOffset = world.getTileEntity(pos.offset(facing));
        TileEntity tile = world.getTileEntity(pos);
        return tileOffset instanceof ITileEnergy && ((ITileEnergy) (tileOffset)).isCanConnect(facing.getOpposite()) && tile instanceof ITileEnergy && ((ITileEnergy) (tile)).isCanConnect(facing);
    }
}
