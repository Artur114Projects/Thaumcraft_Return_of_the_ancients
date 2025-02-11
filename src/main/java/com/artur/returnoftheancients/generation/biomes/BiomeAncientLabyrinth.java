package com.artur.returnoftheancients.generation.biomes;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import thaumcraft.common.entities.monster.EntityEldritchGuardian;
import thaumcraft.common.entities.monster.EntityInhabitedZombie;
import thaumcraft.common.entities.monster.EntityMindSpider;

import java.util.ArrayList;
import java.util.List;

public class BiomeAncientLabyrinth extends BiomeBase {

    public static List<SpawnListEntry> spawnListEntryList = new ArrayList<>();

    public BiomeAncientLabyrinth(String registryName, Biome.BiomeProperties properties, EBiome eBiome) {
        super(registryName, properties, eBiome);
        this.topBlock = Blocks.AIR.getDefaultState();
        this.fillerBlock = Blocks.AIR.getDefaultState();

        spawnListEntryList.add(new SpawnListEntry(EntityEldritchGuardian.class, 100, 4, 14));
        spawnListEntryList.add(new SpawnListEntry(EntityMindSpider.class, 30, 20, 100));
        spawnListEntryList.add(new SpawnListEntry(EntityInhabitedZombie.class, 50, 4, 8));

        this.spawnableCaveCreatureList = spawnListEntryList;
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
