package com.artur.returnoftheancients.proxy;

import com.artur.returnoftheancients.ancientworldlegacy.main.AncientWorld;
import com.artur.returnoftheancients.capabilities.PlayerTimer;
import com.artur.returnoftheancients.capabilities.TRACapabilities;
import com.artur.returnoftheancients.client.gui.GuiHandler;
import com.artur.returnoftheancients.generation.biomes.BiomeTaint;
import com.artur.returnoftheancients.handlers.RegisterHandler;

import com.artur.returnoftheancients.init.InitBiome;
import com.artur.returnoftheancients.init.InitBlocks;
import com.artur.returnoftheancients.main.MainR;
import com.artur.returnoftheancients.misc.CraftingRegister;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;


public class CommonProxy {
	public void registerItemRenderer(Item item, int meta, String id) {}
	
    public void preInit(FMLPreInitializationEvent event) {
        CraftingRegister.register();
        TRACapabilities.preInit();
        PlayerTimer.preInit();
    }
    
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(MainR.INSTANCE, new GuiHandler());
        ((BiomeTaint) InitBiome.TAINT).registerBiomeP2();
        ((BiomeTaint) InitBiome.TAINT_MOUNTAINS).registerBiomeP2();
        ((BiomeTaint) InitBiome.TAINT_SEA).registerBiomeP2(InitBlocks.TAINT_VOID_STONE.getDefaultState(), InitBlocks.TAINT_VOID_STONE.getDefaultState());
        ((BiomeTaint) InitBiome.TAINT_WASTELAND).registerBiomeP2(InitBlocks.TAINT_VOID_STONE.getDefaultState(), InitBlocks.TAINT_VOID_STONE.getDefaultState());
        ((BiomeTaint) InitBiome.TAINT_DEEP_SEA).registerBiomeP2(InitBlocks.TAINT_VOID_STONE.getDefaultState(), InitBlocks.TAINT_VOID_STONE.getDefaultState());
        ((BiomeTaint) InitBiome.TAINT_PLATEAU).registerBiomeP2();
        RegisterHandler.registerTCRecipes();
        RegisterHandler.registerResearch();
    }

    public void postInit(FMLPostInitializationEvent event) {
        RegisterHandler.registerStructures();
    }

    public void serverStarting(FMLServerStartingEvent event) {
        RegisterHandler.registerCommands(event);
        AncientWorld.load();
    }
}
