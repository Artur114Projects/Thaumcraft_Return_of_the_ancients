package com.artur.returnoftheancients.main;

import com.artur.returnoftheancients.ancientworldgeneration.main.AncientWorld;
import com.artur.returnoftheancients.energy.EnergySystemsProvider;
import com.artur.returnoftheancients.generation.portal.base.AncientPortalsProcessor;
import com.artur.returnoftheancients.generation.terraingen.TerrainHandler;
import com.artur.returnoftheancients.handlers.RegisterHandler;
import com.artur.returnoftheancients.handlers.ServerEventsHandler;
import com.artur.returnoftheancients.init.InitBiome;
import com.artur.returnoftheancients.init.InitDimensions;
import com.artur.returnoftheancients.misc.ReturnOfTheAncientsTab;
import com.artur.returnoftheancients.misc.WorldDataFields;
import com.artur.returnoftheancients.proxy.CommonProxy;
import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;


@Mod(modid = Referense.MODID, useMetadata = true, version = Referense.VERSION)
public class MainR {

	public static final SimpleNetworkWrapper NETWORK = new SimpleNetworkWrapper(Referense.MODID);

	@Instance
	public static MainR instance;
	
	@SidedProxy(clientSide = Referense.CLIENT, serverSide = Referense.COMMON)
	public static CommonProxy proxy;
	
	@EventHandler
	public static void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.TERRAIN_GEN_BUS.register(new TerrainHandler());
		RegisterHandler.registerPackets();
		InitDimensions.registerDimensions();
		InitBiome.initBiomes();
		RegisterHandler.registerTileEntity();
		proxy.preInit(event);
	}
	
	@EventHandler
	public static void Init(FMLInitializationEvent event) {
		proxy.init(event);
		InitBiome.registerBiomeArrays();
	}
	
	@EventHandler
	public static void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}


	@EventHandler
	public void serverStarting(FMLServerStartingEvent e) {
		proxy.serverStarting(e);
	}

	@EventHandler
	public void serverStopping(FMLServerStoppingEvent e) {
		AncientWorld.unload();
		AncientPortalsProcessor.unload();
		EnergySystemsProvider.unload();
		WorldDataFields.unload();
		ServerEventsHandler.unload();
	}


	// Creative Tabs
	public static final CreativeTabs ReturnOfTheAncientsTab = new ReturnOfTheAncientsTab("returnoftheancients_tab");
}
