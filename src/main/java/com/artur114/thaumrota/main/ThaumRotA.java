package com.artur114.thaumrota.main;

import com.artur114.bananalib.mc.registry.BananaRegisterBus;
import com.artur114.bananalib.mc.registry.IRegisterBus;
import com.artur114.bananalib.mc.services.BananaServicesManager;
import com.artur114.bananalib.mc.services.IServicesManager;
import com.artur114.thaumrota.common.creative.RotACreativeTab;
import com.artur114.thaumrota.common.util.DevScriptsShell;
import com.artur114.thaumrota.proxy.IProxy;
import com.artur114.thaumrota.server.event.PublicSStartingEvent;
import com.artur114.thaumrota.server.event.PublicSStoppingEvent;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

import java.nio.file.Paths;

@Mod(modid = ThaumRotA.MODID, useMetadata = true)
public class ThaumRotA {
    public static final CreativeTabs CREATIVE_TAB = new RotACreativeTab("thaumrota_tab");
    public static final SimpleNetworkWrapper NETWORK = new SimpleNetworkWrapper("thaumrota");
    public static final IRegisterBus REGISTER_BUS = new BananaRegisterBus().putNetWrapper(NETWORK);
    public static final IServicesManager SERVICES = new BananaServicesManager();
    public static final DevScriptsShell DEV_SHELL = new DevScriptsShell(Paths.get("..", "src/test/groovy/scripts").toAbsolutePath().normalize()).loadClass("RotADevScript.groovy");
    public static final EventBus INTERNAL_EVENT_BUS = new EventBus();
    public static final String MODID = "thaumrota";

    @Instance
    public static ThaumRotA INSTANCE;

    @SidedProxy(
        clientSide = "com.artur114.thaumrota.proxy.ClientProxy",
        serverSide = "com.artur114.thaumrota.proxy.ServerProxy"
    )
    public static IProxy proxy;

    @EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(REGISTER_BUS, event);
    }

    @EventHandler
    public static void Init(FMLInitializationEvent event) {
        proxy.init(REGISTER_BUS, event);
    }

    @EventHandler
    public static void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(REGISTER_BUS, event);
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent e) {
        INTERNAL_EVENT_BUS.post(new PublicSStartingEvent(e));
    }

    @EventHandler
    public void serverStopping(FMLServerStoppingEvent e) {
        INTERNAL_EVENT_BUS.post(new PublicSStoppingEvent(e));
    }

    public static ResourceLocation loc(String id) {
        return new ResourceLocation(MODID, id);
    }
}

