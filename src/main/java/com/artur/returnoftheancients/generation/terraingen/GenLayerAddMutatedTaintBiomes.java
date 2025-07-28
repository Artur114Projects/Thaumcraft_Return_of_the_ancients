package com.artur.returnoftheancients.generation.terraingen;

import com.artur.returnoftheancients.init.InitBiome;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerAddMutatedTaintBiomes extends GenLayer {
    public GenLayerAddMutatedTaintBiomes(long seed, GenLayer parent) {
        super(seed);

        this.parent = parent;
    }

    @Override
    public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
        int[] aint1 = this.parent.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
        int[] aint = IntCache.getIntCache(areaWidth * areaHeight);

        int mountainsId = Biome.getIdForBiome(InitBiome.TAINT_MOUNTAINS);
        int wastelandId = Biome.getIdForBiome(InitBiome.TAINT_WASTELAND);
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

                if (k == mountainsId) {
                    int j3 = aint1[i1 + 0 + (j1 - 1) * areaWidth1];
                    int i4 = aint1[i1 + 1 + (j1 + 0) * areaWidth1];
                    int l1 = aint1[i1 - 1 + (j1 + 0) * areaWidth1];
                    int k2 = aint1[i1 + 0 + (j1 + 1) * areaWidth1];

                    this.initChunkSeed(x, y);
                    if (j3 == mountainsId && i4 == mountainsId && l1 == mountainsId && k2 == mountainsId && this.nextInt(1) == 0) {
                        aint[i + j * areaWidth] = Biome.getIdForBiome(InitBiome.TAINT_EXTREME_MOUNTAINS);
                    }
                }
            }
        }

        return aint;
    }
}
