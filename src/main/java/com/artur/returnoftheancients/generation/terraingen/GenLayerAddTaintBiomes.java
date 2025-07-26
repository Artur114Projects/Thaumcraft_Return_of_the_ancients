package com.artur.returnoftheancients.generation.terraingen;

import com.artur.returnoftheancients.init.InitBiome;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerAddTaintBiomes extends GenLayer {
    public GenLayerAddTaintBiomes(long seed, GenLayer parent) {
        super(seed);

        this.parent = parent;
    }

    @Override
    public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
        int[] aint1 = this.parent.getInts(areaX - 2, areaY - 2, areaWidth + 4, areaHeight + 4);
        int[] aint = IntCache.getIntCache(areaWidth * areaHeight);

        int mountainsId = Biome.getIdForBiome(InitBiome.TAINT_MOUNTAINS);
        int taintId = Biome.getIdForBiome(InitBiome.TAINT);
        int seaId = Biome.getIdForBiome(InitBiome.TAINT_SEA);

        for (int j = 0; j < areaHeight; j++) {
            for (int i = 0; i < areaWidth; i++) {
                int areaWidth1 = areaWidth + 4;
                int i1 = i + 2;
                int j1 = j + 2;

                int k = aint1[i1 + j1 * areaWidth1];

                int x = (i + areaX);
                int y = (j + areaY);


                aint[i + j * areaWidth] = k;

                if (k == taintId) {
                    int j3 = aint1[i1 + 0 + (j1 - 1) * areaWidth1];
                    int i4 = aint1[i1 + 1 + (j1 + 0) * areaWidth1];
                    int l1 = aint1[i1 - 1 + (j1 + 0) * areaWidth1];
                    int k2 = aint1[i1 + 0 + (j1 + 1) * areaWidth1];

                    if (isBiomeSea(l1) || isBiomeSea(k2) || isBiomeSea(j3) || isBiomeSea(i4)) {
                        aint[i + j * areaWidth] = Biome.getIdForBiome(InitBiome.TAINT_BEACH);
                    }
                }

                this.initChunkSeed(x, y);
                if (k == mountainsId && GenLayersHandler.isAllBiomesOnRangeEqualsInt0(aint1, mountainsId, i1, j1, areaWidth1, 1) && this.nextInt(7) == 0) {
                    aint[i + j * areaWidth] = Biome.getIdForBiome(InitBiome.TAINT_EXTREME_MOUNTAINS);
                }

                if (k == seaId && GenLayersHandler.hasBiomeOnRange0(aint1, Biome.getIdForBiome(InitBiome.TAINT_PLATEAU), i1, j1, areaWidth1, 1)) {
                    aint[i + j * areaWidth] = Biome.getIdForBiome(InitBiome.TAINT_BEACH);
                }
            }
        }

        return aint;
    }

    private boolean isBiomeSea(int biome) {
        return biome == Biome.getIdForBiome(InitBiome.TAINT_SEA);
    }

    private boolean isBiomeMountains(int biome) {
        return biome == Biome.getIdForBiome(InitBiome.TAINT_MOUNTAINS);
    }
}
