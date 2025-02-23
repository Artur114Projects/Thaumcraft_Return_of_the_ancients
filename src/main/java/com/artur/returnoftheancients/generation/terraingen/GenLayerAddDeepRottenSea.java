package com.artur.returnoftheancients.generation.terraingen;

import com.artur.returnoftheancients.init.InitBiome;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerAddDeepRottenSea extends GenLayer {
    public GenLayerAddDeepRottenSea(long seed, GenLayer parent) {
        super(seed);

        this.parent = parent;
    }

    @Override
    public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
        int[] aint1 = this.parent.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
        int[] aint = IntCache.getIntCache(areaWidth * areaHeight);

        int taintId = Biome.getIdForBiome(InitBiome.TAINT);
        int seaId = Biome.getIdForBiome(InitBiome.TAINT_SEA);

        for (int j = 0; j < areaHeight; j++) {
            for (int i = 0; i < areaWidth; i++) {
                int areaWidth1 = areaWidth + 2;
                int i1 = i + 1;
                int j1 = j + 1;

                int k = aint1[i1 + j1 * areaWidth1];

                int x = (i + areaX);
                int y = (j + areaY);

                aint[i + j * areaWidth] = k;

                if (GenLayersHandler.isAllBiomesOnRangeEqualsInt0(aint1, Biome.getIdForBiome(InitBiome.TAINT_SEA), i1, j1, areaWidth1, 1)) {
                    aint[i + j * areaWidth] = Biome.getIdForBiome(InitBiome.TAINT_DEEP_SEA);
                }
            }
        }

        return aint;
    }
}

