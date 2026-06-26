package com.artur114.thaumrota.common.worldstate.playertimer;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public interface IPlayerTimer extends INBTSerializable<NBTTagCompound> {
    long getTime(String timerId);
    void addTime(long amount, String timerId);
    void delete(String timerId);
    void createTimer(String timerId);
    boolean hasTimer(String timerId);
}
