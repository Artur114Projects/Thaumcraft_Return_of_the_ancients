package com.artur114.thaumrota.common.generation.terraingen;

import com.artur114.thaumrota.common.generation.portallegacy.base.AncientPortalsProcessor;
import com.artur114.thaumrota.common.init.InitBiomes;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerBiome;
import org.jetbrains.annotations.NotNull;

public class GenLayerBiomeTRA extends GenLayerBiome {
    private final ChunkPos[] portalsGenerationPos;
    private final int biomeSize;

    public GenLayerBiomeTRA(long p_i45560_1_, GenLayer p_i45560_3_, WorldType p_i45560_4_, ChunkGeneratorSettings p_i45560_5_, long worldSeedBase, int biomeSize) {
        super(p_i45560_1_, p_i45560_3_, p_i45560_4_, p_i45560_5_);
        portalsGenerationPos = new ChunkPos[AncientPortalsProcessor.portalsCount];
        GenLayersHandler.initPortalsPosOnWorld(portalsGenerationPos, worldSeedBase);
        this.biomeSize = biomeSize;
    }

    @Override
    public int @NotNull[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
        int[] aint1 = super.getInts(areaX, areaY, areaWidth, areaHeight);

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
                if (GenLayersHandler.isCollideToAnyPortal(portalsGenerationPos, x, y, biomeSize, 2)) {
                    if (GenLayersHandler.isCollideToAnyPortal(portalsGenerationPos, x, y, biomeSize, 1)) {
                        int randId = this.nextInt(mutations.length + 4);
                        int id = randId >= mutations.length ? 0 : randId;
                        aint1[i + j * areaWidth] = this.nextInt(4) == 0 || GenLayersHandler.isCollideToAnyPortal(portalsGenerationPos, x, y, biomeSize, 0) ? taintId : mutations[id];
                    } else {
                        aint1[i + j * areaWidth] = this.nextInt(4) == 0 ? taintId : GenLayersHandler.getRandomIntOnArray(mutations, this.nextInt(mutations.length), Biome.getIdForBiome(InitBiomes.TAINT_MOUNTAINS));
                    }
                }
            }
        }
        return aint1;
    }
}
