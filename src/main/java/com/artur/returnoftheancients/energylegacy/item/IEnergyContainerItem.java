package com.artur.returnoftheancients.energylegacy.item;

import net.minecraft.item.ItemStack;

public interface IEnergyContainerItem {
    float getMaxEnergy();
    float getMaxChargingSpeed();
    float charge(ItemStack stack, float count);
    float discharge(ItemStack stack, float count);
    default void setEnergy(ItemStack stack, float count) {
        stack.getOrCreateSubCompound("EnergySystem").setFloat("EnergyCount", count);
    }
    default float getEnergy(ItemStack stack) {
        return stack.getOrCreateSubCompound("EnergySystem").getFloat("EnergyCount");
    }
}
