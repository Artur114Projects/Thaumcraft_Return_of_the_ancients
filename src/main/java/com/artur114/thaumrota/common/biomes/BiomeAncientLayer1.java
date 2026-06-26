package com.artur114.thaumrota.common.biomes;

import com.artur114.bananalib.mc.base.BBiomeBase;
import com.artur114.thaumrota.main.ThaumRotA;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;

public class BiomeAncientLayer1 extends BBiomeBase {
    public BiomeAncientLayer1(String registryName, Biome.BiomeProperties properties, EBiome eBiome) {
        super(new ResourceLocation(ThaumRotA.MODID, registryName), properties, eBiome);
        this.topBlock = Blocks.AIR.getDefaultState();
        this.fillerBlock = Blocks.AIR.getDefaultState();
    }

    @Override
    public int getWaterColorMultiplier() {
        return 0xABE1CB;
    }
}
