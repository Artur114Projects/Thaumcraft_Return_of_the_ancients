package com.artur.returnoftheancients.capabilities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GenericCapProviderNS<C> implements ICapabilityProvider {
    protected Capability<C> capability;
    protected C instance;

    public GenericCapProviderNS(C instance, Capability<C> capability) {
        this.capability = capability;
        this.instance = instance;
    }

    @Override
    public boolean hasCapability(@NotNull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == this.capability;
    }

    @Nullable
    @Override
    public <T> T getCapability(@NotNull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == this.capability ? this.capability.cast(instance) : null;
    }
}