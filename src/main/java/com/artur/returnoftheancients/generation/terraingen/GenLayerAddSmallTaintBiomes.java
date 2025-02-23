package com.artur.returnoftheancients.generation.terraingen;

import com.artur.returnoftheancients.init.InitBiome;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerAddSmallTaintBiomes extends GenLayerWithPortalsPos {

    public GenLayerAddSmallTaintBiomes(long seed, long worldSeed, GenLayer parent) {
        super(seed, worldSeed, parent);
    }

    @Override
    public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
        int[] aint1 = this.parent.getInts(areaX - 0, areaY - 0, areaWidth + 0, areaHeight + 0);
        int[] aint = IntCache.getIntCache(areaWidth * areaHeight);

        int taintId = Biome.getIdForBiome(InitBiome.TAINT);
        int seaId = Biome.getIdForBiome(InitBiome.TAINT_SEA);

        for (int j = 0; j < areaHeight; j++) {
            for (int i = 0; i < areaWidth; i++) {
                int areaWidth1 = areaWidth + 0;
                int i1 = i + 0;
                int j1 = j + 0;

                int k = aint1[i1 + j1 * areaWidth1];

                int x = (i + areaX);
                int y = (j + areaY);

                aint[i + j * areaWidth] = k;

                if (GenLayersHandler.isCollideToAnyPortal(portalsGenerationPos, x, y, 0, 2)) {
                    aint[i + j * areaWidth] = Biome.getIdForBiome(InitBiome.TAINT_PLATEAU);
                }
            }
        }

        return aint;
    }
}
