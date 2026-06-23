package com.artur114.thaumrota.common.event;

import com.artur114.thaumrota.common.event.managers.*;
import com.artur114.thaumrota.main.ThaumRotA;
import com.artur114.thaumrota.server.event.PublicSStoppingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber(modid = ThaumRotA.MODID)
public class CommonEventsHandler {
    public static final TaintBiomeEventsManager TAINT_BIOME_EVENTS_MANAGER = new TaintBiomeEventsManager();
    public static final ShortChunkLoadManager SHORT_CHUNK_LOAD_MANAGER = new ShortChunkLoadManager();
    public static final PlayerInBiomeManager PLAYER_IN_BIOME_MANAGER = new PlayerInBiomeManager();
    public static final TCFixEventsManager TC_FIX_EVENTS_MANAGER = new TCFixEventsManager();
    public static final MiscEventsManager MISC_EVENTS_MANAGER = new MiscEventsManager();
    public static final TimerTasksManager TIMER_TASKS_MANAGER = new TimerTasksManager();
    public static final SlowBuildManager SLOW_BUILD_MANAGER = new SlowBuildManager();

    @SubscribeEvent
    public static void playerTickEvent(TickEvent.PlayerTickEvent e) {
        PLAYER_IN_BIOME_MANAGER.tickEventPlayerTickEvent(e);
        MISC_EVENTS_MANAGER.tickEventPlayerTickEvent(e);
    }

    @SubscribeEvent
    public static void canSpawn(LivingSpawnEvent.CheckSpawn e) {
        TAINT_BIOME_EVENTS_MANAGER.livingSpawnEventCheckSpawn(e);
    }

    @SubscribeEvent
    public static void tick(TickEvent.WorldTickEvent e) {
        TAINT_BIOME_EVENTS_MANAGER.tickEventWorldTickEvent(e);
    }

    @SubscribeEvent
    public static void livingFall(LivingFallEvent e) {
        TC_FIX_EVENTS_MANAGER.livingFallEvent(e);
    }

    @SubscribeEvent
    public static void serverTick(TickEvent.ServerTickEvent e) {
        SHORT_CHUNK_LOAD_MANAGER.tickEventServerTickEvent(e);
        TIMER_TASKS_MANAGER.tickEventServerTickEvent(e);
        SLOW_BUILD_MANAGER.tickEventServerTickEvent(e);
    }

    @SubscribeEvent
    public static void saveEvent(WorldEvent.Save e) {
        SHORT_CHUNK_LOAD_MANAGER.worldEventSave(e);
    }

    @SubscribeEvent
    public static void unload(PublicSStoppingEvent e) {
        SHORT_CHUNK_LOAD_MANAGER.unload();
        TIMER_TASKS_MANAGER.unload();
        SLOW_BUILD_MANAGER.unload();
    }
}