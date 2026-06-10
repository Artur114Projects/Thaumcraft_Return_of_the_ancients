package com.artur114.thaumrota.common.biomes;

import com.artur114.bananalib.mc.base.IBiomeData;
import com.artur114.thaumrota.common.init.InitBiomes;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;

public enum EBiome implements IBiomeData {

    TAINT_SEA(InitBiomes.TAINT_TYPE, InitBiomes.TAINT_TYPE_L, InitBiomes.TAINT_TYPE_L_SEA),
    TAINT_EDGE(InitBiomes.TAINT_TYPE, InitBiomes.TAINT_TYPE_EDGE),
    TAINT(InitBiomes.TAINT_TYPE, InitBiomes.TAINT_TYPE_L),
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

    @Override
    public BiomeManager.BiomeType biomeType() {
        return this.biomeType;
    }

    @Override
    public int weight() {
        return this.weight;
    }

    @Override
    public BiomeDictionary.Type[] types() {
        return this.types;
    }
}