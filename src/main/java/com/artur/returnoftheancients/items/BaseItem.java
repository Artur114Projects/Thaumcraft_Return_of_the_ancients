package com.artur.returnoftheancients.items;

import com.artur.returnoftheancients.init.InitItems;
import com.artur.returnoftheancients.main.Main;
import com.artur.returnoftheancients.utils.interfaces.IHasModel;
import net.minecraft.item.Item;

public abstract class BaseItem extends Item implements IHasModel {

    protected BaseItem(String name) {
        this.setRegistryName(name);
        this.setUnlocalizedName(name);

        InitItems.ITEMS.add(this);
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
