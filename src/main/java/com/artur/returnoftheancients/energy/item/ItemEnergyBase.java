package com.artur.returnoftheancients.energy.item;

import com.artur.returnoftheancients.handlers.HandlerR;
import com.artur.returnoftheancients.items.BaseItem;
import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class ItemEnergyBase extends BaseItem implements IEnergyContainerItem {
    private float maxChargingSpeed = 0;
    private float maxEnergy = 0;

    protected ItemEnergyBase(String name) {
        super(name);

        this.setMaxStackSize(1);
    }

    @Override
    public float getMaxEnergy() {
        return maxEnergy;
    }

    @Override
    public float charge(ItemStack stack, float count) {
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

    @Override
    public float discharge(ItemStack stack, float count) {
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

    @Override
    public float getMaxChargingSpeed() {
        return maxChargingSpeed;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return (this.getMaxEnergy() - this.getEnergy(stack)) / this.getMaxEnergy();
    }

    @Override
    public int getRGBDurabilityForDisplay(@NotNull ItemStack stack) {
        return MathHelper.hsvToRGB(180 / 360.0F, 24 / 100.0F, this.getEnergy(stack) / this.getMaxEnergy());
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        tooltip.add(TextFormatting.AQUA + I18n.format(Referense.MODID + ".energy.local.0") + TextFormatting.RESET + " " + HandlerR.kJToString(getEnergy(stack)) + "/" + HandlerR.kJToString(getMaxEnergy()));
    }

    protected void setMaxChargingSpeed(float maxChargingSpeed) {
        this.maxChargingSpeed = maxChargingSpeed;
    }

    protected void setMaxEnergy(float maxEnergy) {
        this.maxEnergy = maxEnergy;
    }
}
