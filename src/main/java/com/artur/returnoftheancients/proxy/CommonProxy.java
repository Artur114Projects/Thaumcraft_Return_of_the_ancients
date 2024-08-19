package com.artur.returnoftheancients.proxy;

import com.artur.returnoftheancients.ancientworldgeneration.main.AncientWorld;
import com.artur.returnoftheancients.handlers.RegisterHandler;

import com.artur.returnoftheancients.misc.CraftingRegister;
import com.artur.returnoftheancients.misc.PlayersCountDifficultyProcessor;
import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import thaumcraft.Thaumcraft;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchCategories;

import java.util.Objects;


public class CommonProxy {
	public void registerItemRenderer(Item item, int meta, String id) {}
	
    public void preInit(FMLPreInitializationEvent event)
    {
        CraftingRegister.register();
    	RegisterHandler.preInitRegistries();
    }
    
    public void init(FMLInitializationEvent event)
    {
        ResearchCategories.registerCategory("ANCIENT_WORLD", null, new AspectList(), new ResourceLocation(Thaumcraft.MODID, "textures/blocks/ancient_stone_5.png"), new ResourceLocation(Thaumcraft.MODID, "textures/gui/gui_research_back_6.jpg"));
        ThaumcraftApi.registerResearchLocation(new ResourceLocation(Referense.MODID, "research/my_research"));
    }

    public void postInit(FMLPostInitializationEvent event)
    {

        RegisterHandler.registerStructures();
    }
    public void serverStarting(FMLServerStartingEvent event) {
        AncientWorld.load();
        RegisterHandler.registerCommands(event);
        PlayersCountDifficultyProcessor.compile(TRAConfigs.DifficultySettings.playersCountDifficulty);
    }

}
