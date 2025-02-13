package com.artur.returnoftheancients.tileentity;

import com.artur.returnoftheancients.containers.ContainerWithPages;
import com.artur.returnoftheancients.containers.IContainerWithPages;
import com.artur.returnoftheancients.containers.util.AspectInputSlotsManager;
import com.artur.returnoftheancients.containers.util.CustomCraftingGear;
import com.artur.returnoftheancients.energy.intefaces.ITileEnergyProvider;
import com.artur.returnoftheancients.energy.tile.TileEnergyProviderBase;
import com.artur.returnoftheancients.energy.util.EnergyContainerHandler;
import com.artur.returnoftheancients.energy.util.EnumEnergyType;
import com.artur.returnoftheancients.generation.portal.AncientPortalOpening;
import com.artur.returnoftheancients.generation.portal.base.AncientPortal;
import com.artur.returnoftheancients.util.AspectBottle;
import com.artur.returnoftheancients.util.AspectBottlesList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.api.items.ItemsTC;

import java.util.Objects;

public class TileEntityAncientTeleport extends TileEnergyProviderBase implements IAspectContainer, IEssentiaTransport, ITickable, IContainerWithPages, ITileEnergyProvider {
    public static final int SIZE = 9;


    private final ItemStackHandler itemStackHandler = new ItemStackHandler(SIZE) {
        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
        }
    };
    public final AspectBottlesList aspectBottles = new AspectBottlesList(
            new AspectBottle(Aspect.VOID, 60, 32 + 24, 16),
            new AspectBottle(Aspect.DARKNESS, 60, 32 + 24 + 24 + 4, 16),
            new AspectBottle(Aspect.ELDRITCH, 60, 32 + 24 + 48 + 8, 16),

            new AspectBottle(Aspect.ORDER, 80, 38 + 28, 12),
            new AspectBottle(Aspect.ENTROPY, 80, 38 + 28 + 32 + 4, 12)
    );
    public CustomCraftingGear craftingGear = new CustomCraftingGear(itemStackHandler, new int[]{4, 5, 6, 7, 8}, new ItemStack[] {
            new ItemStack(ItemsTC.plate, 8, 3),
            new ItemStack(ItemsTC.plate, 8, 3),

            new ItemStack(ItemsTC.mechanismComplex, 2),

            new ItemStack(ItemsTC.plate, 8, 3),
            new ItemStack(ItemsTC.plate, 8, 3)
    });
    public AspectInputSlotsManager inputSlotsManager = new AspectInputSlotsManager(itemStackHandler, aspectBottles, new int[] {0, 1, 2, 3},
            new int[] {3},
            new int[] {0, 1, 2}
    );
    public EnergyContainerHandler energyContainerHandler = new EnergyContainerHandler(EnumEnergyType.MEGA.format(10.0F), 0, EnumEnergyType.MEGA.format(0.5F), 1, 2, 3);
    private ContainerWithPages currentContainer;

    public AncientPortal portal;

    public int isActive = 0;



    // TODO: Доделать систему с удалением вкладки после активации
    public void requestToActivate(EntityPlayerMP player) {
        if (player.isCreative() || (craftingGear.craft() && aspectBottles.isFull(0, 1, 2))) {
            aspectBottles.empty(0, 1, 2);
            isActive = 1;
            inputSlotsManager.reset(new int[] {0, 1}, new int[] {3});
            portal = new AncientPortalOpening(this);
        }
    }


    @Override
    public void update() {
        if (!world.isRemote) {
            inputSlotsManager.fill();
            energyContainerHandler.update();
            this.fill();
        }
    }

    private void fill() {
        EnumFacing[] facings = EnumFacing.HORIZONTALS;
        for (EnumFacing facing : facings) {
            TileEntity te = ThaumcraftApiHelper.getConnectableTile(this.world, this.pos, facing);
            if (te == null) continue;
            IEssentiaTransport ic = (IEssentiaTransport) te;
            EnumFacing oppositeFacing = facing.getOpposite();
            Aspect aspect = ic.getEssentiaType(oppositeFacing);
            AspectBottle bottle = aspectBottles.getAspectBottleWithAspect(aspect);
            if (bottle != null && ic.getEssentiaAmount(oppositeFacing) > 0 && bottle.isCanAdd()) {
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
    public void readSyncNBT(NBTTagCompound nbt) {
        aspectBottles.readFromNBT(nbt);
    }

    @Override
    public NBTTagCompound writeSyncNBT(NBTTagCompound nbt) {
        return aspectBottles.writeToNBT(nbt);
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
    public void markDirty() {
        super.markDirty();
        this.syncTile(false);
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
        AspectBottle bottle = aspectBottles.getAspectBottleWithAspect(aspect);
        if (i == 0 || bottle == null || !bottle.isCanAdd()) {
            return 0;
        }
        int ret = bottle.addCount(i);
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
        return isActive == 0 ? null : Aspect.ORDER;
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
        return enumFacing == EnumFacing.DOWN ? Objects.requireNonNull(aspectBottles.getAspectBottleWithAspect(Aspect.ENTROPY)).getCount() : 0;
    }

    @Override
    public int getMinimumSuction() {
        return 0;
    }

    @Override
    public ContainerWithPages getContainer() {
        return currentContainer;
    }

    @Override
    public void setContainer(ContainerWithPages container) {
        this.currentContainer = container;
    }

    @Override
    public BlockPos getPosC() {
        return this.pos;
    }

    @Override
    public int getDimension() {
        return world.provider.getDimension();
    }

    @Override
    public boolean isRemote() {
        return world.isRemote;
    }

    @Override
    public boolean isCanConnect(EnumFacing facing) {
        return facing != EnumFacing.DOWN && facing != EnumFacing.UP;
    }

    @Override
    public float canAdd(float count) {
        return energyContainerHandler.canAdd(count);
    }

    @Override
    public float add(float count) {
        return energyContainerHandler.add(count);
    }

    @Override
    public float take(float count) {
        return energyContainerHandler.take(count);
    }

    @Override
    public boolean canAddFromFacing(EnumFacing facing) {
        return facing != EnumFacing.DOWN && facing != EnumFacing.UP;
    }

    @Override
    public boolean canTakeFromFacing(EnumFacing facing) {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return energyContainerHandler.isEmpty();
    }

    @Override
    public boolean isNeedAdd() {
        return energyContainerHandler.isNeedAdd();
    }

    @Override
    public float maxInput() {
        return energyContainerHandler.getMaxInput();
    }

    @Override
    public float maxOutput() {
        return energyContainerHandler.getMaxOutput();
    }
}
