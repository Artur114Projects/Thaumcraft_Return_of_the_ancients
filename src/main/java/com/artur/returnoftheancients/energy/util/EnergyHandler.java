package com.artur.returnoftheancients.energy.util;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EnergyHandler {

    private float lastEnergyCount = 0.0F;
    public float energyCount = 0.0F;
    public final float maxOutput;
    public final float maxEnergy;
    public final float maxInput;
    private final int syncLine;


    /**
     * @param maxEnergy in kJ
     * @param maxOutput in kW
     * @param maxInput in kW
     */
    public EnergyHandler(float maxEnergy, float maxOutput, float maxInput, int syncLine) {
        this.maxOutput = maxOutput / 20.0F;
        this.maxInput = maxInput / 20.0F;
        this.maxEnergy = maxEnergy;
        this.syncLine = syncLine;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setFloat("EnergyCount", energyCount);
        return nbt;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        energyCount = nbt.getFloat("EnergyCount");
    }

    public float getMaxInputInkW() {
        return maxInput;
    }

    public float getMaxOutputInkW() {
        return maxOutput;
    }

    public boolean isNeedAdd() {
        return energyCount < maxEnergy;
    }

    public boolean isEmpty() {
        return energyCount <= 0;
    }

    public float canAdd(float count) {
        if (energyCount + count <= maxEnergy) {
            return count;
        } else {
            return maxEnergy - energyCount;
        }
    }

    public float add(float count) {
        if (energyCount + count <= maxEnergy) {
            energyCount += count;
            return count;
        } else {
            float localCount = this.energyCount;
            this.energyCount = maxEnergy;
            return maxEnergy - localCount;
        }
    }

    public float take(float count) {
        if (energyCount - count >= 0) {
            energyCount -= count;
            return count;
        } else {
            float localCount = this.energyCount;
            energyCount = 0;
            return localCount;
        }
    }

    @SideOnly(Side.CLIENT)
    public void updateOnClient(int data) {
        energyCount = data / 1000.0F;
    }

    public void sendWindowProperty(IContainerListener listener, Container container) {
        listener.sendWindowProperty(container, syncLine, (int) (energyCount * 1000));
    }

    public void detectAndSendChanges(IContainerListener listener, Container container) {
        if (lastEnergyCount != energyCount) {
            sendWindowProperty(listener, container);
        }
        lastEnergyCount = energyCount;
    }
}
