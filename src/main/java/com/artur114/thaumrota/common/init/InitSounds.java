package com.artur114.thaumrota.common.init;

import com.artur114.bananalib.mc.registry.ann.RegistryContainer;
import com.artur114.thaumrota.main.ThaumRotA;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

@RegistryContainer
public class InitSounds {
    public static final SoundEvent FAN = create("fan");
    public static final SoundEvent BUM = create("bum");
    public static final SoundEvent WHISPER = create("whisper");
    public static final SoundEvent HEARTBEAT = create("heartbeat");
    public static final SoundEvent SPOTLIGHT = create("spotlight");
    public static final SoundEvent PROJECTOR = create("projector");
    public static final SoundEvent MAGIC_PUFF = create("magic_puff");
    public static final SoundEvent DOOR_OPEN_2 = create("door_open_2");
    public static final SoundEvent DOOR_OPEN_1 = create("door_open_1");
    public static final SoundEvent PNEUMATIC_PUFF = create("pneumatic_puff");
    public static final SoundEvent PEDESTAL_ACTIVATED = create("pedestal_activated");
    public static final SoundEvent PNEUMATIC_PUFF_LONG = create("pneumatic_puff_long");
    public static final SoundEvent ANCIENT_WORLD_AMBIENT = create("ancient_world_ambient");
    public static final SoundEvent ANCIENT_CONTROLLER_ACTIVATE = create("ancient_controller_activate");
    public static final SoundEvent ANCIENT_CONTROLLER_DEACTIVATE = create("ancient_controller_deactivate");

    private static SoundEvent create(String name) {
        ResourceLocation rl = ThaumRotA.loc(name);
        return new SoundEvent(rl).setRegistryName(rl);
    }
}