package com.artur114.thaumrota.common.generation.terraingen;


import com.artur114.bananalib.mc.BananaMC;
import com.artur114.thaumrota.common.biomes.BiomeTaint;
import com.artur114.thaumrota.common.init.InitBiomes;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.WorldTypeEvent.InitBiomeGens;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber
public class TerrainHandler {
    private static final Logger log = LogManager.getLogger("ThaumRotA/TerrainGen");

    @SubscribeEvent
    public void initializeAllBiomeGenerators(InitBiomeGens event) {
        if (event.getWorldType() == WorldType.DEFAULT || event.getWorldType() == WorldType.LARGE_BIOMES) {
            GenLayer[] genlayer = GenLayerRotA.initializeAllBiomeGenerators(event.getSeed(), event.getWorldType());
            event.setNewBiomeGens(genlayer);

            log.info("Injected RotA Biome Gens");
        }
    }

    @SubscribeEvent
    public static void decoratePost(DecorateBiomeEvent.Post e) {
        Chunk chunk = e.getWorld().getChunkFromChunkCoords(e.getChunkPos().x, e.getChunkPos().z);
        byte[] biomeArray = chunk.getBiomeArray();

        if (BananaMC.chunkContainsBiomeTypeOnCorners(chunk, InitBiomes.TAINT_TYPE)) {
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

        if (BananaMC.chunkContainsBiomeType(chunk, InitBiomes.TAINT_TYPE)) {
            e.setResult(Event.Result.DENY);
        }
    }
}