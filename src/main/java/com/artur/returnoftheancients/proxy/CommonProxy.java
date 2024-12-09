package com.artur.returnoftheancients.proxy;

import com.artur.returnoftheancients.ancientworldgeneration.main.AncientWorld;
import com.artur.returnoftheancients.capabilities.PlayerTimer;
import com.artur.returnoftheancients.client.gui.GuiHandler;
import com.artur.returnoftheancients.generation.biomes.BiomeTaint;
import com.artur.returnoftheancients.handlers.RegisterHandler;

import com.artur.returnoftheancients.init.InitBiome;
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
	
    public void preInit(FMLPreInitializationEvent event)
    {
        PlayerTimer.preInit();
        CraftingRegister.register();
    	RegisterHandler.preInitRegistries();
    }
    
    public void init(FMLInitializationEvent event)
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(MainR.instance, new GuiHandler());
        ((BiomeTaint) InitBiome.TAINT).registerBiomeP2();
        RegisterHandler.registerTCRecipes();
        RegisterHandler.registerResearch();
    }

    public void postInit(FMLPostInitializationEvent event)
    {

        RegisterHandler.registerStructures();
    }
    public void serverStarting(FMLServerStartingEvent event) {
        AncientWorld.load();
        RegisterHandler.registerCommands(event);
    }

}
