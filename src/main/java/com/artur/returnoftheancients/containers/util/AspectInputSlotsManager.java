package com.artur.returnoftheancients.containers.util;

import com.artur.returnoftheancients.utils.AspectBottle;
import com.artur.returnoftheancients.utils.AspectBottlesList;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IEssentiaContainerItem;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.items.consumables.ItemPhial;

import java.util.ArrayList;

public class AspectInputSlotsManager {
    private final IItemHandlerModifiable itemHandler;
    private final AspectBottlesList bottlesList;
    private int[][] bottlesToInputSlots;
    private int[] inputSlots;

    public AspectInputSlotsManager(IItemHandlerModifiable itemHandler, AspectBottlesList bottlesList, int[] inputSlots, int[]... bottlesToInputSlots) {
        this.bottlesToInputSlots = bottlesToInputSlots;
        this.bottlesList = bottlesList;
        this.itemHandler = itemHandler;
        this.inputSlots = inputSlots;
    }

    // TODO: Добавить анимацию на клиенте
    public void fill() {
        for (int i = 0; i < inputSlots.length; i = i + 2) {
            ItemStack stack = itemHandler.getStackInSlot(inputSlots[i]);
            ItemStack outputStack = itemHandler.getStackInSlot(inputSlots[i + 1]);
            if (stack.getItem() instanceof IEssentiaContainerItem) {
                IEssentiaContainerItem containerItem = ((IEssentiaContainerItem) stack.getItem());
                AspectBottlesList bottles = bottlesList.get(bottlesToInputSlots[i / 2]);
                AspectList list = containerItem.getAspects(stack);
                if (list == null) {
                    return;
                }
                if (!list.aspects.isEmpty()) {
                    Aspect aspect = bottles.containAnyAspectOnList(new ArrayList<>(list.aspects.keySet()));
                    if (aspect != null) {
                        if (list.getAmount(aspect) > 0) {
                            if (stack.getItem() instanceof ItemPhial) {
                                AspectBottle bottle = bottles.getAspectBottleWithAspect(aspect);
                                if (bottle != null && bottle.canAdd(10)) {
                                    itemHandler.insertItem(inputSlots[i + 1], new ItemStack(ItemsTC.phial), false);
                                    itemHandler.extractItem(inputSlots[i], 1, false);
                                    bottle.addCount(10);
                                }
                                return;
                            }
                            if (bottles.add(aspect, 1) > 0) {
                                list.add(aspect, -1);
                                containerItem.setAspects(stack, list);
                            }
                        } else {
                            containerItem.setAspects(stack, new AspectList());
                            ItemStack resStack = stack.copy();
                            resStack.setCount(1);
                            if (outputStack == ItemStack.EMPTY) {
                                itemHandler.insertItem(inputSlots[i + 1], resStack, false);
                                itemHandler.setStackInSlot(inputSlots[i], ItemStack.EMPTY);
                            } else if (outputStack.isItemEqual(resStack) && outputStack.getCount() < itemHandler.getSlotLimit(inputSlots[i + 1])) {
                                itemHandler.insertItem(inputSlots[i + 1], resStack, false);
                                itemHandler.setStackInSlot(inputSlots[i], ItemStack.EMPTY);
                            }
                        }
                    }
                }
            }
        }
    }

    public void reset(int[] inputSlots, int[]... bottlesToInputSlots) {
        this.bottlesToInputSlots = bottlesToInputSlots;
        this.inputSlots = inputSlots;
    }
}
