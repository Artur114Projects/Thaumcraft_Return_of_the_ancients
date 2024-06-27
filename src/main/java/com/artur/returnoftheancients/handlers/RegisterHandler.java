package com.artur.returnoftheancients.handlers;

import com.artur.returnoftheancients.events.RemoveUnresolvedItems;
import com.artur.returnoftheancients.generation.generators.AncientLabyrinthGenerator;
import com.artur.returnoftheancients.init.InitBlocks;
import com.artur.returnoftheancients.init.InitItems;
import com.artur.returnoftheancients.init.InitSounds;
import com.artur.returnoftheancients.utils.interfaces.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class RegisterHandler {

	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(InitBlocks.BLOCKS.toArray(new Block[0]));
		System.out.println("Blocks is registry! " + InitBlocks.BLOCKS);
	}

	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(InitItems.ITEMS.toArray(new Item[0]));
	}

	public static void preInitRegistries()
    {
		MinecraftForge.EVENT_BUS.register(new EventsHandler());
		MinecraftForge.EVENT_BUS.register(new InitSounds());
//		MinecraftForge.EVENT_BUS.register(new MCTimer());
//		MinecraftForge.EVENT_BUS.register(new PortalEvent());
//		MinecraftForge.EVENT_BUS.register(new GenPortal());
		MinecraftForge.EVENT_BUS.register(new RemoveUnresolvedItems());
		MinecraftForge.EVENT_BUS.register(new AncientLabyrinthGenerator.AncientWorldBuildProcessor());
	}

	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event) {
		for (Block block : InitBlocks.BLOCKS) {
			if (block instanceof IHasModel) {
				((IHasModel)block).registerModels();
			}
		}
		for(Item item : InitItems.ITEMS) {
			if(item instanceof IHasModel) {
				((IHasModel)item).registerModels();
			}
		}
	}
}
