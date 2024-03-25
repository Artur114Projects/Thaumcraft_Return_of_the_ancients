package com.artur.returnoftheancients.main;

import com.artur.returnoftheancients.init.InitBiome;
import com.artur.returnoftheancients.init.InitBlocks;
import com.artur.returnoftheancients.init.InitDimensions;
import com.artur.returnoftheancients.misc.ReturnOfTheAncientsTab;
import com.artur.returnoftheancients.proxy.CommonProxy;
import com.artur.returnoftheancients.referense.Referense;
import com.artur.returnoftheancients.utils.interfaces.IMobsGen;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;


@Mod(modid = Referense.MODID, useMetadata = true, version = Referense.VERSION)
public class Main {
	
	@Instance
	public static Main instance;
	
	@SidedProxy(clientSide = Referense.CLIENT, serverSide = Referense.COMMON)
	public static CommonProxy proxy;
	
	@EventHandler
	public static void preInit(FMLPreInitializationEvent event) {
//		System.out.println("preInit");
//		for (int i = 0; i != 10; i++)
		InitDimensions.registerDimensions();
		InitBiome.initBiomes();
		InitBlocks.registerTileEntity();
		proxy.preInit(event);
	}
	
	@EventHandler
	public static void Init(FMLInitializationEvent event) {
//		System.out.println("Init");
//		for (int i = 0; i != 10; i++)
//			System.out.println("!!!");
		proxy.init(event);
	}
	
	@EventHandler
	public static void postInit(FMLPostInitializationEvent event) {
//		System.out.println("postInit");
//		for (int i = 0; i != 10; i++)
//			System.out.println("!!!");
		proxy.postInit(event);
	}


	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		proxy.serverStarting(event);
	}


	// Creative Tabs
	public static final CreativeTabs ReturnOfTheAncientsTab = new ReturnOfTheAncientsTab("returnoftheancients_tab");
}
