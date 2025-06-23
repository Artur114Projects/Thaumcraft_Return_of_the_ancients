package com.artur.returnoftheancients.generation.biomes;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import thaumcraft.common.entities.monster.EntityEldritchGuardian;
import thaumcraft.common.entities.monster.EntityInhabitedZombie;
import thaumcraft.common.entities.monster.EntityMindSpider;

import java.util.ArrayList;
import java.util.List;

public class BiomeAncientLayer1 extends BiomeBase {
    public BiomeAncientLayer1(String registryName, Biome.BiomeProperties properties, EBiome eBiome) {
        super(registryName, properties, eBiome);
        this.topBlock = Blocks.AIR.getDefaultState();
        this.fillerBlock = Blocks.AIR.getDefaultState();
    }

    @Override
    public int getGrassColorAtPos(BlockPos pos) {
        return 0;
    }

    @Override
    public int getFoliageColorAtPos(BlockPos pos) {
        return 0;
    }
}
