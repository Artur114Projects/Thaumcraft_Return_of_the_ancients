package com.artur114.returnoftheancients.main;

import com.artur114.bananalib.mc.register.BananaRegisterBus;
import com.artur114.bananalib.mc.register.IRegisterBus;
import com.artur114.returnoftheancients.common.misc.CreativeTabRotA;
import com.artur114.returnoftheancients.common.referense.Referense;
import com.artur114.returnoftheancients.proxy.IProxy;
import com.artur114.returnoftheancients.server.event.PublicSStartingEvent;
import com.artur114.returnoftheancients.server.event.PublicSStoppingEvent;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

// TODO: Улучшить древний мир
// TODO: Сделать зараженный биом

@Mod(modid = Referense.MODID, useMetadata = true, version = Referense.VERSION)
public class ThaumicRotA {
    public static final CreativeTabs ROTA_CREATIVE_TAB = new CreativeTabRotA("returnoftheancients_tab");
    public static final SimpleNetworkWrapper NETWORK = new SimpleNetworkWrapper(Referense.MODID);
    public static final IRegisterBus REGISTER_BUS = new BananaRegisterBus().putNetWrapper(NETWORK);
    public static final EventBus INTERNAL_EVENT_BUS = new EventBus();
    public static final String NAME = "Thaumcraft: Return of the Ancients";
    public static final String MODID = "returnoftheancients";
    public static final String VERSION = "v2.0.0-alpha";

    @Instance
    public static ThaumicRotA INSTANCE;

    @SidedProxy(clientSide = Referense.CLIENT, serverSide = Referense.COMMON)
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

