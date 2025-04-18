package com.artur.returnoftheancients.energy.bases.block;


import com.artur.returnoftheancients.capabilities.TRACapabilities;
import com.artur.returnoftheancients.energy.bases.tile.ITileEnergy;
import com.artur.returnoftheancients.energy.system.EnergySystemsManager;
import com.artur.returnoftheancients.tileentity.BlockTileEntity;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockEnergyBase<T extends TileEntity> extends BlockTileEntity<T> {
    protected BlockEnergyBase(String name, Material material, float hardness, float resistance, SoundType soundType) {
        super(name, material, hardness, resistance, soundType);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        EnergySystemsManager manager = worldIn.getCapability(TRACapabilities.ENERGY_SYSTEMS_MANAGER, null);
        TileEntity tileRaw = worldIn.getTileEntity(pos);
        if (manager != null && tileRaw instanceof ITileEnergy) {
            manager.onBlockDestroyed((ITileEnergy) tileRaw);
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);

        worldIn.updateComparatorOutputLevel(pos, this);
    }
}
