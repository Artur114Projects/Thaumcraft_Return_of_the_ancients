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
import net.minecraft.init.Items;
import net.minecraft.item.Item;
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
import thaumcraft.api.aspects.AspectEventProxy;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.AspectRegistryEvent;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ScanItem;
import thaumcraft.api.research.ScanningManager;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;


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
        l.apply(ServerPacketTileAncientTeleportData.HandlerTATD.class, ServerPacketTileAncientTeleportData.class, Side.SERVER);
        l.apply(ServerPacketSyncContainerHideSlots.HandlerSHS.class, ServerPacketSyncContainerHideSlots.class, Side.SERVER);
        l.apply(ServerPacketGetWeather.HandlerGW.class, ServerPacketGetWeather.class, Side.SERVER);
        l.apply(ClientPacketSendWeather.HandlerSW.class, ClientPacketSendWeather.class, Side.CLIENT);
        l.apply(ClientPacketSyncAncientPortals.HandlerSAP.class, ClientPacketSyncAncientPortals.class, Side.CLIENT);
        l.apply(ClientPacketSyncProtectedChunk.HandlerSPC.class, ClientPacketSyncProtectedChunk.class, Side.CLIENT);
        l.apply(ClientPacketSyncEnergySystem.HandlerSES.class, ClientPacketSyncEnergySystem.class, Side.CLIENT);
        l.apply(ClientPacketSyncAncientLayer1s.HandlerSAL.class, ClientPacketSyncAncientLayer1s.class, Side.CLIENT);
        l.apply(ClientPacketCreateFX.HandlerCFX.class, ClientPacketCreateFX.class, Side.CLIENT);
        return l.list();
    }

    public void registerTCRecipes() {
        ThaumcraftApi.addInfusionCraftingRecipe(ThaumRotA.loc("portal_compass"), new InfusionRecipe(
            "UNLOCKELDRITCH",
            new ItemStack(InitItems.COMPASS),
            4,
            new AspectList().add(Aspect.ELDRITCH, 125).add(Aspect.DARKNESS, 75).add(Aspect.SENSES, 100),
            Items.COMPASS, ItemsTC.mechanismComplex, new ItemStack(ItemsTC.plate, 1, 3), ItemsTC.morphicResonator, new ItemStack(ItemsTC.plate, 1, 3))
        );
        ThaumcraftApi.addInfusionCraftingRecipe(ThaumRotA.loc("soul_binder"), new InfusionRecipe(
            "UNLOCKELDRITCH",
            new ItemStack(InitItems.SOUL_BINDER),
            2,
            new AspectList().add(Aspect.TRAP, 75).add(Aspect.SOUL, 100),
            Blocks.SOUL_SAND, new ItemStack(ItemsTC.plate, 1, 3), new ItemStack(ItemsTC.plate, 1, 3), new ItemStack(ItemsTC.plate, 1, 3), new ItemStack(ItemsTC.plate, 1, 3))
        );
        ThaumcraftApi.addInfusionCraftingRecipe(ThaumRotA.loc("ancient_fuse"), new InfusionRecipe(
            "UNLOCKELDRITCH",
            new ItemStack(InitItems.IMITATION_ANCIENT_FUSE),
            4,
            new AspectList().add(Aspect.ORDER, 200).add(Aspect.CRYSTAL, 100).add(Aspect.ELDRITCH, 75).add(Aspect.MECHANISM, 50),
            BlocksTC.crystalOrder,
            BlocksTC.stoneArcane,
            new ItemStack(ItemsTC.plate, 1, 3),
            BlocksTC.stoneArcane,
            new ItemStack(ItemsTC.plate, 1, 3),
            BlocksTC.stoneArcane,
            new ItemStack(ItemsTC.plate, 1, 3),
            BlocksTC.stoneArcane,
            new ItemStack(ItemsTC.plate, 1, 3)
        ));
    }

    @SubscribeEvent
    public void registerAspects(AspectRegistryEvent e) {
        injectAspects(e, InitItems.SOUL_BINDER, aspects -> {
            aspects.add(Aspect.SOUL, 10);
            aspects.add(Aspect.TRAP, 4);
        });
    }

    private void injectAspects(AspectRegistryEvent e, Item item, Consumer<AspectList> loader) {
        this.injectAspects(e, new ItemStack(item), loader);
    }

    private void injectAspects(AspectRegistryEvent e, ItemStack item, Consumer<AspectList> loader) {
        AspectList aspects = new AspectList();
        loader.accept(aspects);
        e.register.registerObjectTag(item, aspects);
    }
}
