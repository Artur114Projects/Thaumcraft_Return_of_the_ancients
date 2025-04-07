package com.artur.returnoftheancients.tileentity;

import com.artur.returnoftheancients.energy.bases.tile.ITileEnergy;
import com.artur.returnoftheancients.energy.bases.tile.ITileEnergyProvider;
import com.artur.returnoftheancients.energy.bases.tile.TileEnergyBase;
import com.artur.returnoftheancients.energy.util.EnergyTypes;
import com.artur.returnoftheancients.util.Tuple;
import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;

public class TileEntityEnergyLine extends TileEnergyBase implements ITickable {
    @SideOnly(Side.CLIENT)
    public final ArrayList<Tuple<Boolean, Tuple<EnumFacing, ITileEnergy>>> neighbors = new ArrayList<>(6);
    protected float maxTransferredEnergy = EnergyTypes.KILO.formatW(400);
    protected float currentTransferredEnergy = 0;
    protected float transferredEnergyBuffer = 0;
    protected float prevTransferredEnergy = 0;

    @Override
    public void update() {
        this.prevTransferredEnergy = this.currentTransferredEnergy;
        this.currentTransferredEnergy = this.transferredEnergyBuffer;
        this.transferredEnergyBuffer = 0;

        if (this.world.isRemote) {
            this.compileNeighbors();
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public boolean isEnergyLine() {
        return true;
    }

    @Override
    public boolean canConnect(EnumFacing facing) {
        return true;
    }

    @Override
    public float transferEnergy(float count) {
        this.transferredEnergyBuffer += count;
        return count * this.currentEfficiency();
    }

    public float currentTransferredEnergy() {
        return currentTransferredEnergy;
    }

    public float prevTransferredEnergy() {
        return prevTransferredEnergy;
    }

    public float maxTransferredEnergy() {
        return maxTransferredEnergy;
    }

    private float currentEfficiency() {
        return 0.9998F;
    }

    @SideOnly(Side.CLIENT)
    public void compileNeighbors() {
        this.neighbors.clear();
        for (EnumFacing facing : EnumFacing.VALUES) {
            if (this.canConnect(facing)) {
                TileEntity tileRaw = this.world.getTileEntity(this.pos.offset(facing));
                if (tileRaw instanceof ITileEnergy) {
                    ITileEnergy tile = (ITileEnergy) tileRaw;
                    if (tile.canConnect(facing.getOpposite())) {
                        this.neighbors.add(new Tuple<>(false, new Tuple<>(facing.getOpposite(), tile), false));
                    }
                }
            }
        }
        this.updateWorkingNeighbors();
    }

    @SideOnly(Side.CLIENT)
    private void updateWorkingNeighbors() {
        for (Tuple<Boolean, Tuple<EnumFacing, ITileEnergy>> neighbor : this.neighbors) {
            ITileEnergy tile = neighbor.getSecond().getSecond();

            if (tile instanceof ITileEnergyProvider) {
                neighbor.setFirst(((ITileEnergyProvider) tile).canAdd(this.maxTransferredEnergy) > 0);
            }

            if (tile instanceof TileEntityEnergyLine) {
                neighbor.setFirst(((TileEntityEnergyLine) tile).currentTransferredEnergy() > 0);
            }
        }
    }
}
