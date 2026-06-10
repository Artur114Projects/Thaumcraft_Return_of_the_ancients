package com.artur114.thaumrota.common.generation.terraingen;

import com.artur114.thaumrota.common.init.InitBiomes;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import org.jetbrains.annotations.NotNull;

public class GenLayerAddTaintBeach extends GenLayer {

    public GenLayerAddTaintBeach(long seed, GenLayer parent) {
        super(seed);

        this.parent = parent;
    }

    @Override
    public int @NotNull [] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
        int[] aint1 = this.parent.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
        int[] aint = IntCache.getIntCache(areaWidth * areaHeight);

        int mountainsId = Biome.getIdForBiome(InitBiomes.TAINT_MOUNTAINS);
        int wastelandId = Biome.getIdForBiome(InitBiomes.TAINT_WASTELAND);
        int taintId = Biome.getIdForBiome(InitBiomes.TAINT);
        int seaId = Biome.getIdForBiome(InitBiomes.TAINT_SEA);

        for (int j = 0; j < areaHeight; j++) {
            for (int i = 0; i < areaWidth; i++) {
                int areaWidth1 = areaWidth + 2;
                int i1 = i + 1;
                int j1 = j + 1;

                int k = aint1[i1 + j1 * areaWidth1];

                int x = (i + areaX);
                int y = (j + areaY);

                aint[i + j * areaWidth] = k;

                this.initChunkSeed(x, y);

                if (k == taintId) {
                    int j3 = aint1[i1 + 0 + (j1 - 1) * areaWidth1];
                    int i4 = aint1[i1 + 1 + (j1 + 0) * areaWidth1];
                    int l1 = aint1[i1 - 1 + (j1 + 0) * areaWidth1];
                    int k2 = aint1[i1 + 0 + (j1 + 1) * areaWidth1];

                    if (isBiomeSea(l1) || isBiomeSea(k2) || isBiomeSea(j3) || isBiomeSea(i4)) {
                        aint[i + j * areaWidth] = Biome.getIdForBiome(InitBiomes.TAINT_BEACH);
                    }
                }

                if (k == seaId && GenLayersHandler.hasBiomeOnRange0(aint1, Biome.getIdForBiome(InitBiomes.TAINT_PLATEAU), i1, j1, areaWidth1, 1)) {
                    aint[i + j * areaWidth] = Biome.getIdForBiome(InitBiomes.TAINT_BEACH);
                }
            }
        }
        return aint;
    }

    private boolean isBiomeSea(int biome) {
        return biome == Biome.getIdForBiome(InitBiomes.TAINT_SEA);
    }
}
