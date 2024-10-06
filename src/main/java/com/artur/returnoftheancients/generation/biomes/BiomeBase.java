package com.artur.returnoftheancients.generation.biomes;

import com.artur.returnoftheancients.init.InitBiome;
import com.artur.returnoftheancients.referense.Referense;
import com.artur.returnoftheancients.utils.interfaces.IBiome;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public abstract class BiomeBase extends Biome implements IBiome {

    private final BiomeManager.BiomeType biomeType;

    private final int weight;

    private final BiomeDictionary.Type[] types;

    public BiomeBase(String registryName, Biome.BiomeProperties properties, EBiome eBiome) {
        super(properties);
        this.setRegistryName(Referense.MODID, registryName);
        this.biomeType = eBiome.getBiomeType();
        this.weight = eBiome.getWeight();
        this.types = eBiome.getTypes();
        InitBiome.BIOMES.add(this);
    }

    @Override
    public void registerBiome() {
        ForgeRegistries.BIOMES.register(this);
        BiomeDictionary.addTypes(this, this.types);

        if (this.weight > 0) {
            BiomeManager.addBiome(biomeType, new BiomeEntry(this, this.weight));
            BiomeManager.addSpawnBiome(this);
            System.out.println("register biome " + this.getBiomeName());
        }
    }
}