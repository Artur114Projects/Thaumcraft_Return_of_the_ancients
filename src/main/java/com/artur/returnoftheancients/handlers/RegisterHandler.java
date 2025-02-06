package com.artur.returnoftheancients.handlers;

import com.artur.returnoftheancients.ancientworldgeneration.structurebuilder.CustomGenStructure;
import com.artur.returnoftheancients.ancientworldgeneration.structurebuilder.util.ITRAStructureIsUseEBS;
import com.artur.returnoftheancients.blocks.BaseBlockContainer;
import com.artur.returnoftheancients.client.fx.particle.util.ParticleSprite;
import com.artur.returnoftheancients.commads.*;
import com.artur.returnoftheancients.init.InitBlocks;
import com.artur.returnoftheancients.init.InitItems;
import com.artur.returnoftheancients.init.InitParticleSprite;
import com.artur.returnoftheancients.init.InitTileEntity;
import com.artur.returnoftheancients.main.MainR;
import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.network.*;
import com.artur.returnoftheancients.referense.Referense;
import com.artur.returnoftheancients.tileentity.BlockTileEntity;
import com.artur.returnoftheancients.utils.interfaces.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.Thaumcraft;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.AspectRegistryEvent;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ScanBlock;
import thaumcraft.api.research.ScanItem;
import thaumcraft.api.research.ScanningManager;

import static thaumcraft.api.items.ItemsTC.*;
import static net.minecraft.init.Items.*;

@EventBusSubscriber(modid = Referense.MODID)
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

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void registerParticlesTexture(TextureStitchEvent.Pre e){
		registerParticleSprites(e);
	}

	public static void registerParticleSprites(TextureStitchEvent.Pre e) {
		if (TRAConfigs.Any.debugMode) System.out.println("Registering particles!");
		for (ParticleSprite sprite : InitParticleSprite.PARTICLES_SPRITES) {
			sprite.register(e);
			if (TRAConfigs.Any.debugMode) System.out.println("registered: " + sprite);
		}
	}

	public static void registerTileEntity() {
		for (BlockTileEntity<?> block : InitTileEntity.TILE_ENTITIES) {
			GameRegistry.registerTileEntity(block.getTileEntityClass(), block.getRegistryName().toString());
		}
		for (BaseBlockContainer<?> block : InitTileEntity.TILE_ENTITIES_CONTAINER) {
			GameRegistry.registerTileEntity(block.getTileEntityClass(), block.getRegistryName().toString());
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
		MainR.NETWORK.registerMessage(new ServerPacketTileAncientTeleportData.HandlerTATD(), ServerPacketTileAncientTeleportData.class, id++, Side.SERVER);
		MainR.NETWORK.registerMessage(new ServerPacketSyncContainerHideSlots.HandlerSHS(), ServerPacketSyncContainerHideSlots.class, id++, Side.SERVER);
		MainR.NETWORK.registerMessage(new ServerPacketGetWeather.HandlerGW(), ServerPacketGetWeather.class, id++, Side.SERVER);
		MainR.NETWORK.registerMessage(new ClientPacketSendWeather.HandlerSW(), ClientPacketSendWeather.class, id++, Side.CLIENT);
		MainR.NETWORK.registerMessage(new ClientPacketSyncAncientPortals.HandlerSAP(), ClientPacketSyncAncientPortals.class, id++, Side.CLIENT);
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
			event.registerServerCommand(new TpToPortal());
			event.registerServerCommand(new Command());
		}
		event.registerServerCommand(new TRACommand());
	}

	public static void registerResearch() {
		ResearchCategories.registerCategory("ANCIENT_WORLD", "UNLOCKELDRITCH", new AspectList().add(Aspect.ELDRITCH, 1), new ResourceLocation(Referense.MODID, "textures/gui/ancient_logo.png"), new ResourceLocation(Thaumcraft.MODID, "textures/gui/gui_research_back_6.jpg"));
		ThaumcraftApi.registerResearchLocation(new ResourceLocation(Referense.MODID, "research/ancient_world_base"));
		ThaumcraftApi.registerResearchLocation(new ResourceLocation(Referense.MODID, "research/ancient_world_things"));
		ScanningManager.addScannableThing(new ScanItem("!PRIMAL_BLADE", new ItemStack(InitItems.PRIMAL_BLADE)));
		ScanningManager.addScannableThing(new ScanBlock("!FIRE_TRAP", InitTileEntity.FIRE_TRAP));
	}

	public 	static void registerStructures() {
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
		CustomGenStructure.setUseEBS((x, y, z, state) -> {
            if (state.getBlock().equals(InitBlocks.BOSS_TRIGGER_BLOCK)) return false;
			if (state.getBlock().equals(InitTileEntity.ELDRITCH_TRAP)) return false;
			if (state.getBlock().equals(InitTileEntity.FIRE_TRAP)) return false;
			if (state.getBlock().equals(BlocksTC.nitor.get(EnumDyeColor.BLACK))) return false;
            return true;
		});
		CustomGenStructure.register();

		CustomGenStructure.setUseBinary();
		CustomGenStructure.put("taint_tree_big");
		CustomGenStructure.put("taint_tree_small");
		CustomGenStructure.put("ancient_boss");
		CustomGenStructure.put("ancient_crossroads_trap");
		CustomGenStructure.put("ancient_developer_platform");

		CustomGenStructure.put("ancient_entry_way");
		CustomGenStructure.put("ancient_door");
		CustomGenStructure.put("ancient_door1");
		CustomGenStructure.put("ancient_border_cap");
		CustomGenStructure.put("ancient_door_rock_rotate-1");
		CustomGenStructure.put("ancient_door_rock_rotate-2");
		CustomGenStructure.put("ancient_portal_floor");

		CustomGenStructure.setUseAir();
		CustomGenStructure.put("ancient_portal_air_cube");
		CustomGenStructure.put("ancient_portal");
		CustomGenStructure.put("ancient_area");
		CustomGenStructure.put("ancient_exit");
		CustomGenStructure.register();
	}

	public static void registerTCRecipes() {
		ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(Referense.MODID + ":portal_compass"), new InfusionRecipe(
				"START",
				new ItemStack(InitItems.COMPASS),
				4,
				new AspectList().add(Aspect.ELDRITCH, 125).add(Aspect.DARKNESS, 75).add(Aspect.SENSES, 100),
				COMPASS, mechanismComplex, new ItemStack(plate, 1, 3), morphicResonator, new ItemStack(plate, 1, 3))
		);
		ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(Referense.MODID + ":soul_binder"), new InfusionRecipe(
				"SOUL_BINDER",
				new ItemStack(InitItems.SOUL_BINDER),
				1,
				new AspectList().add(Aspect.TRAP, 75).add(Aspect.SOUL, 100),
				Blocks.SOUL_SAND, new ItemStack(plate, 1, 3), new ItemStack(plate, 1, 3), new ItemStack(plate, 1, 3), new ItemStack(plate, 1, 3))
		);

	}

	@SubscribeEvent
	public static void registerAspects(AspectRegistryEvent event) {
		addAspectsPB(event);
		addAspectsSB(event);
	}

	private static void addAspectsPB(AspectRegistryEvent event) {
		ItemStack myItem = new ItemStack(InitItems.PRIMAL_BLADE);

		AspectList aspects = new AspectList();
		aspects.add(Aspect.FIRE, 20);
		aspects.add(Aspect.WATER, 20);
		aspects.add(Aspect.AIR, 20);
		aspects.add(Aspect.EARTH, 20);
		aspects.add(Aspect.AVERSION, 10);

		event.register.registerObjectTag(myItem, aspects);
	}

	private static void addAspectsSB(AspectRegistryEvent event) {
		ItemStack myItem = new ItemStack(InitItems.SOUL_BINDER);

		AspectList aspects = new AspectList();
		aspects.add(Aspect.SOUL, 10);
		aspects.add(Aspect.TRAP, 4);

		event.register.registerObjectTag(myItem, aspects);
	}
}
