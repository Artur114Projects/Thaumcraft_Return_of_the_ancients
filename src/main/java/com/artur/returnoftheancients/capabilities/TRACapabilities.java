package com.artur.returnoftheancients.capabilities;

import com.artur.returnoftheancients.ancientworld.system.base.IAncientLayer1Manager;
import com.artur.returnoftheancients.ancientworld.system.server.ServerAncientLayer1Manager;
import com.artur.returnoftheancients.ancientworld.system.server.IServerAncientLayer1Manager;
import com.artur.returnoftheancients.blockprotect.IProtectedChunk;
import com.artur.returnoftheancients.blockprotect.server.IServerProtectedChunk;
import com.artur.returnoftheancients.blockprotect.server.ServerProtectedChunk;
import com.artur.returnoftheancients.energy.system.EnergySystemsManager;
import com.artur.returnoftheancients.worldsystems.WorldSystemsManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class TRACapabilities {
    @CapabilityInject(IPlayerTimerCapability.class)
    public static final Capability<IPlayerTimerCapability> TIMER = null;
    @CapabilityInject(IProtectedChunk.class)
    public static final Capability<IProtectedChunk> PROTECTED_CHUNK = null;
    @CapabilityInject(EnergySystemsManager.class)
    public static final Capability<EnergySystemsManager> ENERGY_SYSTEMS_MANAGER = null;
    @CapabilityInject(WorldSystemsManager.class)
    public static final Capability<WorldSystemsManager> WORLD_SYSTEMS_MANAGER = null;
    @CapabilityInject(IAncientLayer1Manager.class)
    public static final Capability<IAncientLayer1Manager> ANCIENT_LAYER_1_MANAGER = null;


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

        CapabilityManager.INSTANCE.register(EnergySystemsManager.class, new Capability.IStorage<EnergySystemsManager>() {
            @Nullable
            @Override
            public NBTBase writeNBT(Capability<EnergySystemsManager> capability, EnergySystemsManager instance, EnumFacing side) {return null;}
            @Override
            public void readNBT(Capability<EnergySystemsManager> capability, EnergySystemsManager instance, EnumFacing side, NBTBase nbt) {}
        }, () -> new EnergySystemsManager(null));

        CapabilityManager.INSTANCE.register(IAncientLayer1Manager.class, new Capability.IStorage<IAncientLayer1Manager>() {
            @Nullable
            @Override
            public NBTBase writeNBT(Capability<IAncientLayer1Manager> capability, IAncientLayer1Manager instance, EnumFacing side) {
                if (!(instance instanceof IServerAncientLayer1Manager)) {
                    return null;
                }

                return ((IServerAncientLayer1Manager) instance).serializeNBT();
            }
            @Override
            public void readNBT(Capability<IAncientLayer1Manager> capability, IAncientLayer1Manager instance, EnumFacing side, NBTBase nbt) {
                if (!(instance instanceof IServerAncientLayer1Manager) || !(nbt instanceof NBTTagCompound)) {
                    return;
                }

                ((IServerAncientLayer1Manager) instance).deserializeNBT((NBTTagCompound) nbt);
            }
        }, () -> new ServerAncientLayer1Manager(null));

        CapabilityManager.INSTANCE.register(WorldSystemsManager.class, new Capability.IStorage<WorldSystemsManager>() {
            @Nullable
            @Override
            public NBTBase writeNBT(Capability<WorldSystemsManager> capability, WorldSystemsManager instance, EnumFacing side) {
                if (instance == null) {
                    return null;
                }

                return instance.serializeNBT();
            }
            @Override
            public void readNBT(Capability<WorldSystemsManager> capability, WorldSystemsManager instance, EnumFacing side, NBTBase nbt) {
                if (instance == null || !(nbt instanceof NBTTagCompound)) {
                    return;
                }

                instance.deserializeNBT((NBTTagCompound) nbt);
            }
        }, () -> new WorldSystemsManager(null));

    }
}
