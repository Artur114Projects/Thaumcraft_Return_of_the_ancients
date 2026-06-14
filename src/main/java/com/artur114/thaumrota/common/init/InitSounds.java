package com.artur114.thaumrota.common.init;

import com.artur114.bananalib.mc.registry.ann.RegistryContainer;
import com.artur114.thaumrota.main.ThaumRotA;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

@RegistryContainer
public class InitSounds {
    public static SoundEvent FAN = create("fan");
    public static SoundEvent BUM = create("bum");
    public static SoundEvent WHISPER = create("whisper");
    public static SoundEvent RUI_DEAD = create("rui_dead");
    public static SoundEvent HEARTBEAT = create("heartbeat");
    public static SoundEvent SPOTLIGHT = create("spotlight");
    public static SoundEvent PROJECTOR = create("projector");
    public static SoundEvent DOOR_OPEN_2 = create("door_open_2");
    public static SoundEvent DOOR_OPEN_1 = create("door_open_1");
    public static SoundEvent PORTAL_IMPACT = create("portal_impact");
    public static SoundEvent PNEUMATIC_PUFF = create("pneumatic_puff");
    public static SoundEvent FIRE_TRAP_SOUND = create("fire_trap_sound");
    public static SoundEvent PORTAL_HEARTBEAT = create("portal_heart_beat");
    public static SoundEvent PEDESTAL_ACTIVATED = create("pedestal_activated");
    public static SoundEvent PNEUMATIC_PUFF_LONG = create("pneumatic_puff_long");
    public static SoundEvent FIRE_TRAP_START_SOUND = create("fire_trap_sound_start");
    public static SoundEvent ANCIENT_CONTROLLER_ACTIVATE = create("ancient_controller_activate");
    public static SoundEvent ANCIENT_CONTROLLER_DEACTIVATE = create("ancient_controller_deactivate");

    private static SoundEvent create(String name) {
        ResourceLocation rl = ThaumRotA.loc(name);
        return new SoundEvent(rl).setRegistryName(rl);
    }
}