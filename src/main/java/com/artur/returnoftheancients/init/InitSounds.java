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
    public static SoundTRA FIRE_TRAP_SOUND = create("fire_trap_sound");

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