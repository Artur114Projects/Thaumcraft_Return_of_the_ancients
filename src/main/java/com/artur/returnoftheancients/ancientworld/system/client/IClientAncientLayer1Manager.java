package com.artur.returnoftheancients.ancientworld.system.client;

import com.artur.returnoftheancients.ancientworld.system.base.IAncientLayer1Manager;
import net.minecraft.nbt.NBTTagCompound;

public interface IClientAncientLayer1Manager extends IAncientLayer1Manager {
    void createAncientLayer(NBTTagCompound data);
    void handleUpdateTag(NBTTagCompound data);
    void startBuild();
    void finisBuild();
}