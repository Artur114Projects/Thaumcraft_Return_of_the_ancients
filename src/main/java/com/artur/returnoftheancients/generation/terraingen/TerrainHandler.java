package com.artur.returnoftheancients.generation.terraingen;


import com.artur.returnoftheancients.generation.biomes.BiomeTaint;
import com.artur.returnoftheancients.handlers.HandlerR;
import com.artur.returnoftheancients.init.InitBiome;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraftforge.event.terraingen.BiomeEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.WorldTypeEvent.InitBiomeGens;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class TerrainHandler {
    @SubscribeEvent
    public void initializeAllBiomeGenerators(InitBiomeGens event) {
        GenLayer[] genlayer = GenLayerTRA.initializeAllBiomeGenerators(event.getSeed(), event.getWorldType());
        event.setNewBiomeGens(genlayer);
    }

    @SubscribeEvent
    public static void decoratePost(DecorateBiomeEvent.Post e) {
        Chunk chunk = e.getWorld().getChunkFromChunkCoords(e.getChunkPos().x, e.getChunkPos().z);
        byte[] biomeArray = chunk.getBiomeArray();

        if (HandlerR.arrayContainsAny(biomeArray, InitBiome.TAINT_BIOMES_ID)) {
            BiomeTaint.decorateCustom(e.getWorld(), e.getRand(), e.getChunkPos(), biomeArray);
        }
    }

    // TODO: Не работает, починить!
    @SubscribeEvent
    public void decorate(DecorateBiomeEvent.Decorate e) {
        Chunk chunk = e.getWorld().getChunkFromChunkCoords(e.getChunkPos().x, e.getChunkPos().z);
        byte[] biomeArray = chunk.getBiomeArray();

        notLake(e, biomeArray);
    }

    private void notLake(DecorateBiomeEvent.Decorate e, byte[] biomeArray) {
        if (e.getType() != DecorateBiomeEvent.Decorate.EventType.LAKE_WATER && e.getType() != DecorateBiomeEvent.Decorate.EventType.LAKE_LAVA) {
            return;
        }

        byte i = biomeArray[0];
        byte j = biomeArray[15 * 16];
        byte k = biomeArray[15 + 15 * 16];
        byte l = biomeArray[15];

        if (HandlerR.arrayContainsAny(InitBiome.TAINT_BIOMES_ID, i) || HandlerR.arrayContainsAny(InitBiome.TAINT_BIOMES_ID, j) || HandlerR.arrayContainsAny(InitBiome.TAINT_BIOMES_ID, k) || HandlerR.arrayContainsAny(InitBiome.TAINT_BIOMES_ID, l)) {
            e.setResult(Event.Result.DENY);
            System.out.println("Not lake!!!");
        }
    }
}