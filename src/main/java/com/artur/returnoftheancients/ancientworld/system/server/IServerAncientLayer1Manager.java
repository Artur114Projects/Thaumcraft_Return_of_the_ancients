package com.artur.returnoftheancients.ancientworld.system.server;

import com.artur.returnoftheancients.ancientworld.system.base.IAncientLayer1Manager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public interface IServerAncientLayer1Manager extends IAncientLayer1Manager, INBTSerializable<NBTTagCompound> {
    void onPlayerLoginIn(EntityPlayerMP player);
    void onPlayerLoginOut(EntityPlayerMP player);
    boolean onPlayerLost(EntityPlayerMP player);
    boolean onPlayerElope(EntityPlayerMP player);
    boolean onPlayerInterruptBuild(EntityPlayerMP player);
    void intoAncientWorld(EntityPlayerMP player);
}