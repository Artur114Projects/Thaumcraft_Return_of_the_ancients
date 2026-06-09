package com.artur114.returnoftheancients.common.proxy;

import com.artur114.returnoftheancients.common.capabilities.PlayerTimer;
import com.artur114.returnoftheancients.common.init.InitCapabilities;
import com.artur114.returnoftheancients.common.handlers.RegisterHandler;

import com.artur114.returnoftheancients.common.init.InitDimensions;
import com.artur114.returnoftheancients.common.misc.CraftingRegister;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.*;


public class CommonProxy {
	public void registerItemRenderer(Item item, int meta, String id) {}
	
    public void preInit(FMLPreInitializationEvent event) {
        RegisterHandler.registerTileEntities();
        InitDimensions.registerDimensions();
        RegisterHandler.registerPackets();
        CraftingRegister.register();
        RegisterHandler.preInit();
        PlayerTimer.preInit();
    }
    
    public void init(FMLInitializationEvent event) {
    }

    public void postInit(FMLPostInitializationEvent event) {

    }

    public void serverStarting(FMLServerStartingEvent event) {

    }

    public void serverStopping(FMLServerStoppingEvent event) {

    }
}
