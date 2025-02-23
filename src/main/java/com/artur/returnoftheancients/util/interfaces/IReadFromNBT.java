package com.artur.returnoftheancients.util.interfaces;

import net.minecraft.nbt.NBTTagCompound;

import java.lang.reflect.InvocationTargetException;

public interface IReadFromNBT {
    void readFromNBT(NBTTagCompound nbt) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException;
}
