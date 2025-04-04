package com.artur.returnoftheancients.energy.bases.item;

import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.item.ItemStack;

public interface IEnergyContainerItem {
    float getMaxEnergy();
    float getMaxChargingSpeed();
    default float charge(ItemStack stack, float count) {
        float energyCount = this.getEnergy(stack);

        if (energyCount + count <= this.getMaxEnergy()) {
            energyCount += count;
            this.setEnergy(stack, energyCount);
            return count;
        } else {
            float localCount = energyCount;
            energyCount = this.getMaxEnergy();
            this.setEnergy(stack, energyCount);
            return this.getMaxEnergy() - localCount;
        }
    }
    default float discharge(ItemStack stack, float count) {
        float energyCount = this.getEnergy(stack);

        if (energyCount - count >= 0) {
            energyCount -= count;
            this.setEnergy(stack, energyCount);
            return count;
        } else {
            this.setEnergy(stack, 0);
            return energyCount;
        }
    }
    default void setEnergy(ItemStack stack, float count) {
        stack.getOrCreateSubCompound(Referense.MODID).setFloat("EnergyCount", count);
    }
    default float getEnergy(ItemStack stack) {
        return stack.getOrCreateSubCompound(Referense.MODID).getFloat("EnergyCount");
    }
}
