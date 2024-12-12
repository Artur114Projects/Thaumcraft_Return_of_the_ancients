package com.artur.returnoftheancients.utils;

import com.artur.returnoftheancients.main.MainR;
import com.artur.returnoftheancients.network.ClientPacketSyncTileAncientTeleport;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

import java.util.Objects;

public class AspectBottlesList {

    public final AspectBottle[] aspectBottles;

    public AspectBottlesList(AspectBottle... aspectBottles) {
        this.aspectBottles = aspectBottles;
    }

    @Nullable
    public AspectBottle getAspectBottleWithAspect(Aspect aspect, int number) {
        int count = 0;

        for (AspectBottle bottle : aspectBottles) {
            if (bottle.aspect == aspect) {
                if (count == number) {
                    return bottle;
                }
                count++;
            }
        }

        return null;
    }

    @Nullable
    public AspectBottle getAspectBottleWithAspect(Aspect aspect) {
        return getAspectBottleWithAspect(aspect, 0);
    }

    public boolean containAmount(Aspect aspect, int amount) {
        AspectBottle bottle = getAspectBottleWithAspect(aspect);
        return bottle != null && bottle.getCount() < amount;
    }

    public boolean containList(AspectList list) {
        for (Aspect aspect : list.getAspects()) {
            if (!containAmount(aspect, list.getAmount(aspect))) {
                return false;
            }
        }
        return true;
    }

    public boolean isAllFull() {
        for (AspectBottle bottle : aspectBottles) {
            if (bottle.isNotFull()) {
                return false;
            }
        }
        return true;
    }

    public boolean take(Aspect aspect, int amount) {
        return Objects.requireNonNull(getAspectBottleWithAspect(aspect)).takeCount(amount);
    }

    public int add(Aspect aspect, int amount) {
        return Objects.requireNonNull(getAspectBottleWithAspect(aspect)).addCount(amount);
    }

    public boolean containsAspect(Aspect aspect) {
        for (AspectBottle bottle : aspectBottles) {
            if (bottle.aspect == aspect) {
                return true;
            }
        }
        return false;
    }

    public AspectList toAspectList() {
        AspectList list = new AspectList();
        for (AspectBottle bottle : aspectBottles) {
            if (bottle.getCount() > 0) {
                list.add(bottle.aspect, bottle.getCount());
            }
        }
        return list;
    }

    public void addListener(IContainerListener listener, Container containerIn) {
        for (int i = 0; i != aspectBottles.length; i++) {
            listener.sendWindowProperty(containerIn, i + 100, aspectBottles[i].getCount());
        }
    }

    public void detectAndSendChanges(IContainerListener listener, Container containerIn) {
        for (int i = 0; i != aspectBottles.length; i++) {
            AspectBottle bottle = aspectBottles[i];
            if (bottle.lastCount != bottle.getCount()) {
                listener.sendWindowProperty(containerIn, 100 + i, bottle.getCount());
                bottle.lastCount = bottle.getCount();
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void updateCount(int id, int count) {
        if ((id - 100) < aspectBottles.length) {
            aspectBottles[id - 100].setCount(count);
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagList list = new NBTTagList();
        for (AspectBottle aspectBottle : aspectBottles) {
            list.appendTag(aspectBottle.writeToNBT(new NBTTagCompound()));
        }
        nbt.setTag("AspectBottles", list);
        return nbt;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagList list = nbt.getTagList("AspectBottles", 9);
        for (int i = 0; i != list.tagCount(); i++) {
            aspectBottles[i].readFromNBT(list.getCompoundTagAt(i));
        }
    }

    public void sync(TileEntity tile) {
        BlockPos pos = tile.getPos();
        MainR.NETWORK.sendToAllAround(new ClientPacketSyncTileAncientTeleport(pos, 0, writeToNBT(new NBTTagCompound())), new NetworkRegistry.TargetPoint(tile.getWorld().provider.getDimension(), pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 128.0D));
    }

    @SideOnly(Side.CLIENT)
    public void syncInClient(NBTTagCompound nbt) {
        NBTTagList list = nbt.getTagList("AspectBottles", 9);
        for (int i = 0; i != list.tagCount(); i++) {
            aspectBottles[i].readFromNBT(list.getCompoundTagAt(i));
        }
        System.out.println("Sync " + nbt);
    }
}
