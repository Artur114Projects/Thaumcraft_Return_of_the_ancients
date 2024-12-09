package com.artur.returnoftheancients.tileentity;

import com.artur.returnoftheancients.generation.generators.portal.AncientPortalNaturalGeneration;
import com.artur.returnoftheancients.generation.generators.portal.base.AncientPortal;
import com.artur.returnoftheancients.init.InitItems;
import net.minecraft.client.gui.GuiEnchantment;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityAncientTeleport extends TileEntity {
    public static final int SIZE = 1;


    private final ItemStackHandler itemStackHandler = new ItemStackHandler(SIZE) {
        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
        }
    };

    public int isActive = 0;

    public AncientPortal portal;


    public void requestToActivate() {
        ItemStack stack = itemStackHandler.getStackInSlot(0);
        if (stack.getItem() == InitItems.GAVNO) {
            itemStackHandler.setStackInSlot(0, ItemStack.EMPTY);
            isActive = 1;
            portal = new AncientPortalNaturalGeneration(world.getMinecraftServer(), world.provider.getDimension(), pos.getX() >> 4, pos.getZ() >> 4, pos.getY() - 1);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        if (compound.hasKey("items")) {
            itemStackHandler.deserializeNBT((NBTTagCompound) compound.getTag("items"));
        }
        if (compound.hasKey("isActive")) {
            isActive = compound.getInteger("isActive");
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        compound.setTag("items", itemStackHandler.serializeNBT());
        compound.setInteger("isActive", isActive);

        return compound;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }

        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemStackHandler);
        }

        return super.getCapability(capability, facing);
    }

    public boolean isUsableByPlayer(EntityPlayer player) {
        return world.getTileEntity(pos) == this && player.getDistanceSq(pos) <= 64.0D;
    }

    @Override
    public String toString() {
        return itemStackHandler.getStackInSlot(0).toString();
    }
}
