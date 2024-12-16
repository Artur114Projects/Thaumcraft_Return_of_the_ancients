package com.artur.returnoftheancients.containers.util;

import com.artur.returnoftheancients.containers.slots.SlotItemHandlerLimitedByItemStack;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import java.util.Arrays;

public class CustomCraftingGear {

    private SlotItemHandlerLimitedByItemStack[] slotsArray;
    private final ItemStack[] requiredItems;
    private final IItemHandler itemHandler;
    private final int[] slots;


    public CustomCraftingGear(IItemHandler itemHandler, int[] slots, ItemStack[] requiredItems) {
        if (requiredItems.length != slots.length) {
            requiredItems = Arrays.copyOf(requiredItems, slots.length);
            for (int i = 0; i != requiredItems.length; i++) {
                ItemStack stack = requiredItems[i];
                if (stack == null) {
                    requiredItems[i] = ItemStack.EMPTY;
                }
            }
        }
        this.requiredItems = requiredItems;
        this.itemHandler = itemHandler;
        this.slots = slots;
    }

    public Slot[] getSlots(int[] x, int[] y) {
        SlotItemHandlerLimitedByItemStack[] slotsArray = new SlotItemHandlerLimitedByItemStack[slots.length];
        for (int i = 0; i != slots.length; i++) {
            ItemStack requiredItem = requiredItems[i];
            SlotItemHandlerLimitedByItemStack slot = new SlotItemHandlerLimitedByItemStack(itemHandler, requiredItem, requiredItem.getCount(), slots[i], x[i], y[i]);
            slotsArray[i] = slot;
        }
        this.slotsArray = slotsArray;
        return slotsArray;
    }

    public boolean isCanCraft() {
        for (SlotItemHandlerLimitedByItemStack slot : slotsArray) {
            if (!slot.isFull()) {
                return false;
            }
        }
        return true;
    }

    public void empty() {
        for (SlotItemHandlerLimitedByItemStack slot : slotsArray) {
            slot.putStack(ItemStack.EMPTY);
        }
    }

    public boolean craft() {
        if (!isCanCraft()) {
            return false;
        }
        empty();
        return true;
    }
}
