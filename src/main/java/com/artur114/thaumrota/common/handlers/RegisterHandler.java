package com.artur114.thaumrota.common.handlers;

import com.artur114.thaumrota.common.blocks.BlockStairsBase;
import com.artur114.thaumrota.client.fx.particle.util.ParticleSprite;
import com.artur114.thaumrota.server.commads.CommandDataManager;
import com.artur114.thaumrota.server.commads.TRACommand;
import com.artur114.thaumrota.server.commads.TestCommand2;
import com.artur114.thaumrota.server.commads.TpToPortal;
import com.artur114.thaumrota.common.init.InitBlocks;
import com.artur114.thaumrota.common.init.InitItems;
import com.artur114.thaumrota.client.init.InitParticleSprite;
import com.artur114.thaumrota.common.network.*;
import com.artur114.thaumrota.main.ThaumicRotA;
import com.artur114.thaumrota.common.misc.RotAConfigs;
import com.artur114.thaumrota.common.util.interfaces.IHasModel;
import com.artur114.thaumrota.common.util.interfaces.IHasTileEntity;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
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
import thaumcraft.api.research.ScanItem;
import thaumcraft.api.research.ScanningManager;

import java.util.Objects;

import static thaumcraft.api.items.ItemsTC.*;
import static net.minecraft.init.Items.*;

@EventBusSubscriber(modid = ThaumicRotA.MODID)
public class RegisterHandler {
	public static void preInit() {}

	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(InitBlocks.BLOCKS_REGISTER_BUSS.toArray(new Block[0]));
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void onBlockRegisterLater(RegistryEvent.Register<Block> event) {
		event.getRegistry().register(InitBlocks.ELDRITCH_STAIRS = new BlockStairsBase(BlocksTC.stoneEldritchTile.getDefaultState(), "eldritch_stairs").setForCreative().setTRACreativeTab());
		event.getRegistry().register(InitBlocks.ANCIENT_STAIRS = new BlockStairsBase(BlocksTC.stoneAncient.getDefaultState(), "ancient_stairs").setForCreative().setTRACreativeTab());
	}

	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(InitItems.ITEMS_REGISTER_BUSS.toArray(new Item[0]));
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void registerParticlesTexture(TextureStitchEvent.Pre e) {
		registerParticleSprites(e);
	}

	public static void registerParticleSprites(TextureStitchEvent.Pre e) {
		for (ParticleSprite sprite : InitParticleSprite.PARTICLES_SPRITES) {
			sprite.register(e);
		}
	}

	public static void registerTileEntities() {
		for (Block block : InitBlocks.BLOCKS_REGISTER_BUSS) {
			if (block instanceof IHasTileEntity<?>) {
				GameRegistry.registerTileEntity(((IHasTileEntity<?>) block).tileEntityClass(), Objects.requireNonNull(block.getRegistryName()));
			}
		}
	}

	public static void registerPackets() {
		int id = 0;
		ThaumicRotA.NETWORK.registerMessage(new ServerPacketTpToHome.HandlerTTH(), ServerPacketTpToHome.class, id++, Side.SERVER);
		ThaumicRotA.NETWORK.registerMessage(new ClientPacketPlayerNBTData.HandlerPND(), ClientPacketPlayerNBTData.class, id++, Side.CLIENT);
		ThaumicRotA.NETWORK.registerMessage(new ClientPacketMisc.HandlerM(), ClientPacketMisc.class, id++, Side.CLIENT);
		ThaumicRotA.NETWORK.registerMessage(new ServerPacketTileAncientTeleportData.HandlerTATD(), ServerPacketTileAncientTeleportData.class, id++, Side.SERVER);
		ThaumicRotA.NETWORK.registerMessage(new ServerPacketSyncContainerHideSlots.HandlerSHS(), ServerPacketSyncContainerHideSlots.class, id++, Side.SERVER);
		ThaumicRotA.NETWORK.registerMessage(new ServerPacketGetWeather.HandlerGW(), ServerPacketGetWeather.class, id++, Side.SERVER);
		ThaumicRotA.NETWORK.registerMessage(new ClientPacketSendWeather.HandlerSW(), ClientPacketSendWeather.class, id++, Side.CLIENT);
		ThaumicRotA.NETWORK.registerMessage(new ClientPacketSyncAncientPortals.HandlerSAP(), ClientPacketSyncAncientPortals.class, id++, Side.CLIENT);
		ThaumicRotA.NETWORK.registerMessage(new ClientPacketSyncProtectedChunk.HandlerSPC(), ClientPacketSyncProtectedChunk.class, id++, Side.CLIENT);
		ThaumicRotA.NETWORK.registerMessage(new ClientPacketSyncEnergySystem.HandlerSES(), ClientPacketSyncEnergySystem.class, id++, Side.CLIENT);
		ThaumicRotA.NETWORK.registerMessage(new ClientPacketSyncAncientLayer1s.HandlerSAL(), ClientPacketSyncAncientLayer1s.class, id++, Side.CLIENT);
	}

	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event) {
		for (Block block : InitBlocks.BLOCKS_REGISTER_BUSS) {
			if (block instanceof IHasModel) {
				((IHasModel) block).registerModels();
			}
		}
		for(Item item : InitItems.ITEMS_REGISTER_BUSS) {
			if(item instanceof IHasModel) {
				((IHasModel)  item).registerModels();
			}
		}
	}

	public static void registerCommands(FMLServerStartingEvent event) {
		if (RotAConfigs.Any.debugMode) { // TODO: 23.02.2025 Снести!
			event.registerServerCommand(new TestCommand2());
			event.registerServerCommand(new CommandDataManager());
			event.registerServerCommand(new TpToPortal());
		}
		event.registerServerCommand(new TRACommand());
	}

	public static void registerResearch() {
		ResearchCategories.registerCategory("ANCIENT_WORLD_LEGACY", "UNLOCKELDRITCH", new AspectList().add(Aspect.ELDRITCH, 1), new ResourceLocation(ThaumicRotA.MODID, "textures/gui/ancient_logo.png"), new ResourceLocation(Thaumcraft.MODID, "textures/gui/gui_research_back_6.jpg"));
		ThaumcraftApi.registerResearchLocation(new ResourceLocation(ThaumicRotA.MODID, "research/ancient_world_base_legacy"));
		ThaumcraftApi.registerResearchLocation(new ResourceLocation(ThaumicRotA.MODID, "research/ancient_world_things_legacy"));
		ScanningManager.addScannableThing(new ScanItem("!PRIMAL_BLADE", new ItemStack(InitItems.PRIMAL_BLADE)));

		ResearchCategories.registerCategory("ANCIENT_WORLD_LEGACY", "UNLOCKELDRITCH", new AspectList().add(Aspect.ELDRITCH, 1), new ResourceLocation(ThaumicRotA.MODID, "textures/gui/ancient_logo.png"), new ResourceLocation(Thaumcraft.MODID, "textures/gui/gui_research_back_6.jpg"));
	}

	public static void registerTCRecipes() {
		ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicRotA.MODID + ":portal_compass"), new InfusionRecipe(
				"START",
				new ItemStack(InitItems.COMPASS),
				4,
				new AspectList().add(Aspect.ELDRITCH, 125).add(Aspect.DARKNESS, 75).add(Aspect.SENSES, 100),
				COMPASS, mechanismComplex, new ItemStack(plate, 1, 3), morphicResonator, new ItemStack(plate, 1, 3))
		);
		ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicRotA.MODID + ":soul_binder"), new InfusionRecipe(
				"SOUL_BINDER",
				new ItemStack(InitItems.SOUL_BINDER),
				1,
				new AspectList().add(Aspect.TRAP, 75).add(Aspect.SOUL, 100),
				Blocks.SOUL_SAND, new ItemStack(plate, 1, 3), new ItemStack(plate, 1, 3), new ItemStack(plate, 1, 3), new ItemStack(plate, 1, 3))
		);

	}

	@SubscribeEvent
	public static void registerAspects(AspectRegistryEvent event) {
		addAspectsPrimalB(event);
		addAspectsSoulB(event);
	}

	private static void addAspectsPrimalB(AspectRegistryEvent event) {
		ItemStack myItem = new ItemStack(InitItems.PRIMAL_BLADE);

		AspectList aspects = new AspectList();
		aspects.add(Aspect.FIRE, 20);
		aspects.add(Aspect.WATER, 20);
		aspects.add(Aspect.AIR, 20);
		aspects.add(Aspect.EARTH, 20);
		aspects.add(Aspect.AVERSION, 10);

		event.register.registerObjectTag(myItem, aspects);
	}

	private static void addAspectsSoulB(AspectRegistryEvent event) {
		ItemStack myItem = new ItemStack(InitItems.SOUL_BINDER);

		AspectList aspects = new AspectList();
		aspects.add(Aspect.SOUL, 10);
		aspects.add(Aspect.TRAP, 4);

		event.register.registerObjectTag(myItem, aspects);
	}
}
