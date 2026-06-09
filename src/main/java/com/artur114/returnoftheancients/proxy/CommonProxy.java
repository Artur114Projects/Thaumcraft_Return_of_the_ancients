package com.artur114.returnoftheancients.proxy;

import com.artur114.returnoftheancients.capabilities.PlayerTimer;
import com.artur114.returnoftheancients.capabilities.TRACapabilities;
import com.artur114.returnoftheancients.client.gui.GuiHandler;
import com.artur114.returnoftheancients.events.ServerEventsHandler;
import com.artur114.returnoftheancients.generation.portal.base.AncientPortalsProcessor;
import com.artur114.returnoftheancients.generation.terraingen.TerrainHandler;
import com.artur114.returnoftheancients.handlers.RegisterHandler;

import com.artur114.returnoftheancients.init.InitBiome;
import com.artur114.returnoftheancients.init.InitDimensions;
import com.artur114.returnoftheancients.main.MainR;
import com.artur114.returnoftheancients.misc.CraftingRegister;
import com.artur114.returnoftheancients.misc.WorldDataFields;
import com.artur114.returnoftheancients.structurebuilder.StructuresBuildManager;
import net.minecraft.item.Item;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;


public class CommonProxy {
	public void registerItemRenderer(Item item, int meta, String id) {}
	
    public void preInit(FMLPreInitializationEvent event) {
        ForgeChunkManager.setForcedChunkLoadingCallback(MainR.INSTANCE, MainR.FORCE_LOADING_CALLBACK);
        MinecraftForge.TERRAIN_GEN_BUS.register(new TerrainHandler());
        RegisterHandler.registerTileEntities();
        InitDimensions.registerDimensions();
        RegisterHandler.registerPackets();
        CraftingRegister.register();
        RegisterHandler.preInit();
        TRACapabilities.preInit();
        PlayerTimer.preInit();
        InitBiome.preInit();
    }
    
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(MainR.INSTANCE, new GuiHandler());
        RegisterHandler.registerTCRecipes();
        RegisterHandler.registerResearch();
        InitBiome.init();
    }

    public void postInit(FMLPostInitializationEvent event) {
        StructuresBuildManager.init();
    }

    public void serverStarting(FMLServerStartingEvent event) {
        RegisterHandler.registerCommands(event);
    }

    public void serverStopping(FMLServerStoppingEvent event) {
        AncientPortalsProcessor.unload();
        ServerEventsHandler.unload();
        WorldDataFields.unload();
    }
}
