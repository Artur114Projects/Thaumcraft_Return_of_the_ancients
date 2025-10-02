package com.artur.returnoftheancients.worldsystems;

import com.artur.returnoftheancients.capabilities.TRACapabilities;
import com.artur.returnoftheancients.handlers.RegisterHandler;
import com.artur.returnoftheancients.worldsystems.interf.IWorldSystem;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldSystemsManager implements INBTSerializable<NBTTagCompound> {
    private static final List<Class<? extends IWorldSystem>> SYSTEMS = new ArrayList<>();

    private final Map<Class<? extends IWorldSystem>, IWorldSystem> systemMap = new HashMap<>();

    public WorldSystemsManager(World world) {
        for (Class<? extends IWorldSystem> systemClass : SYSTEMS) {
            try {
                IWorldSystem system = systemClass.newInstance();
                system.setWorld(world);

                this.systemMap.put(systemClass, system);
            } catch (InstantiationException | IllegalAccessException e) {
                new RuntimeException(e).printStackTrace(System.err);
            }
        }
    }

    public void update() {

    }

    @SuppressWarnings("unchecked")
    public <T extends IWorldSystem> T system(Class<T> system) {
        return (T) this.systemMap.get(system);
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return null;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {

    }

    public static WorldSystemsManager manager(World world) {
        return world.getCapability(TRACapabilities.WORLD_SYSTEMS_MANAGER, null);
    }

    static {
        RegisterHandler.registerWorldSystems(SYSTEMS);
    }
}
