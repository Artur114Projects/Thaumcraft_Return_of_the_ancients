package com.artur.returnoftheancients.client.render.item;

import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class TileEntityItemStackRendererTRA extends TileEntityItemStackRenderer {
    public static final TileEntityItemStackRendererTRA INSTANCE = new TileEntityItemStackRendererTRA();
    private final Map<Item, IItemStackRenderer> RENDERERS = new HashMap<>();

    @Override
    public void renderByItem(ItemStack stack, float partialTicks) {
        IItemStackRenderer renderer = RENDERERS.get(stack.getItem());

        if (renderer != null) {
            renderer.renderByItem(stack, partialTicks);
        } else {
            super.renderByItem(stack, partialTicks);
        }
    }

    public void register(Item item, IItemStackRenderer renderer) {
        RENDERERS.put(item, renderer);
    }
}
