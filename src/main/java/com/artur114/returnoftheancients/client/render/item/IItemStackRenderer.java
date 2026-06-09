package com.artur114.returnoftheancients.client.render.item;

import net.minecraft.item.ItemStack;

public interface IItemStackRenderer {
    void renderByItem(ItemStack stack, float partialTicks);
}
