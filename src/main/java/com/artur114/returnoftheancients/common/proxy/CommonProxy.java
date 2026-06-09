package com.artur114.returnoftheancients.common.proxy;

import com.artur114.returnoftheancients.common.capabilities.PlayerTimer;
import com.artur114.returnoftheancients.common.capabilities.TRACapabilities;
import com.artur114.returnoftheancients.client.gui.GuiHandler;
import com.artur114.returnoftheancients.common.events.ServerEventsHandler;
import com.artur114.returnoftheancients.common.generation.portal.base.AncientPortalsProcessor;
import com.artur114.returnoftheancients.common.generation.terraingen.TerrainHandler;
import com.artur114.returnoftheancients.common.handlers.RegisterHandler;

import com.artur114.returnoftheancients.common.init.InitBiome;
import com.artur114.returnoftheancients.common.init.InitDimensions;
import com.artur114.returnoftheancients.main.ThaumicRotA;
import com.artur114.returnoftheancients.common.misc.CraftingRegister;
import com.artur114.returnoftheancients.common.structurebuilder.StructuresBuildManager;
import net.minecraft.item.Item;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;


public class CommonProxy {
	public void registerItemRenderer(Item item, int meta, String id) {}
	
    public void preInit(FMLPreInitializationEvent event) {
        ForgeChunkManager.setForcedChunkLoadingCallback(ThaumicRotA.INSTANCE, ThaumicRotA.FORCE_LOADING_CALLBACK);
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
        NetworkRegistry.INSTANCE.registerGuiHandler(ThaumicRotA.INSTANCE, new GuiHandler());
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
    }
}
