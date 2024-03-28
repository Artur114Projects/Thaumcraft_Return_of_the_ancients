package com.artur.returnoftheancients.sounds;

import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;


//@Mod.EventBusSubscriber(modid= Referense.MODID, bus = Mod.EventBusSubscriber)
//@Mod.EventBusSubscriber(Side.SERVER, modid = Referense.MODID)
public class ModSounds {


    //Это наш звук, `test_sound` это название звука указанного в sounds.json
    public static SoundEvent BUM = create("bum");


    @SubscribeEvent
    public void registerSounds(RegistryEvent.Register<SoundEvent> e) {
        //Регистрация звука
        ForgeRegistries.SOUND_EVENTS.register(BUM);
    }
    private static SoundEvent create(String name) {
        ResourceLocation rl = new ResourceLocation(Referense.MODID, name);
        return (SoundEvent) (new SoundEvent(rl)).setRegistryName(rl);
    }
}