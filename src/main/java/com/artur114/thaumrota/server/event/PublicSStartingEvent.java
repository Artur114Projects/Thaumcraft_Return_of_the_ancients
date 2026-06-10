package com.artur114.thaumrota.server.event;

import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PublicSStartingEvent extends Event {
    private final FMLServerStartingEvent event;

    public PublicSStartingEvent(FMLServerStartingEvent event) {
        this.event = event;
    }

    public FMLServerStartingEvent fml() {
        return this.event;
    }
}