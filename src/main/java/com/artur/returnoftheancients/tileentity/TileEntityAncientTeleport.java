package com.artur.returnoftheancients.tileentity;

import com.artur.returnoftheancients.generation.generators.portal.AncientPortalNaturalGeneration;
import com.artur.returnoftheancients.generation.generators.portal.base.AncientPortal;
import com.artur.returnoftheancients.init.InitItems;
import com.artur.returnoftheancients.utils.AspectBottle;
import com.artur.returnoftheancients.utils.AspectBottlesList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.aspects.IEssentiaTransport;

public class TileEntityAncientTeleport extends TileEntity implements IAspectContainer, IEssentiaTransport, ITickable {
    public static final int SIZE = 1;


    private final ItemStackHandler itemStackHandler = new ItemStackHandler(SIZE) {
        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
        }
    };
    public final AspectBottlesList aspectBottles = new AspectBottlesList (
            new AspectBottle(Aspect.FIRE, 200),
            new AspectBottle(Aspect.ELDRITCH, 64)
    );

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
    public void update() {
        fill();
    }

    private void fill() {
        EnumFacing[] facings = EnumFacing.HORIZONTALS;
        for (EnumFacing facing : facings) {
            TileEntity te = ThaumcraftApiHelper.getConnectableTile(this.world, this.pos, facing);
            if (te == null) continue;
            IEssentiaTransport ic = (IEssentiaTransport)te;
            EnumFacing oppositeFacing =  facing.getOpposite();
            Aspect aspect = ic.getEssentiaType(oppositeFacing);
            AspectBottle bottle = aspectBottles.getAspectBottleWithAspect(aspect);
            if (bottle != null && ic.getEssentiaAmount(oppositeFacing) > 0 && bottle.isNotFull()) {
                addToContainer(aspect, ic.takeEssentia(aspect, 1, oppositeFacing));
            }
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
        if (compound.hasKey("AspectBottles")) {
            aspectBottles.readFromNBT(compound);
            aspectBottles.sync(this);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        compound.setTag("items", itemStackHandler.serializeNBT());
        compound.setInteger("isActive", isActive);
        aspectBottles.writeToNBT(compound);

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
    public AspectList getAspects() {
        return aspectBottles.toAspectList();
    }

    @Override
    public void setAspects(AspectList aspectList) {

    }

    @Override
    public boolean doesContainerAccept(Aspect aspect) {
        return aspectBottles.containsAspect(aspect);
    }

    @Override
    public int addToContainer(Aspect aspect, int i) {
        if (i == 0) {
            return 0;
        }
        int ret = aspectBottles.add(aspect, i);
        aspectBottles.sync(this);
        this.markDirty();
        return ret;
    }

    @Override
    public boolean takeFromContainer(Aspect aspect, int i) {
        boolean flag = aspectBottles.take(aspect, i);
        if (flag) {
            markDirty();
        }
        return flag;
    }

    @Override
    @Deprecated
    public boolean takeFromContainer(AspectList aspectList) {
        return false;
    }

    @Override
    public boolean doesContainerContainAmount(Aspect aspect, int i) {
        return aspectBottles.containAmount(aspect, i);
    }

    @Override
    @Deprecated
    public boolean doesContainerContain(AspectList aspectList) {
        return aspectBottles.containList(aspectList);
    }

    @Override
    public int containerContains(Aspect aspect) {
        AspectBottle bottle = aspectBottles.getAspectBottleWithAspect(aspect);
        return bottle != null ? bottle.getCount() : 0;
    }

    // IEssentiaTransport

    @Override
    public boolean isConnectable(EnumFacing enumFacing) {
        return enumFacing != EnumFacing.UP;
    }

    @Override
    public boolean canInputFrom(EnumFacing enumFacing) {
        return enumFacing != EnumFacing.UP && enumFacing != EnumFacing.DOWN;
    }

    @Override
    public boolean canOutputTo(EnumFacing enumFacing) {
        return enumFacing == EnumFacing.DOWN;
    }

    @Override
    public void setSuction(Aspect aspect, int i) {

    }

    @Override
    public Aspect getSuctionType(EnumFacing enumFacing) {
        return null;
    }

    @Override
    public int getSuctionAmount(EnumFacing enumFacing) {
        return aspectBottles.isAllFull() ? 0 : 128;
    }

    @Override
    public int takeEssentia(Aspect aspect, int amount, EnumFacing face) {
        return this.canOutputTo(face) && this.takeFromContainer(aspect, amount) ? amount : 0;
    }

    @Override
    public int addEssentia(Aspect aspect, int amount, EnumFacing face) {
        return this.canInputFrom(face) ? amount - this.addToContainer(aspect, amount) : 0;
    }

    @Override
    public Aspect getEssentiaType(EnumFacing enumFacing) {
        return null;
    }

    @Override
    public int getEssentiaAmount(EnumFacing enumFacing) {
        return 0;
    }

    @Override
    public int getMinimumSuction() {
        return 0;
    }
}
