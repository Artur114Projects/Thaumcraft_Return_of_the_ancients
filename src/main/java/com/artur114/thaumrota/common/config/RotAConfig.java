package com.artur114.thaumrota.common.config;

import com.artur114.thaumrota.common.config.client.RotAClientConfig;
import com.artur114.thaumrota.common.config.server.RotAServerConfig;
import com.artur114.thaumrota.main.ThaumRotA;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = ThaumRotA.MODID)
@Mod.EventBusSubscriber(modid = ThaumRotA.MODID)
public class RotAConfig {

    @Config.LangKey("thaumrota.cfg.any")
    public static Any any = new Any();

    @Config.LangKey("thaumrota.cfg.server")
    public static RotAServerConfig server = new RotAServerConfig();

    @Config.LangKey("thaumrota.cfg.client")
    public static RotAClientConfig client = new RotAClientConfig();


    public static class Any {
        @Config.LangKey("thaumrota.cfg.any.debug")
        @Config.RequiresMcRestart
        public boolean debugMode = false;
    }

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(ThaumRotA.MODID)) {
            ConfigManager.sync(ThaumRotA.MODID, Config.Type.INSTANCE);
            MinecraftForge.EVENT_BUS.post(new RotAConfUpdateEvent());
        }
    }
}