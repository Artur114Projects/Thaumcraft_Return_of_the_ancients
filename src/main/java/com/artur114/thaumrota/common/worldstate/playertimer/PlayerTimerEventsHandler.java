package com.artur114.thaumrota.common.worldstate.playertimer;

import com.artur114.bananalib.mc.cap.BananaCapProv;
import com.artur114.thaumrota.common.init.InitCapabilities;
import com.artur114.thaumrota.main.ThaumRotA;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class PlayerTimerEventsHandler {
    @SubscribeEvent
    public static void attachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityPlayer) {
            event.addCapability(ThaumRotA.loc("timer"), new BananaCapProv<>(new PlayerTimer(), InitCapabilities.TIMER));
        }
    }
}
