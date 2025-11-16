package com.artur.returnoftheancients.proxy;

import com.artur.returnoftheancients.capabilities.PlayerTimer;
import com.artur.returnoftheancients.capabilities.TRACapabilities;
import com.artur.returnoftheancients.client.gui.GuiHandler;
import com.artur.returnoftheancients.events.ServerEventsHandler;
import com.artur.returnoftheancients.generation.portal.base.AncientPortalsProcessor;
import com.artur.returnoftheancients.generation.terraingen.TerrainHandler;
import com.artur.returnoftheancients.handlers.RegisterHandler;

import com.artur.returnoftheancients.init.InitBiome;
import com.artur.returnoftheancients.init.InitDimensions;
import com.artur.returnoftheancients.main.MainR;
import com.artur.returnoftheancients.misc.CraftingRegister;
import com.artur.returnoftheancients.misc.WorldDataFields;
import com.artur.returnoftheancients.structurebuilder.StructureBuildersManager;
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
        StructureBuildersManager.init();
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
