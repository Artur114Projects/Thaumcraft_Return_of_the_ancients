package com.artur.returnoftheancients.proxy;

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
        if (TRAConfigs.Any.debugMode) {
            event.registerServerCommand(new TestCommand());
            event.registerServerCommand(new TestCommand2());
            event.registerServerCommand(new DataManager());
            event.registerServerCommand(new GenAncientLabyrinth());
            event.registerServerCommand(new Command());
        }
        event.registerServerCommand(new TRACommand());

        PlayersCountDifficultyProcessor.compile(TRAConfigs.DifficultySettings.playersCountDifficulty);
        CustomGenStructure.put("ancient_entry");
        CustomGenStructure.put("ancient_crossroads");

        CustomGenStructure.put("ancient_way_rotate-1");
        CustomGenStructure.put("ancient_way_rotate-2");
        CustomGenStructure.put("ancient_turn_rotate-1");
        CustomGenStructure.put("ancient_turn_rotate-2");
        CustomGenStructure.put("ancient_turn_rotate-3");
        CustomGenStructure.put("ancient_turn_rotate-4");
        CustomGenStructure.put("ancient_fork_rotate-1");
        CustomGenStructure.put("ancient_fork_rotate-2");
        CustomGenStructure.put("ancient_fork_rotate-3");
        CustomGenStructure.put("ancient_fork_rotate-4");
        CustomGenStructure.put("ancient_end_rotate-1");
        CustomGenStructure.put("ancient_end_rotate-2");
        CustomGenStructure.put("ancient_end_rotate-3");
        CustomGenStructure.put("ancient_end_rotate-4");

        CustomGenStructure.put("ancient_boss");
        CustomGenStructure.put("ancient_exit");
        CustomGenStructure.put("ancient_entry_way");
        CustomGenStructure.put("ancient_door");
        CustomGenStructure.register();
        CustomGenStructure.put("air_cube");
        CustomGenStructure.setUseEBS();
        CustomGenStructure.register();
    }

}
