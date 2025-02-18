package com.artur.returnoftheancients.capabilities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public interface IPlayerTimerCapability extends INBTSerializable<NBTTagCompound> {
    long getTime(String timerId);
    void addTime(long amount, String timerId);
    void delete(String timerId);
    void createTimer(String timerId);
    boolean hasTimer(String timerId);
}
