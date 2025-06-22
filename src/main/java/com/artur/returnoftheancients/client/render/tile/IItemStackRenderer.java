package com.artur.returnoftheancients.client.render.tile;

import net.minecraft.item.ItemStack;

public interface IItemStackRenderer {
    void renderByItem(ItemStack stack, float partialTicks);
}
