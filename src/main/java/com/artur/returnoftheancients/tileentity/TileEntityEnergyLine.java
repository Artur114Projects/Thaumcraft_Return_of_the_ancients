package com.artur.returnoftheancients.tileentity;

import com.artur.returnoftheancients.energy.bases.tile.ITileEnergy;
import com.artur.returnoftheancients.energy.bases.tile.ITileEnergyProvider;
import com.artur.returnoftheancients.energy.bases.tile.TileEnergyBase;
import com.artur.returnoftheancients.energy.util.EnergyTypes;
import com.artur.returnoftheancients.handlers.RenderHandler;
import com.artur.returnoftheancients.util.Tuple;
import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;

public class TileEntityEnergyLine extends TileEnergyBase implements ITickable {
    @SideOnly(Side.CLIENT)
    public final ArrayList<Tuple<Boolean, Tuple<EnumFacing, ITileEnergy>>> neighbors = new ArrayList<>(6);
    protected float maxTransferredEnergy = EnergyTypes.KILO.formatW(400.0F);
    protected float currentTransferredEnergy = 0;
    protected float transferredEnergyBuffer = 0;
    @SideOnly(Side.CLIENT)
    protected float prevRenderAlpha = 0;
    @SideOnly(Side.CLIENT)
    protected float renderAlpha = 0;

    @Override
    public void update() {
        this.currentTransferredEnergy = this.transferredEnergyBuffer;
        this.transferredEnergyBuffer = 0;

        if (this.world.isRemote) {
            this.updateWorkingNeighbors();
            this.calculateAlpha();
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();

        if (this.world.isRemote) {
            this.compileNeighbors();
        }
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

    public boolean isWorking() {
        return this.currentTransferredEnergy > 0;
    }

    public float currentTransferredEnergy() {
        return currentTransferredEnergy;
    }

    public float maxTransferredEnergy() {
        return maxTransferredEnergy;
    }

    @SideOnly(Side.CLIENT)
    public float alphaForRender(float partialTick) {
        return MathHelper.clamp(RenderHandler.interpolate(this.prevRenderAlpha, this.renderAlpha, partialTick) * 0.8F + 0.2F, 0.0F, 1.0F);
    }

    @SideOnly(Side.CLIENT)
    private void calculateAlpha() {
        this.prevRenderAlpha = this.renderAlpha;
        this.renderAlpha = RenderHandler.interpolate(this.renderAlpha, this.currentTransferredEnergy / this.maxTransferredEnergy, 0.1F);
    }

    private float currentEfficiency() {
        return 0.9998F;
    }

    @SideOnly(Side.CLIENT)
    public void compileNeighbors() {
        this.neighbors.clear();
        UltraMutableBlockPos blockPos = UltraMutableBlockPos.getBlockPosFromPoll();
        for (EnumFacing facing : EnumFacing.VALUES) {
            if (this.canConnect(facing)) {
                TileEntity tileRaw = this.world.getTileEntity(blockPos.setPos(this.pos).offset(facing));
                if (tileRaw instanceof ITileEnergy) {
                    ITileEnergy tile = (ITileEnergy) tileRaw;
                    if (tile.canConnect(facing.getOpposite())) {
                        this.neighbors.add(new Tuple<>(false, new Tuple<>(facing.getOpposite(), tile), false));
                    }
                }
            }
        }
        UltraMutableBlockPos.returnBlockPosToPoll(blockPos);
        this.updateWorkingNeighbors();
    }

    @SideOnly(Side.CLIENT)
    private void updateWorkingNeighbors() {
        for (Tuple<Boolean, Tuple<EnumFacing, ITileEnergy>> neighbor : this.neighbors) {
            ITileEnergy tile = neighbor.getSecond().getSecond();

            if (tile instanceof ITileEnergyProvider) {
                neighbor.setFirst(this.isWorking());
            }

            if (tile instanceof TileEntityEnergyLine) {
                neighbor.setFirst(((TileEntityEnergyLine) tile).isWorking());
            }
        }
    }
}
