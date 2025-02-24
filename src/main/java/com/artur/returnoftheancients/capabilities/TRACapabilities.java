package com.artur.returnoftheancients.capabilities;

import com.artur.returnoftheancients.blockprotect.IProtectedChunk;
import com.artur.returnoftheancients.blockprotect.client.ClientProtectedChunk;
import com.artur.returnoftheancients.blockprotect.server.IServerProtectedChunk;
import com.artur.returnoftheancients.blockprotect.server.ServerProtectedChunk;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.capabilities.IPlayerWarp;

import javax.annotation.Nonnull;

public class TRACapabilities {
    @CapabilityInject(IPlayerTimerCapability.class)
    public static final Capability<IPlayerTimerCapability> TIMER = null;
    @CapabilityInject(IProtectedChunk.class)
    public static final Capability<IProtectedChunk> PROTECTED_CHUNK = null;


    public static IPlayerTimerCapability getTimer(@Nonnull EntityPlayer player) {
        return player.getCapability(TIMER, null);
    }

    public static void preInit() {
        CapabilityManager.INSTANCE.register(IProtectedChunk.class, new Capability.IStorage<IProtectedChunk>() {
            @Nullable
            @Override
            public NBTBase writeNBT(Capability<IProtectedChunk> capability, IProtectedChunk instance, EnumFacing side) {
                if (!(instance instanceof IServerProtectedChunk)) {
                    return null;
                }

                return ((IServerProtectedChunk) instance).serializeNBT();
            }

            @Override
            public void readNBT(Capability<IProtectedChunk> capability, IProtectedChunk instance, EnumFacing side, NBTBase nbt) {
                if (!(instance instanceof IServerProtectedChunk) || !(nbt instanceof NBTTagCompound)) {
                    return;
                }

                ((IServerProtectedChunk) instance).deserializeNBT((NBTTagCompound) nbt);
            }
        }, () -> new ServerProtectedChunk(new ChunkPos(0, 0), 0));

    }
}
