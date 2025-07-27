package com.artur.returnoftheancients.generation.terraingen;

import com.artur.returnoftheancients.generation.portal.base.AncientPortalsProcessor;
import com.artur.returnoftheancients.init.InitBiome;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerBiome;
import net.minecraft.world.gen.layer.IntCache;
import org.jetbrains.annotations.NotNull;

public class GenLayerAddTaintBiomes extends GenLayer {
    private final ChunkPos[] portalsGenerationPos;
    private final int biomeSize;

    public GenLayerAddTaintBiomes(long seed, GenLayer parent, long worldSeedBase, int biomeSize) {
        super(seed);
        this.portalsGenerationPos = new ChunkPos[AncientPortalsProcessor.portalsCount];
        GenLayersHandler.initPortalsPosOnWorld(this.portalsGenerationPos, worldSeedBase);
        this.biomeSize = biomeSize;
        this.parent = parent;
    }

    @Override
    public int @NotNull [] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
        int[] aint1 = this.parent.getInts(areaX, areaY, areaWidth, areaHeight);
        int[] aint = IntCache.getIntCache(areaWidth * areaHeight);

        int taintId = Biome.getIdForBiome(InitBiome.TAINT);

        for (int j = 0; j < areaHeight; j++) {
            for (int i = 0; i < areaWidth; i++) {
                int x = (i + areaX);
                int y = (j + areaY);
                this.initChunkSeed(x, y);
                int k = aint1[i + j * areaWidth];
                if (GenLayersHandler.isCollideToAnyPortal(portalsGenerationPos, x, y, biomeSize + 1, 2)) {
                    if (GenLayersHandler.isCollideToAnyPortal(portalsGenerationPos, x, y, biomeSize + 1, 1)) {
                        int randId = this.nextInt(InitBiome.TAINT_BIOMES_L_MUTATION_INT_ID.length + 2);
                        int id = randId >= InitBiome.TAINT_BIOMES_L_MUTATION_INT_ID.length ? 0 : randId;
                        aint[i + j * areaWidth] = 1000000 + (this.nextInt(4) == 0 || GenLayersHandler.isCollideToAnyPortal(portalsGenerationPos, x, y, biomeSize + 1, 0) ? taintId : InitBiome.TAINT_BIOMES_L_MUTATION_INT_ID[id]);
                    } else {
                        aint[i + j * areaWidth] = 1000000 + (this.nextInt(4) == 0 ? taintId : GenLayersHandler.getRandomIntOnArray(InitBiome.TAINT_BIOMES_L_MUTATION_INT_ID, this.nextInt(InitBiome.TAINT_BIOMES_L_MUTATION_INT_ID.length), Biome.getIdForBiome(InitBiome.TAINT_MOUNTAINS)));
                    }
                } else {
                    aint[i + j * areaWidth] = k;
                }
            }
        }

        return aint;
    }
}