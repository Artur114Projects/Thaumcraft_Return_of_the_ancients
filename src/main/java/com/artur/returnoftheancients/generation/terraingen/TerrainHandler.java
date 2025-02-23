package com.artur.returnoftheancients.generation.terraingen;


import com.artur.returnoftheancients.generation.biomes.BiomeTaint;
import com.artur.returnoftheancients.handlers.MiscHandler;
import com.artur.returnoftheancients.init.InitBiome;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.layer.GenLayer;
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

        if (MiscHandler.fullCheckChunkContainsAnyOnBiomeArray(chunk, InitBiome.TAINT_BIOMES_ID)) {
            ((WorldServer) e.getWorld()).getChunkProvider().loadChunk(e.getChunkPos().x, e.getChunkPos().z, () -> BiomeTaint.decorateCustom(e.getWorld(), e.getRand(), e.getChunkPos(), biomeArray));
        }
    }


    @SubscribeEvent
    public void populate(PopulateChunkEvent.Populate e) {
        Chunk chunk = e.getWorld().getChunkFromChunkCoords(e.getChunkX(), e.getChunkZ());

        notPopulateInTaintBiome(e, chunk);
    }

    private void notPopulateInTaintBiome(PopulateChunkEvent.Populate e, Chunk chunk) {
        if (e.getType() != PopulateChunkEvent.Populate.EventType.LAKE && e.getType() != PopulateChunkEvent.Populate.EventType.LAVA && e.getType() != PopulateChunkEvent.Populate.EventType.ANIMALS) {
            return;
        }
        if (e.getWorld().provider.getDimension() != 0) {
            return;
        }

        if (MiscHandler.fastCheckChunkContainsAnyOnBiomeArray(chunk, InitBiome.TAINT_BIOMES_ID)) {
            e.setResult(Event.Result.DENY);
        }
    }
}