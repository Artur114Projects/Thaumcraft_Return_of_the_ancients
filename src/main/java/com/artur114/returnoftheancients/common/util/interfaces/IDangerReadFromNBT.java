package com.artur114.returnoftheancients.common.util.interfaces;

import net.minecraft.nbt.NBTTagCompound;

public interface IDangerReadFromNBT {
    void readFromNBT(NBTTagCompound nbt) throws Exception;
}
