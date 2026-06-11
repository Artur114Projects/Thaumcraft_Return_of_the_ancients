package com.artur114.thaumrota.registry;

import com.artur114.bananalib.mc.registry.ann.AutoInstantiate;
import com.artur114.bananalib.mc.registry.data.PacketRegData;
import com.artur114.bananalib.mc.registry.data.PacketRegDataList;
import com.artur114.bananalib.mc.registry.interf.IHasCraftRecipe;
import com.artur114.bananalib.mc.registry.interf.IHasNetworkPacket;
import com.artur114.bananalib.mc.registry.interf.ILoadStageInit;
import com.artur114.bananalib.mc.registry.interf.ILoadStagePre;
import com.artur114.thaumrota.common.blocks.BlockStairsBase;
import com.artur114.thaumrota.common.generation.terraingen.TerrainHandler;
import com.artur114.thaumrota.common.init.InitBlocks;
import com.artur114.thaumrota.common.init.InitItems;
import com.artur114.thaumrota.common.network.*;
import com.artur114.thaumrota.main.ThaumRotA;
import com.artur114.thaumrota.server.commads.TRACommand;
import com.artur114.thaumrota.server.event.PublicSStartingEvent;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
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

import java.util.Collections;
import java.util.List;

import static net.minecraft.init.Items.COMPASS;
import static thaumcraft.api.items.ItemsTC.*;
import static thaumcraft.api.items.ItemsTC.plate;

@AutoInstantiate
public class RegistryHandler implements ILoadStagePre, ILoadStageInit, IHasNetworkPacket, IHasCraftRecipe {
    @SubscribeEvent
    public void serverStarting(PublicSStartingEvent e) {
        e.fml().registerServerCommand(new TRACommand());
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onBlockRegisterLater(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(InitBlocks.ELDRITCH_STAIRS = new BlockStairsBase(BlocksTC.stoneEldritchTile.getDefaultState(), "eldritch_stairs").setForCreative().setCreativeTab(ThaumRotA.CREATIVE_TAB));
        event.getRegistry().register(InitBlocks.ANCIENT_STAIRS = new BlockStairsBase(BlocksTC.stoneAncient.getDefaultState(), "ancient_stairs").setForCreative().setCreativeTab(ThaumRotA.CREATIVE_TAB));
        ThaumRotA.REGISTER_BUS.registerBlock(InitBlocks.ELDRITCH_STAIRS, InitBlocks.ANCIENT_STAIRS);
    }

    @Override
    public void onPreInit() {
        MinecraftForge.TERRAIN_GEN_BUS.register(new TerrainHandler());
        ThaumRotA.INTERNAL_EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onInit() {
        this.registerTCRecipes();
        this.registerResearch();
    }

    @Override
    public List<ResourceLocation> registerCraftRecipesName() {
        return Collections.singletonList(ThaumRotA.loc("soul_binder_clear"));
    }

    @Override
    public List<PacketRegData> registerPacketsData() {
        PacketRegDataList l = new PacketRegDataList();
        l.apply(ServerPacketTpToHome.HandlerTTH.class, ServerPacketTpToHome.class, Side.SERVER);
        l.apply(ClientPacketPlayerNBTData.HandlerPND.class, ClientPacketPlayerNBTData.class, Side.CLIENT);
        l.apply(ClientPacketMisc.HandlerM.class, ClientPacketMisc.class, Side.CLIENT);
        l.apply(ServerPacketTileAncientTeleportData.HandlerTATD.class, ServerPacketTileAncientTeleportData.class, Side.SERVER);
        l.apply(ServerPacketSyncContainerHideSlots.HandlerSHS.class, ServerPacketSyncContainerHideSlots.class, Side.SERVER);
        l.apply(ServerPacketGetWeather.HandlerGW.class, ServerPacketGetWeather.class, Side.SERVER);
        l.apply(ClientPacketSendWeather.HandlerSW.class, ClientPacketSendWeather.class, Side.CLIENT);
        l.apply(ClientPacketSyncAncientPortals.HandlerSAP.class, ClientPacketSyncAncientPortals.class, Side.CLIENT);
        l.apply(ClientPacketSyncProtectedChunk.HandlerSPC.class, ClientPacketSyncProtectedChunk.class, Side.CLIENT);
        l.apply(ClientPacketSyncEnergySystem.HandlerSES.class, ClientPacketSyncEnergySystem.class, Side.CLIENT);
        l.apply(ClientPacketSyncAncientLayer1s.HandlerSAL.class, ClientPacketSyncAncientLayer1s.class, Side.CLIENT);
        return l.list();
    }

    public void registerResearch() {
        ResearchCategories.registerCategory("ANCIENT_WORLD_LEGACY", "UNLOCKELDRITCH", new AspectList().add(Aspect.ELDRITCH, 1), new ResourceLocation(ThaumRotA.MODID, "textures/gui/ancient_logo.png"), new ResourceLocation(Thaumcraft.MODID, "textures/gui/gui_research_back_6.jpg"));
        ThaumcraftApi.registerResearchLocation(new ResourceLocation(ThaumRotA.MODID, "research/ancient_world_base_legacy"));
        ThaumcraftApi.registerResearchLocation(new ResourceLocation(ThaumRotA.MODID, "research/ancient_world_things_legacy"));
        ScanningManager.addScannableThing(new ScanItem("!PRIMAL_BLADE", new ItemStack(InitItems.PRIMAL_BLADE)));

        ResearchCategories.registerCategory("ANCIENT_WORLD_LEGACY", "UNLOCKELDRITCH", new AspectList().add(Aspect.ELDRITCH, 1), new ResourceLocation(ThaumRotA.MODID, "textures/gui/ancient_logo.png"), new ResourceLocation(Thaumcraft.MODID, "textures/gui/gui_research_back_6.jpg"));
    }

    public void registerTCRecipes() {
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumRotA.MODID + ":portal_compass"), new InfusionRecipe(
                "START",
                new ItemStack(InitItems.COMPASS),
                4,
                new AspectList().add(Aspect.ELDRITCH, 125).add(Aspect.DARKNESS, 75).add(Aspect.SENSES, 100),
                COMPASS, mechanismComplex, new ItemStack(plate, 1, 3), morphicResonator, new ItemStack(plate, 1, 3))
        );
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumRotA.MODID + ":soul_binder"), new InfusionRecipe(
                "SOUL_BINDER",
                new ItemStack(InitItems.SOUL_BINDER),
                1,
                new AspectList().add(Aspect.TRAP, 75).add(Aspect.SOUL, 100),
                Blocks.SOUL_SAND, new ItemStack(plate, 1, 3), new ItemStack(plate, 1, 3), new ItemStack(plate, 1, 3), new ItemStack(plate, 1, 3))
        );
    }

    @SubscribeEvent
    public void registerAspects(AspectRegistryEvent event) {
        addAspectsPrimalB(event);
        addAspectsSoulB(event);
    }

    private void addAspectsPrimalB(AspectRegistryEvent event) {
        ItemStack myItem = new ItemStack(InitItems.PRIMAL_BLADE);

        AspectList aspects = new AspectList();
        aspects.add(Aspect.FIRE, 20);
        aspects.add(Aspect.WATER, 20);
        aspects.add(Aspect.AIR, 20);
        aspects.add(Aspect.EARTH, 20);
        aspects.add(Aspect.AVERSION, 10);

        event.register.registerObjectTag(myItem, aspects);
    }

    private void addAspectsSoulB(AspectRegistryEvent event) {
        ItemStack myItem = new ItemStack(InitItems.SOUL_BINDER);

        AspectList aspects = new AspectList();
        aspects.add(Aspect.SOUL, 10);
        aspects.add(Aspect.TRAP, 4);

        event.register.registerObjectTag(myItem, aspects);
    }
}
