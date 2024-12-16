package com.artur.returnoftheancients.containers.slots;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.IEssentiaContainerItem;
import thaumcraft.common.items.consumables.ItemPhial;

public class SlotItemHandlerAspectInput extends SlotItemHandlerLimitedByClass{
    public SlotItemHandlerAspectInput(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, IEssentiaContainerItem.class, 1, index, xPosition, yPosition);
    }

    @Override
    public int getItemStackLimit(@NotNull ItemStack stack) {
        ItemStack maxAdd = stack.copy();
        int maxInput = stack.getItem() instanceof ItemPhial ? stack.getMaxStackSize() : 1;
        maxAdd.setCount(maxInput);

        IItemHandler handler = this.getItemHandler();
        ItemStack currentStack = handler.getStackInSlot(index);
        if (handler instanceof IItemHandlerModifiable) {
            IItemHandlerModifiable handlerModifiable = (IItemHandlerModifiable) handler;

            handlerModifiable.setStackInSlot(index, ItemStack.EMPTY);

            ItemStack remainder = handlerModifiable.insertItem(index, maxAdd, true);

            handlerModifiable.setStackInSlot(index, currentStack);

            return maxInput - remainder.getCount();
        }
        else
        {
            ItemStack remainder = handler.insertItem(index, maxAdd, true);

            int current = currentStack.getCount();
            int added = maxInput - remainder.getCount();
            return current + added;
        }
    }
}
