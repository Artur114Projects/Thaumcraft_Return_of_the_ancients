package com.artur.returnoftheancients.containers;

import com.artur.returnoftheancients.main.MainR;
import com.artur.returnoftheancients.network.ServerPacketSyncContainerHideSlots;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public abstract class ContainerWithPages extends Container {
    protected final Map<Integer, ShellForSlot> customSlots = new HashMap<>();
    protected boolean isAddSlotToCustomInventory = false;
    protected IContainerWithPages tileC;

    public ContainerWithPages(IContainerWithPages tileC) {
        this.tileC = tileC;
        tileC.setContainer(this);
    }

    public void setAddSlotsToPlayerInventory() {
        isAddSlotToCustomInventory = false;
    }

    public void setAddSlotsToCustomInventory() {
        isAddSlotToCustomInventory = true;
    }

    protected void onSlotsStateChange(List<Integer> hide, List<Integer> notHide) {
        if (tileC.isRemote()) {
            MainR.NETWORK.sendToServer(new ServerPacketSyncContainerHideSlots(tileC, hide, notHide));
        }
    }

    public void hideSlots(List<Integer> slots) {
        for (int id : slots) {
            customSlots.get(id).hide();
        }
        onSlotsStateChange(slots, new ArrayList<>());
    }

    public void unHideSlots(List<Integer> slots) {
        for (int id : slots) {
            customSlots.get(id).unHide();
        }
        onSlotsStateChange(new ArrayList<>(), slots);
    }

    public void hideAll() {
        hideSlots(new ArrayList<>(customSlots.keySet()));
    }

    public void unHideAll() {
        unHideSlots(new ArrayList<>(customSlots.keySet()));
    }

    public void setVisible(int... slots) {
        List<Integer> slotsListUnHide = new ArrayList<>();
        for (int i = 0; i != slots.length; i++) {
            slotsListUnHide.add(slots[i]);
        }
        unHideSlots(slotsListUnHide);

        List<Integer> slotsListHide = new ArrayList<>(customSlots.keySet());
        slotsListHide.removeAll(slotsListUnHide);

        hideSlots(slotsListHide);
    }

    public void setNotVisible(int... slots) {
        List<Integer> slotsListHide = new ArrayList<>();
        for (int i = 0; i != slots.length; i++) {
            slotsListHide.add(slots[i]);
        }
        hideSlots(slotsListHide);

        List<Integer> slotsListUnHide = new ArrayList<>(customSlots.keySet());
        slotsListUnHide.removeAll(slotsListHide);

        unHideSlots(slotsListUnHide);
    }

    public boolean isHide(int index) {
        ShellForSlot shellSlot = null;
        for (ShellForSlot slot : customSlots.values()) {
            if (slot.slot.slotNumber == index) {
                shellSlot = slot;
            }
        }
        if (shellSlot == null) {
            return false;
        }
        return shellSlot.isHide();
    }

    @Override
    protected @NotNull Slot addSlotToContainer(@NotNull Slot slotIn) {
        Slot res = super.addSlotToContainer(slotIn);
        if (isAddSlotToCustomInventory) {
            ShellForSlot slot = new ShellForSlot(slotIn);
            customSlots.put(slotIn.getSlotIndex(), slot);
            slot.hide();
        }
        return res;
    }

    @Override
    protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
        boolean flag = false;
        int i = startIndex;

        if (reverseDirection)
        {
            i = endIndex - 1;
        }

        if (stack.isStackable())
        {
            while (!stack.isEmpty())
            {
                if (reverseDirection)
                {
                    if (i < startIndex)
                    {
                        break;
                    }
                }
                else if (i >= endIndex)
                {
                    break;
                }

                Slot slot = this.inventorySlots.get(i);
                ItemStack itemstack = slot.getStack();

                if (!itemstack.isEmpty() && itemstack.getItem() == stack.getItem() && (!stack.getHasSubtypes() || stack.getMetadata() == itemstack.getMetadata()) && ItemStack.areItemStackTagsEqual(stack, itemstack) && !isHide(i))
                {
                    int j = itemstack.getCount() + stack.getCount();
                    int maxSize = Math.min(slot.getSlotStackLimit(), stack.getMaxStackSize());

                    if (j <= maxSize)
                    {
                        stack.setCount(0);
                        itemstack.setCount(j);
                        slot.onSlotChanged();
                        flag = true;
                    }
                    else if (itemstack.getCount() < maxSize)
                    {
                        stack.shrink(maxSize - itemstack.getCount());
                        itemstack.setCount(maxSize);
                        slot.onSlotChanged();
                        flag = true;
                    }
                }

                if (reverseDirection)
                {
                    --i;
                }
                else
                {
                    ++i;
                }
            }
        }

        if (!stack.isEmpty())
        {
            if (reverseDirection)
            {
                i = endIndex - 1;
            }
            else
            {
                i = startIndex;
            }

            while (true)
            {
                if (reverseDirection)
                {
                    if (i < startIndex)
                    {
                        break;
                    }
                }
                else if (i >= endIndex)
                {
                    break;
                }

                Slot slot1 = this.inventorySlots.get(i);
                ItemStack itemstack1 = slot1.getStack();

                if (itemstack1.isEmpty() && slot1.isItemValid(stack) && !isHide(i))
                {
                    if (stack.getCount() > slot1.getSlotStackLimit())
                    {
                        slot1.putStack(stack.splitStack(slot1.getSlotStackLimit()));
                    }
                    else
                    {
                        slot1.putStack(stack.splitStack(stack.getCount()));
                    }

                    slot1.onSlotChanged();
                    flag = true;
                    break;
                }

                if (reverseDirection)
                {
                    --i;
                }
                else
                {
                    ++i;
                }
            }
        }

        return flag;
    }

    protected static class ShellForSlot {

        protected final Slot slot;
        private final int originalX;

        private ShellForSlot(Slot slot) {
            this.originalX = slot.xPos;
            this.slot = slot;
        }

        protected void hide() {
            slot.xPos = Integer.MAX_VALUE;
        }

        protected void unHide() {
            slot.xPos = originalX;
        }

        protected boolean isHide() {
            return slot.xPos == Integer.MAX_VALUE;
        }
    }
}
