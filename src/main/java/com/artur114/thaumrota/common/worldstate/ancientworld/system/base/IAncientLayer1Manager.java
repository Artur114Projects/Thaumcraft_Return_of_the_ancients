package com.artur114.thaumrota.common.worldstate.ancientworld.system.base;

import net.minecraft.entity.player.EntityPlayer;
import org.jetbrains.annotations.Nullable;

public interface IAncientLayer1Manager {
    @Nullable
    AncientLayer1 sectorFor(EntityPlayer player);
    boolean isRemote();
    void worldTick();
}
