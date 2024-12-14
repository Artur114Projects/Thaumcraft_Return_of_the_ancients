package com.artur.returnoftheancients.containers;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public abstract class ContainerWithPages extends Container {
    protected final Map<Integer, ShellForSlot> customSlots = new HashMap<>();
    protected boolean isAddSlotToCustomInventory = false;

    public void setAddSlotsToPlayerInventory() {
        isAddSlotToCustomInventory = false;
    }

    public void setAddSlotsToCustomInventory() {
        isAddSlotToCustomInventory = true;
    }

    public void hideSlots(List<Integer> slots) {
        for (int id : slots) {
            customSlots.get(id).hide();
        }
    }

    public void hideSlots(int... slots) {
        for (int id : slots) {
            customSlots.get(id).hide();
        }
    }


    public void unHideSlots(int... slots) {
        for (int id : slots) {
            customSlots.get(id).unHide();
        }
    }

    public void unHideSlots(List<Integer> slots) {
        for (int id : slots) {
            customSlots.get(id).unHide();
        }
    }

    public void hideAll() {
        for (ShellForSlot slot : customSlots.values()) {
            slot.hide();
        }
    }

    public void unHideAll() {
        for (ShellForSlot slot : customSlots.values()) {
            slot.unHide();
        }
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

    public void setVisible(int... slots) {
        for (ShellForSlot slot : customSlots.values()) {
            if (Arrays.stream(slots).anyMatch(id -> Objects.equals(id, slot.slot.getSlotIndex()))) {
                slot.unHide();
            } else {
                slot.hide();
            }
        }
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
