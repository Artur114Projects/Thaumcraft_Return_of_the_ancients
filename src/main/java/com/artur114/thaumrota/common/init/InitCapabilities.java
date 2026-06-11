package com.artur114.thaumrota.common.init;

import com.artur114.bananalib.mc.cap.BananaCapStorage;
import com.artur114.bananalib.mc.cap.BananaCapStorageNoSave;
import com.artur114.bananalib.mc.registry.ann.AutoInstantiate;
import com.artur114.bananalib.mc.registry.interf.ILoadStagePre;
import com.artur114.thaumrota.common.worldstate.ancientworld.system.base.IAncientLayer1Manager;
import com.artur114.thaumrota.common.worldstate.ancientworld.system.server.ServerAncientLayer1Manager;
import com.artur114.thaumrota.common.worldstate.blockprotect.IProtectedChunk;
import com.artur114.thaumrota.common.worldstate.blockprotect.server.ServerProtectedChunk;
import com.artur114.thaumrota.common.worldstate.playertimer.IPlayerTimer;
import com.artur114.thaumrota.common.worldstate.playertimer.PlayerTimer;
import com.artur114.thaumrota.common.energy.system.EnergySystemsManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nonnull;

@AutoInstantiate
public class InitCapabilities implements ILoadStagePre {
    @CapabilityInject(IPlayerTimer.class)
    public static final Capability<IPlayerTimer> TIMER = null;
    @CapabilityInject(IProtectedChunk.class)
    public static final Capability<IProtectedChunk> PROTECTED_CHUNK = null;
    @CapabilityInject(EnergySystemsManager.class)
    public static final Capability<EnergySystemsManager> ENERGY_SYSTEMS_MANAGER = null;
    @CapabilityInject(IAncientLayer1Manager.class)
    public static final Capability<IAncientLayer1Manager> ANCIENT_LAYER_1_MANAGER = null;

    public static IPlayerTimer getTimer(@Nonnull EntityPlayer player) {
        return player.getCapability(TIMER, null);
    }

    @Override
    public void onPreInit() {
        CapabilityManager.INSTANCE.register(
            IProtectedChunk.class,
            new BananaCapStorage<>(),
            () -> new ServerProtectedChunk(new ChunkPos(0, 0), 0)
        );
        CapabilityManager.INSTANCE.register(
            EnergySystemsManager.class,
            new BananaCapStorageNoSave<>(),
            () -> new EnergySystemsManager(null)
        );
        CapabilityManager.INSTANCE.register(
            IAncientLayer1Manager.class,
            new BananaCapStorage<>(),
            () -> new ServerAncientLayer1Manager(null)
        );
        CapabilityManager.INSTANCE.register(
            IPlayerTimer.class,
            new BananaCapStorage<>(),
            PlayerTimer::new
        );
    }
}
