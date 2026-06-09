package com.artur114.returnoftheancients.common.items;

import com.artur114.returnoftheancients.common.init.InitItems;
import com.artur114.returnoftheancients.main.ThaumicRotA;
import com.artur114.returnoftheancients.common.util.interfaces.IHasModel;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public abstract class BaseItem extends Item implements IHasModel {

    protected BaseItem(String name) {
        this.setRegistryName(name);
        this.setUnlocalizedName(name);

        InitItems.ITEMS_REGISTER_BUSS.add(this);
    }

    protected void setTRACreativeTab() {
        this.setCreativeTab(ThaumicRotA.ROTA_CREATIVE_TAB);
    }

    protected void addForCreativeOnlyTooltip(List<String> tooltip) {
        tooltip.add(TextFormatting.RED + I18n.format("returnoftheancients.for_creative_only"));
    }

    @Override
    public void registerModels() {
        ThaumicRotA.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
