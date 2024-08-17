package com.artur.returnoftheancients.handlers;

import com.artur.returnoftheancients.ancientworldgeneration.structurebuilder.CustomGenStructure;
import com.artur.returnoftheancients.client.TrapParticleFlame;
import com.artur.returnoftheancients.commads.*;
import com.artur.returnoftheancients.init.InitBlocks;
import com.artur.returnoftheancients.init.InitItems;
import com.artur.returnoftheancients.init.InitTileEntity;
import com.artur.returnoftheancients.main.MainR;
import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.network.ClientPacketMisc;
import com.artur.returnoftheancients.network.ClientPacketPlayerNBTData;
import com.artur.returnoftheancients.network.ServerPacketTpToHome;
import com.artur.returnoftheancients.tileentity.BlockTileEntity;
import com.artur.returnoftheancients.utils.interfaces.IHasModel;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

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

	public static void registerTileEntity() {
		System.out.println(InitTileEntity.TILE_ENTITIES.size());
		for (BlockTileEntity block : InitTileEntity.TILE_ENTITIES) {
			GameRegistry.registerTileEntity(block.getTileEntityClass(), block.getRegistryName().toString());
			System.out.println(block.getRegistryName().toString());
		}
	}

	public static void preInitRegistries()
    {
	}

	public static void registerPackets() {
		int id = 0;
		MainR.NETWORK.registerMessage(new ServerPacketTpToHome.HandlerTTH(), ServerPacketTpToHome.class, id++, Side.SERVER);
		MainR.NETWORK.registerMessage(new ClientPacketPlayerNBTData.HandlerPND(), ClientPacketPlayerNBTData.class, id++, Side.CLIENT);
		MainR.NETWORK.registerMessage(new ClientPacketMisc.HandlerM(), ClientPacketMisc.class, id++, Side.CLIENT);
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

	public static void registerCommands(FMLServerStartingEvent event) {
		if (TRAConfigs.Any.debugMode) {
			event.registerServerCommand(new TestCommand());
			event.registerServerCommand(new TestCommand2());
			event.registerServerCommand(new DataManager());
			event.registerServerCommand(new GenAncientLabyrinth());
			event.registerServerCommand(new Command());
		}
		event.registerServerCommand(new TRACommand());
	}

	public 	static void registerStructures() {
		CustomGenStructure.setUseBinary();
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

		CustomGenStructure.put("ancient_developer_platform");
		CustomGenStructure.put("ancient_boss");
		CustomGenStructure.put("ancient_entry_way");
		CustomGenStructure.put("ancient_door");
		CustomGenStructure.put("ancient_door1");
		CustomGenStructure.put("ancient_border_cap");

		CustomGenStructure.setUseAir();
		CustomGenStructure.put("ancient_area");
		CustomGenStructure.put("ancient_exit");
		CustomGenStructure.put("air_cube");
		CustomGenStructure.register();
	}
}
