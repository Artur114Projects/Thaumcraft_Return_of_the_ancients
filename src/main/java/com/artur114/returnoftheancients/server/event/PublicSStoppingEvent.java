package com.artur114.returnoftheancients.server.event;

import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PublicSStoppingEvent extends Event {
    private final FMLServerStoppingEvent event;

    public PublicSStoppingEvent(FMLServerStoppingEvent event) {
        this.event = event;
    }

    public FMLServerStoppingEvent fml() {
        return this.event;
    }
}