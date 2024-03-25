package com.artur.returnoftheancients.ancientworldutilities;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class PortalEvent {
    static short tick = 0;

//    @SubscribeEvent
    public void Tick(TickEvent.PlayerTickEvent e) {
        if (tick >= 200) {
            System.out.println("Tick");
            tick = 0;
        }
        tick++;
    }
}
