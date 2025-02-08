package com.artur.returnoftheancients.items;

import com.artur.returnoftheancients.init.InitItems;
import com.artur.returnoftheancients.main.MainR;
import com.artur.returnoftheancients.utils.interfaces.IHasModel;
import net.minecraft.item.Item;

public abstract class BaseItem extends Item implements IHasModel {

    protected BaseItem(String name) {
        this.setRegistryName(name);
        this.setUnlocalizedName(name);

        InitItems.ITEMS.add(this);
    }

    protected void setTRACreativeTab() {
        this.setCreativeTab(MainR.RETURN_OF_ANCIENTS_TAB);
    }

    @Override
    public void registerModels() {
        MainR.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
