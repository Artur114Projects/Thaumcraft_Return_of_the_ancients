package com.artur114.thaumrota.client.render.item;

import net.minecraft.item.ItemStack;

public interface IItemStackRenderer {
    void renderByItem(ItemStack stack, float partialTicks);
}
