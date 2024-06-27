package com.artur.returnoftheancients.init;

import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;


//@Mod.EventBusSubscriber(modid= Referense.MODID, bus = Mod.EventBusSubscriber)
//@Mod.EventBusSubscriber(Side.SERVER, modid = Referense.MODID)
public class InitSounds {


    //Это наш звук, `test_sound` это название звука указанного в sounds.json
    private static final List<SoundEvent> soundEvents = new ArrayList<>();
    public static SoundEvent BUM = create("bum");
    public static SoundEvent WHISPER = create("whisper");
    public static SoundEvent HEARTBEAT = create("heartbeat");
    public static SoundEvent RUI_DEAD = create("rui_dead");

    @SubscribeEvent
    public void registerSounds(RegistryEvent.Register<SoundEvent> e) {
        for (SoundEvent soundEvent : soundEvents) {
            if (soundEvent != null) {
                ForgeRegistries.SOUND_EVENTS.register(soundEvent);
            }
        }
    }

    private static SoundEvent create(String name) {
        ResourceLocation rl = new ResourceLocation(Referense.MODID, name);
        SoundEvent s = (new SoundEvent(rl)).setRegistryName(rl);
        soundEvents.add(s);
        return s;
    }
}