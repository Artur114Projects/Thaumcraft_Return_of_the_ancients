package com.artur.returnoftheancients.main;

import com.artur.returnoftheancients.ancientworldlegacy.main.AncientWorld;
import com.artur.returnoftheancients.client.fx.shader.HeatShader;
import com.artur.returnoftheancients.generation.portal.base.AncientPortalsProcessor;
import com.artur.returnoftheancients.generation.terraingen.TerrainHandler;
import com.artur.returnoftheancients.handlers.RegisterHandler;
import com.artur.returnoftheancients.events.ServerEventsHandler;
import com.artur.returnoftheancients.init.InitBiome;
import com.artur.returnoftheancients.init.InitDimensions;
import com.artur.returnoftheancients.misc.ReturnOfTheAncientsTab;
import com.artur.returnoftheancients.misc.WorldDataFields;
import com.artur.returnoftheancients.proxy.CommonProxy;
import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

import java.lang.reflect.Method;


@Mod(modid = Referense.MODID, useMetadata = true, version = Referense.VERSION)
public class MainR {

	// TODO: Улучшить древний мир
	// TODO: Сделать зараженный биом
	// TODO: Сделать систему с новой энергией механизмами и т.д
	// TODO: Сделать систему генерации мобов

	public static final SimpleNetworkWrapper NETWORK = new SimpleNetworkWrapper(Referense.MODID);

	@Instance
	public static MainR INSTANCE;
	
	@SidedProxy(clientSide = Referense.CLIENT, serverSide = Referense.COMMON)
	public static CommonProxy proxy;
	
	@EventHandler
	public static void preInit(FMLPreInitializationEvent event) {
		ForgeChunkManager.setForcedChunkLoadingCallback(INSTANCE, TRA_LOADING_CALLBACK);
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
		AncientPortalsProcessor.unload();
		ServerEventsHandler.unload();
		WorldDataFields.unload();
		AncientWorld.unload();
	}


	public static final CreativeTabs RETURN_OF_ANCIENTS_TAB = new ReturnOfTheAncientsTab("returnoftheancients_tab");

	public static final ForgeChunkManager.LoadingCallback TRA_LOADING_CALLBACK = (tickets, world) -> {
        for (ForgeChunkManager.Ticket ticket : tickets) {
			if (!ticket.isPlayerTicket()) {
				boolean flag = true;

				if (ticket.getModData().hasKey("userClassName")) {
					try {
						Class<?> clas = Class.forName(ticket.getModData().getString("userClassName"));
						Method method = clas.getDeclaredMethod("loadingCallback", ForgeChunkManager.Ticket.class, World.class);
						method.invoke(null, ticket, world);
					} catch (Exception e) {
						System.err.println("Failed to find a ticket user! Ticket is release...");
						e.printStackTrace(System.err);
						flag = false;
					}
                } else {
					System.err.println("Failed to find a ticket user! Ticket is release...");
					flag = false;
				}

				if (!flag) {
					ForgeChunkManager.releaseTicket(ticket);
				}
			}
		}
    };
}
