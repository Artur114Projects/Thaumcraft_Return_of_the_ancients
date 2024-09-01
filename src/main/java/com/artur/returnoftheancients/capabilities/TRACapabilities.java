package com.artur.returnoftheancients.capabilities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import thaumcraft.api.capabilities.IPlayerWarp;

import javax.annotation.Nonnull;

public class TRACapabilities {
    @CapabilityInject(IPlayerTimerCapability.class)
    public static final Capability<IPlayerTimerCapability> TIMER = null;


    public static IPlayerTimerCapability getTimer(@Nonnull EntityPlayer player) {
        return player.getCapability(TIMER, null);
    }
}
