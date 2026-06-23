package com.artur114.thaumrota.common.event.managers;

import com.artur114.bananalib.mc.BananaMC;
import com.artur114.thaumrota.common.biomes.BiomeTaint;
import com.artur114.thaumrota.common.init.InitBiomes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TaintBiomeEventsManager {
    public void tickEventWorldTickEvent(TickEvent.WorldTickEvent e) {
        if (!e.world.isRemote) {
            if (e.phase != TickEvent.Phase.START) {
                return;
            }

            if (e.world.provider.getDimension() == 0) {
                MinecraftServer server = e.world.getMinecraftServer();
                if (server != null && server.getTickCounter() % 40 == 0) {
                    int taintChunks = 0;
                    for (Chunk chunk : ((WorldServer) e.world).getChunkProvider().getLoadedChunks()) {
                        if (chunk == null) {
                            continue;
                        }

                        if (BananaMC.chunkContainsBiomeTypeOnCorners(chunk, InitBiomes.TAINT_TYPE_L)) {
                            BiomeTaint.chunkHasBiomeUpdate(chunk);
                            taintChunks++;
                        }
                    }
                    BiomeTaint.taintChunks = taintChunks;
                }
            }
        }
    }

    public void livingSpawnEventCheckSpawn(LivingSpawnEvent.CheckSpawn e) {
        Biome biome = e.getWorld().getBiome(e.getEntity().getPosition());
        if (biome instanceof BiomeTaint) {
            if (!BiomeTaint.canSpawn(e.getEntity(), (BiomeTaint) biome)) {
                e.setResult(Event.Result.DENY);
            }
        }
    }
}
