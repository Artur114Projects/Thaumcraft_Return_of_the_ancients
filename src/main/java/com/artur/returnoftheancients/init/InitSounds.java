package com.artur.returnoftheancients.init;

import com.artur.returnoftheancients.misc.SoundTRA;
import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Mod.EventBusSubscriber(modid = Referense.MODID)
public class InitSounds {
    private static final List<SoundEvent> soundEvents = new ArrayList<>();
    public static final HashMap<String, SoundEvent> SOUND_MAP = new HashMap<>();

    public static SoundTRA BUM = create("bum");
    public static SoundTRA WHISPER = create("whisper");
    public static SoundTRA RUI_DEAD = create("rui_dead");
    public static SoundTRA HEARTBEAT = create("heartbeat");
    public static SoundTRA DOOR_OPEN_2 = create("door_open_2");
    public static SoundTRA DOOR_OPEN_1 = create("door_open_1");
    public static SoundTRA PORTAL_IMPACT = create("portal_impact");
    public static SoundTRA PNEUMATIC_PUFF = create("pneumatic_puff");
    public static SoundTRA FIRE_TRAP_SOUND = create("fire_trap_sound");
    public static SoundTRA PORTAL_HEARTBEAT = create("portal_heart_beat");
    public static SoundTRA PEDESTAL_ACTIVATED = create("pedestal_activated");
    public static SoundTRA PNEUMATIC_PUFF_LONG = create("pneumatic_puff_long");
    public static SoundTRA FIRE_TRAP_START_SOUND = create("fire_trap_sound_start");
    public static SoundTRA ANCIENT_PORTAL_LIGHT_ON = create("ancient_portal_light_on");
    public static SoundTRA ANCIENT_CONTROLLER_ACTIVATE = create("ancient_controller_activate");
    public static SoundTRA ANCIENT_CONTROLLER_DEACTIVATE = create("ancient_controller_deactivate");

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> e) {
        for (SoundEvent soundEvent : soundEvents) {
            if (soundEvent != null) {
                ForgeRegistries.SOUND_EVENTS.register(soundEvent);
            }
        }
        soundEvents.clear();
    }

    private static SoundTRA create(String name) {
        ResourceLocation rl = new ResourceLocation(Referense.MODID, name);
        SoundEvent s = (new SoundEvent(rl)).setRegistryName(rl);
        soundEvents.add(s);
        SOUND_MAP.put(name, s);
        return new SoundTRA(name, s);
    }
}