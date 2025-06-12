package com.artur.returnoftheancients.ancientworld.system.server;

import com.artur.returnoftheancients.ancientworld.system.base.IAncientLayer1Manager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public interface IServerAncientLayer1Manager extends IAncientLayer1Manager, INBTSerializable<NBTTagCompound> {
    void onPlayerLoginIn(EntityPlayerMP player);
    void onPlayerLoginOut(EntityPlayerMP player);
    void onPlayerLost(EntityPlayerMP player);
    void onPlayerElope(EntityPlayerMP player);
    void intoAncientWorld(EntityPlayerMP player);
}