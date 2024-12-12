package com.artur.returnoftheancients.containers;

import com.artur.returnoftheancients.tileentity.TileEntityAncientTeleport;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerAncientTeleport extends Container {

    private final TileEntityAncientTeleport tile;
    private int lastIsActive;

    public ContainerAncientTeleport(IInventory playerInventory, TileEntityAncientTeleport te) {

        this.tile = te;


        this.addSlots();
        this.bindPlayerSlots(playerInventory);
    }

    private void bindPlayerSlots(IInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                int x = j * 18 + 74;
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

        IItemHandler itemHandler = this.tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

        int slotID = 0;


//        addSlotToContainer(new SlotItemHandler(itemHandler, slotID++, 44, 17));
        addSlotToContainer(new SlotItemHandler(itemHandler, slotID++, 80, 35));
//        addSlotToContainer(new SlotItemHandler(itemHandler, slotID++, 116, 53));
    }

    @Override
    public void addListener(IContainerListener listener) {
        super.addListener(listener);
        listener.sendWindowProperty(this, 0, tile.isActive);

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
    public boolean canInteractWith(EntityPlayer player) {
        return tile.isUsableByPlayer(player);
    }
}
