package com.artur.returnoftheancients.util.interfaces;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface IWriteToNBT {
    NBTTagCompound writeToNBT(NBTTagCompound nbt);

    static NBTTagList getNBTListAsCollection(Collection<? extends IWriteToNBT> collection) {
        NBTTagList list = new NBTTagList();

        for (IWriteToNBT write : collection) {
            list.appendTag(write.writeToNBT(new NBTTagCompound()));
        }

        return list;
    }

    static <T extends ICanConstructInNBT> List<T> initObjectsAsNBTList(NBTTagList list, Class<T> objClass) throws NullPointerException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (list == null || objClass == null) {
            throw new NullPointerException();
        }

        List<T> ret = new ArrayList<>(list.tagCount());

        for (int i = 0; i != list.tagCount(); i++) {
            NBTTagCompound tag = list.getCompoundTagAt(i);
            Constructor<T> constructor = objClass.getDeclaredConstructor(NBTTagCompound.class);
            constructor.setAccessible(true);
            ret.add(constructor.newInstance(tag));
        }

        return ret;
    }

    static <T extends IWriteToNBT> NBTTagList objectsToNBT(Collection<T> objects) {
        NBTTagList list = new NBTTagList();

        for (T obj : objects) {
            list.appendTag(obj.writeToNBT(new NBTTagCompound()));
        }

        return list;
    }
}
