package com.artur.returnoftheancients.energy.bases.item;

import com.artur.returnoftheancients.energy.util.EnergyTypes;
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

        tooltip.add(TextFormatting.AQUA + I18n.format(Referense.MODID + ".energy.local.0") + TextFormatting.RESET + " " + EnergyTypes.kJToString(getEnergy(stack)) + "/" + EnergyTypes.kJToString(getMaxEnergy()));
    }

    protected void setMaxChargingSpeed(float maxChargingSpeed) {
        this.maxChargingSpeed = maxChargingSpeed;
    }

    protected void setMaxEnergy(float maxEnergy) {
        this.maxEnergy = maxEnergy;
    }
}
