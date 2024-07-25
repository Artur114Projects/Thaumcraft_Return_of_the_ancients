package com.artur.returnoftheancients.proxy;

import com.artur.returnoftheancients.ancientworldgeneration.main.AncientWorld;
import com.artur.returnoftheancients.ancientworldgeneration.structurebuilder.CustomGenStructure;
import com.artur.returnoftheancients.commads.*;
import com.artur.returnoftheancients.handlers.RegisterHandler;

import com.artur.returnoftheancients.misc.PlayersCountDifficultyProcessor;
import com.artur.returnoftheancients.misc.TRAConfigs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;


public class CommonProxy {
	public void registerItemRenderer(Item item, int meta, String id) {}
	
    public void preInit(FMLPreInitializationEvent event)
    {
    	RegisterHandler.preInitRegistries();
    }
    
    public void init(FMLInitializationEvent event)
    {
    }

    public void postInit(FMLPostInitializationEvent event)
    {
    }
    public void serverStarting(FMLServerStartingEvent event) {
        RegisterHandler.registerCommands(event);
        RegisterHandler.registerStructures(event);
        PlayersCountDifficultyProcessor.compile(TRAConfigs.DifficultySettings.playersCountDifficulty);
        AncientWorld.serverStarting(event);
    }

}
