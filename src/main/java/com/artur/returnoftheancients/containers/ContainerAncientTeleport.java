package com.artur.returnoftheancients.containers;

import com.artur.returnoftheancients.containers.slots.SlotItemHandlerAspectInput;
import com.artur.returnoftheancients.containers.slots.SlotItemHandlerLimitedByClass;
import com.artur.returnoftheancients.containers.slots.SlotItemHandlerOutput;
import com.artur.returnoftheancients.tileentity.TileEntityAncientTeleport;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.aspects.IEssentiaContainerItem;
import thaumcraft.common.blocks.essentia.BlockJarItem;
import thaumcraft.common.container.slot.SlotOutput;
import thaumcraft.common.items.consumables.ItemPhial;

import java.util.HashMap;
import java.util.Map;

public class ContainerAncientTeleport extends ContainerWithPages {

    private final TileEntityAncientTeleport tile;
    private int lastIsActive;


    public ContainerAncientTeleport(IInventory playerInventory, TileEntityAncientTeleport te) {
        super(te);
        this.tile = te;


        this.addSlots();
        this.bindPlayerSlots(playerInventory);
    }

    private void bindPlayerSlots(IInventory playerInventory) {
        setAddSlotsToPlayerInventory();

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                int x = j * 18 + 69;
                int y = i * 18 + 106;
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, x, y));
            }
        }

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                int x = j * 18 + 12;
                int y = i * 18 + 106;
                this.addSlotToContainer(new Slot(playerInventory, j + i * 3, x, y));
            }
        }
    }

    private void addSlots() {
        setAddSlotsToCustomInventory();

        IItemHandler itemHandler = this.tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        int slotID = 0;
        addSlotToContainer(new SlotItemHandlerAspectInput(itemHandler, slotID++, 21, 22));
        addSlotToContainer(new SlotItemHandlerOutput(itemHandler, slotID++, 21, 58));

        addSlotToContainer(new SlotItemHandlerAspectInput(itemHandler, slotID++, 21, 22));
        addSlotToContainer(new SlotItemHandlerOutput(itemHandler, slotID++, 21, 58));

        int[] x = new int[] {166, 202, 184, 202, 166};
        int[] y = new int[] {22, 22, 40, 58, 58};

        for (Slot slot : tile.craftingGear.getSlots(x, y)) {
            addSlotToContainer(slot);
        }
    }

    @Override
    public void addListener(@NotNull IContainerListener listener) {
        super.addListener(listener);
        listener.sendWindowProperty(this, 0, tile.isActive);

        tile.energyHandler.sendWindowProperty(listener, this);
        tile.aspectBottles.addListener(listener, this);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (int i = 0; i != listeners.size(); i++) {
            IContainerListener icontainerlistener = this.listeners.get(i);

            if (lastIsActive != tile.isActive) {
                icontainerlistener.sendWindowProperty(this, 0, tile.isActive);
            }

            tile.energyHandler.detectAndSendChanges(icontainerlistener, this);
            tile.aspectBottles.detectAndSendChanges(icontainerlistener, this);

            lastIsActive = tile.isActive;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data) {
        switch (id) {
            case 0:{
                tile.isActive = data;
            } break;
            case 1:{
                tile.energyHandler.updateOnClient(data);
            } break;
        }

        if (id >= 100) {
            tile.aspectBottles.updateCount(id, data);
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {

        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {

            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < TileEntityAncientTeleport.SIZE) {

                if (!this.mergeItemStack(itemstack1, TileEntityAncientTeleport.SIZE, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }

            } else if (!this.mergeItemStack(itemstack1, 0, TileEntityAncientTeleport.SIZE, false)) {

                return ItemStack.EMPTY;

            }

            if (itemstack1.isEmpty()) {

                slot.putStack(ItemStack.EMPTY);

            } else {

                slot.onSlotChanged();

            }
        }

        return itemstack;
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

                if (!itemstack.isEmpty() && itemstack.getItem() == stack.getItem() && (!stack.getHasSubtypes() || stack.getMetadata() == itemstack.getMetadata()) && ItemStack.areItemStackTagsEqual(stack, itemstack) && !isHide(i)) {
                    int slotStackLimit = slot.getSlotStackLimit();

                    if (stack.getItem() instanceof ItemPhial && slot instanceof SlotItemHandlerAspectInput) {
                        slotStackLimit = 64;
                    }

                    int j = itemstack.getCount() + stack.getCount();
                    int maxSize = Math.min(slotStackLimit, stack.getMaxStackSize());

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

                if (itemstack1.isEmpty() && slot1.isItemValid(stack) && !isHide(i)) {
                    int slotStackLimit = slot1.getSlotStackLimit();

                    if (stack.getItem() instanceof ItemPhial && slot1 instanceof SlotItemHandlerAspectInput) {
                        slotStackLimit = 64;
                    }

                    if (stack.getCount() > slotStackLimit)
                    {
                        slot1.putStack(stack.splitStack(slotStackLimit));
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

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return tile.isUsableByPlayer(player);
    }
}
