package com.artur.returnoftheancients.generation.biomes;

import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;

public class BiomeAncientLayer1 extends BiomeBase {
    public BiomeAncientLayer1(String registryName, Biome.BiomeProperties properties, EBiome eBiome) {
        super(registryName, properties, eBiome);
        this.topBlock = Blocks.AIR.getDefaultState();
        this.fillerBlock = Blocks.AIR.getDefaultState();
    }

    @Override
    public int getWaterColorMultiplier() {
        return 0xABE1CB;
    }
}
