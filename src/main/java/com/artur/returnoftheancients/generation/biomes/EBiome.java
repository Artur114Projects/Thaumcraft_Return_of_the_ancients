package com.artur.returnoftheancients.generation.biomes;

import com.artur.returnoftheancients.init.InitBiome;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import thaumcraft.common.world.biomes.BiomeGenMagicalForest;

public enum EBiome {

    TAINT_EDGE(InitBiome.TAINT_TYPE, InitBiome.TAINT_TYPE_EDGE),
    TAINT(InitBiome.TAINT_TYPE, InitBiome.TAINT_TYPE_L),
    ANCIENT(BiomeDictionary.Type.DEAD, BiomeDictionary.Type.DENSE);

    private final BiomeManager.BiomeType biomeType;

    private final int weight;

    private final BiomeDictionary.Type[] types;

    EBiome(BiomeDictionary.Type... types) {
        this(null, 0, types);
    }

    EBiome(BiomeManager.BiomeType biomeType, int weight, BiomeDictionary.Type... types) {
        this.biomeType = biomeType;
        this.weight = weight;
        this.types = types;
    }

    public BiomeManager.BiomeType getBiomeType() {
        return this.biomeType;
    }

    public int getWeight() {
        return this.weight;
    }

    public BiomeDictionary.Type[] getTypes() {
        return this.types;
    }
}