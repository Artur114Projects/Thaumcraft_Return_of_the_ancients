package com.artur.returnoftheancients.client.render.item;

import net.minecraft.item.ItemStack;

public interface IItemStackRenderer {
    void renderByItem(ItemStack stack, float partialTicks);
}
