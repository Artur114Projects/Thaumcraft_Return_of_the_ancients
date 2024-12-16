package com.artur.returnoftheancients.containers.slots;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import thaumcraft.common.container.ContainerResearchTable;

public class SlotItemHandlerLimitedByClass extends SlotItemHandler {

    protected final Class<?> clazz;
    protected final int stackLimit;
    protected final int index;

    public SlotItemHandlerLimitedByClass(IItemHandler itemHandler, Class<?> clazz, int stackLimit, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.stackLimit = stackLimit;
        this.clazz = clazz;
        this.index = index;
    }

    public SlotItemHandlerLimitedByClass(IItemHandler itemHandler, Class<?> clazz, int index, int xPosition, int yPosition) {
        this(itemHandler, clazz, 64, index, xPosition, yPosition);
    }


    @Override
    public int getItemStackLimit(@NotNull ItemStack stack) {
        ItemStack maxAdd = stack.copy();
        int maxInput = Math.min(stack.getMaxStackSize(), stackLimit);
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

    @Override
    public boolean isItemValid(@NotNull ItemStack stack) {
        return super.isItemValid(stack) && this.clazz.isAssignableFrom(stack.getItem().getClass());
    }

    @Override
    public int getSlotStackLimit() {
        return stackLimit;
    }

    public boolean isFull() {
        return getHasStack() && getStack().getCount() >= stackLimit;
    }
}
