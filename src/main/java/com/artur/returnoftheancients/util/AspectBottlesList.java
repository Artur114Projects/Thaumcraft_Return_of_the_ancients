package com.artur.returnoftheancients.util;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

import java.util.List;
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

    public void empty(int... ids) {
        for (int i : ids) {
            aspectBottles[i].setCount(0);
        }
    }

    public AspectBottlesList get(int... ids) {
        AspectBottle[] bottles = new AspectBottle[ids.length];
        for (int i = 0; i != ids.length; i++) {
            bottles[i] = aspectBottles[ids[i]];
        }
        return new AspectBottlesList(bottles);
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

    public Aspect containAnyAspectOnList(List<Aspect> list) {
        for (Aspect aspect : list) {
            if (containsAspect(aspect)) {
                return aspect;
            }
        }
        return null;
    }

    public boolean isAllFull() {
        for (AspectBottle bottle : aspectBottles) {
            if (bottle.isCanAdd()) {
                return false;
            }
        }
        return true;
    }

    public boolean isFull(int... bottles) {
        for (int id : bottles) {
            AspectBottle bottle = aspectBottles[id];
            if (bottle.isCanAdd()) {
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

    public boolean canAdd(Aspect aspect, int amount) {
        return Objects.requireNonNull(getAspectBottleWithAspect(aspect)).canAdd(amount);
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
        NBTTagList list = nbt.getTagList("AspectBottles", 10);
        for (int i = 0; i != list.tagCount(); i++) {
            aspectBottles[i].readFromNBT(list.getCompoundTagAt(i));
        }
    }
}
