package com.artur.returnoftheancients.generation.terraingen;

import com.artur.returnoftheancients.generation.generators.portal.base.AncientPortalsProcessor;
import com.artur.returnoftheancients.init.InitBiome;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import org.jetbrains.annotations.NotNull;

public class GenLayerAddMutatedTaintBiomes extends GenLayer {
    private final ChunkPos[] portalsGenerationPos;
    private final boolean zoom;

    public GenLayerAddMutatedTaintBiomes(long seed, GenLayer parent, long worldSeedBase, boolean zoom) {
        super(seed);
        this.zoom = zoom;
        this.parent = parent;
        portalsGenerationPos = new ChunkPos[AncientPortalsProcessor.portalsCount];
        TerrainGenHandler.initPortalsPosOnWorld(portalsGenerationPos, worldSeedBase);
    }

    @Override
    public int @NotNull [] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
        int[] aint1 = this.parent.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
        int[] aint = IntCache.getIntCache(areaWidth * areaHeight);
        for (int j = 0; j < areaHeight; j++) {
            for (int i = 0; i < areaWidth; i++) {
                int areaWidth1 = areaWidth + 2;

                int i1 = i + 1;
                int j1 = j + 1;

                int k = aint1[i1 + j1 * areaWidth1];

                int x = (i + areaX);
                int y = (j + areaY);

                this.initChunkSeed(x, y);


                if (zoom) {
                    if (!TerrainGenHandler.hasBiomeOnRange0(aint1, Biome.getIdForBiome(InitBiome.TAINT_EDGE), i1, j1, areaWidth1, 1)) {
                        int k1 = TerrainGenHandler.findAnyBiomeOnBAOnRange2(aint1, InitBiome.TAINT_BIOMES_L_MUTATION_INT_ID, i1, j1, areaWidth1, 1);
                        if (k1 != -1 && (this.nextInt(4) != 0 || k1 == Biome.getIdForBiome(InitBiome.TAINT_SEA))) {
                            aint[i + j * areaWidth] = k1;
                        } else {
                            aint[i + j * areaWidth] = k;
                        }
                    } else {
                        aint[i + j * areaWidth] = k;
                    }
                } else {
                    if (!TerrainGenHandler.isCollideToAnyPortal(portalsGenerationPos, x, y, 3, 1) && TerrainGenHandler.isCollideToAnyPortal(portalsGenerationPos, x, y, 3, 4) && this.nextInt(2) == 0) {
                        int k1 = InitBiome.TAINT_BIOMES_L_MUTATION_INT_ID[this.nextInt(InitBiome.TAINT_BIOMES_L_MUTATION_INT_ID.length)];
                        if (k1 == Biome.getIdForBiome(InitBiome.TAINT_MOUNTAINS) && !TerrainGenHandler.isCollideToAnyPortal(portalsGenerationPos, x, y, 3, 3)) {
                            k1 = Biome.getIdForBiome(InitBiome.TAINT);
                        }
                        aint[i + j * areaWidth] = k1;
                    } else {
                        aint[i + j * areaWidth] = k;
                    }
                }
            }
        }
        return aint;
    }
}
