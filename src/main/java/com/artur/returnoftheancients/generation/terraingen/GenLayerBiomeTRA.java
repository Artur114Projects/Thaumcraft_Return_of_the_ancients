package com.artur.returnoftheancients.generation.terraingen;

import com.artur.returnoftheancients.generation.generators.portal.base.AncientPortalsProcessor;
import com.artur.returnoftheancients.handlers.HandlerR;
import com.artur.returnoftheancients.init.InitBiome;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerBiome;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class GenLayerBiomeTRA extends GenLayerBiome {
    private ChunkPos[] portalsGenerationPosOverWorld = null;
    private final long worldSeedBase;

    public GenLayerBiomeTRA(long p_i45560_1_, GenLayer p_i45560_3_, WorldType p_i45560_4_, ChunkGeneratorSettings p_i45560_5_, long worldSeedBase) {
        super(p_i45560_1_, p_i45560_3_, p_i45560_4_, p_i45560_5_);
        this.worldSeedBase = worldSeedBase;
    }

    @Override
    public int @NotNull [] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
        int[] aint1 = super.getInts(areaX, areaY, areaWidth, areaHeight);

        int taintId = Biome.getIdForBiome(InitBiome.TAINT);

        for (int i = 0; i < areaWidth; i++) {
            for (int j = 0; j < areaHeight; j++) {
                int x = (i + areaX);
                int y = (j + areaY);
                if (isCollideToAnyPortalOnOverWorld(x, y)) {
                    aint1[i + j * areaWidth] = taintId;
                }
            }
        }
        return aint1;
    }

    public boolean isCollideToAnyPortalOnOverWorld(int x, int z) {
        if (portalsGenerationPosOverWorld == null) {
            portalsGenerationPosOverWorld = new ChunkPos[AncientPortalsProcessor.portalsCount];
            AncientPortalsProcessor.initPortalsPosOnWorld(portalsGenerationPosOverWorld, worldSeedBase);
        }
        for (ChunkPos pos : portalsGenerationPosOverWorld) {
            if (HandlerR.isCollide(x, z, pos.x >> 4, pos.z >> 4, 1)) {
                return true;
            }
        }
        return false;
    }
}
