package com.artur.returnoftheancients.generation.terraingen;

import com.artur.returnoftheancients.generation.portal.base.AncientPortalsProcessor;
import com.artur.returnoftheancients.init.InitBiome;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerBiome;
import org.jetbrains.annotations.NotNull;

public class GenLayerBiomeTRA extends GenLayerBiome {
    private final ChunkPos[] portalsGenerationPos;

    public GenLayerBiomeTRA(long p_i45560_1_, GenLayer p_i45560_3_, WorldType p_i45560_4_, ChunkGeneratorSettings p_i45560_5_, long worldSeedBase) {
        super(p_i45560_1_, p_i45560_3_, p_i45560_4_, p_i45560_5_);
        portalsGenerationPos = new ChunkPos[AncientPortalsProcessor.portalsCount];
        GenLayersHandler.initPortalsPosOnWorld(portalsGenerationPos, worldSeedBase);
    }

    @Override
    public int @NotNull[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
        int[] aint1 = super.getInts(areaX, areaY, areaWidth, areaHeight);

        int taintId = Biome.getIdForBiome(InitBiome.TAINT);

        for (int j = 0; j < areaHeight; j++) {
            for (int i = 0; i < areaWidth; i++) {
                int x = (i + areaX);
                int y = (j + areaY);
                this.initChunkSeed(x, y);
                if (GenLayersHandler.isCollideToAnyPortal(portalsGenerationPos, x, y, 4, 2)) {
                    if (GenLayersHandler.isCollideToAnyPortal(portalsGenerationPos, x, y, 4, 1)) {
                        int randId = this.nextInt(InitBiome.TAINT_BIOMES_L_MUTATION_INT_ID.length + 10);
                        int id = randId >= InitBiome.TAINT_BIOMES_L_MUTATION_INT_ID.length ? 0 : randId;
                        aint1[i + j * areaWidth] = this.nextInt(4) == 0 ? taintId : InitBiome.TAINT_BIOMES_L_MUTATION_INT_ID[id];
                    } else {
                        aint1[i + j * areaWidth] = this.nextInt(4) == 0 ? taintId : GenLayersHandler.getRandomIntOnArray(InitBiome.TAINT_BIOMES_L_MUTATION_INT_ID, this.nextInt(InitBiome.TAINT_BIOMES_L_MUTATION_INT_ID.length), Biome.getIdForBiome(InitBiome.TAINT_MOUNTAINS));
                    }
                }
            }
        }
        return aint1;
    }
}
