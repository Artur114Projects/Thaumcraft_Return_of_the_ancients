package com.artur114.thaumrota.common.items;

import com.artur114.bananalib.mc.registry.data.ModelRegData;
import com.artur114.bananalib.mc.registry.interf.IHasModel;
import com.artur114.thaumrota.common.config.RotAConfigs;
import com.artur114.thaumrota.main.ThaumRotA;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.*;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class ItemPrimalBlade extends ItemAxe implements IHasModel {

    public ItemPrimalBlade(String name, Item.ToolMaterial e) {
        super(e, RotAConfigs.Any.primalBladeDamage - 1, (float) RotAConfigs.Any.primalBladeSpeed);
        this.setRegistryName(name);
        this.setUnlocalizedName(name);
        this.setMaxStackSize(1);
        this.setCreativeTab(ThaumRotA.CREATIVE_TAB);
        this.setNoRepair();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (GuiScreen.isShiftKeyDown()) {
            tooltip.add(TextFormatting.WHITE + I18n.format("item.itemprimalblade.info.1"));
        } else {
            tooltip.add(TextFormatting.GRAY + I18n.format("thaumrota.info.p1") + " " + TextFormatting.WHITE + "[Shift]" + TextFormatting.GRAY + " " + I18n.format("thaumrota.info.p2"));
        }
    }

    @Override
    public @NotNull EnumRarity getRarity(@NotNull ItemStack stack) {
        return EnumRarity.EPIC;
    }

    @Override
    public List<ModelRegData> registerModelsData() {
        return Collections.singletonList(ModelRegData.inventory(this, 0));
    }
}
