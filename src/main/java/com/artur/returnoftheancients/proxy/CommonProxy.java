package com.artur.returnoftheancients.proxy;

import com.artur.returnoftheancients.commads.*;
import com.artur.returnoftheancients.handlers.RegisterHandler;

import com.artur.returnoftheancients.main.MainR;
import com.artur.returnoftheancients.misc.PlayersCountDifficultyProcessor;
import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.network.ClientPacketMisc;
import com.artur.returnoftheancients.network.ServerPacketTpToHome;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;


public class CommonProxy {
	public void registerItemRenderer(Item item, int meta, String id) {}
	
    public void preInit(FMLPreInitializationEvent event)
    {
        MainR.NETWORK.registerMessage(new ServerPacketTpToHome.HandlerTTH(), ServerPacketTpToHome.class, 2, Side.SERVER);
    	RegisterHandler.preInitRegistries();
    }
    
    public void init(FMLInitializationEvent event)
    {
    }

    public void postInit(FMLPostInitializationEvent event)
    {
        PlayersCountDifficultyProcessor.compile(TRAConfigs.DifficultySettings.playersCountDifficulty);
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
    }
}
