package com.artur114.thaumrota.common.generation.terraingen;

import com.artur114.thaumrota.common.generation.portal.base.AncientPortalsProcessor;
import com.artur114.thaumrota.common.init.InitBiomes;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
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

        int taintId = Biome.getIdForBiome(InitBiomes.TAINT);
        int[] mutations = new int[] {
            Biome.getIdForBiome(InitBiomes.TAINT_MOUNTAINS),
            Biome.getIdForBiome(InitBiomes.TAINT_SEA),
            Biome.getIdForBiome(InitBiomes.TAINT_WASTELAND)
        };

        for (int j = 0; j < areaHeight; j++) {
            for (int i = 0; i < areaWidth; i++) {
                int x = (i + areaX);
                int y = (j + areaY);
                this.initChunkSeed(x, y);
                int k = aint1[i + j * areaWidth];
                if (GenLayersHandler.isCollideToAnyPortal(portalsGenerationPos, x, y, biomeSize + 1, 2)) {
                    if (GenLayersHandler.isCollideToAnyPortal(portalsGenerationPos, x, y, biomeSize + 1, 1)) {
                        int randId = this.nextInt(mutations.length + 2);
                        int id = randId >= mutations.length ? 0 : randId;
                        aint[i + j * areaWidth] = 1000000 + (this.nextInt(4) == 0 || GenLayersHandler.isCollideToAnyPortal(portalsGenerationPos, x, y, biomeSize + 1, 0) ? taintId : mutations[id]);
                    } else {
                        aint[i + j * areaWidth] = 1000000 + (this.nextInt(4) == 0 ? taintId : GenLayersHandler.getRandomIntOnArray(mutations, this.nextInt(mutations.length), Biome.getIdForBiome(InitBiomes.TAINT_MOUNTAINS)));
                    }
                } else {
                    aint[i + j * areaWidth] = k;
                }
            }
        }

        return aint;
    }
}