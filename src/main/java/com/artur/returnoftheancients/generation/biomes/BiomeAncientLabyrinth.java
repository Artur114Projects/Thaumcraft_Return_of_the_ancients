package com.artur.returnoftheancients.generation.biomes;

import com.artur.returnoftheancients.init.InitBiome;
import com.artur.returnoftheancients.init.InitDimensions;
import com.artur.returnoftheancients.referense.Referense;
import com.artur.returnoftheancients.utils.interfaces.IBiome;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.entities.monster.EntityEldritchCrab;
import thaumcraft.common.entities.monster.EntityEldritchGuardian;
import thaumcraft.common.entities.monster.EntityInhabitedZombie;
import thaumcraft.common.entities.monster.EntityMindSpider;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BiomeAncientLabyrinth extends Biome implements IBiome {

    // Тип BiomeType, определяющий генерацию биома
    private final BiomeManager.BiomeType biomeType = BiomeManager.BiomeType.COOL;
    // Вес биома
    private int weight = 0;
    // Типы биома
    private final BiomeDictionary.Type[] types = new BiomeDictionary.Type[]{BiomeDictionary.Type.DEAD, BiomeDictionary.Type.DENSE};

    public static String NAME = "ancient entry";
    public static List<SpawnListEntry> spawnListEntryList = new ArrayList<>();

    public BiomeAncientLabyrinth() {
        super(new BiomeProperties(NAME)
                .setBaseHeight(1.0F)
                .setHeightVariation(0.2F)
                .setRainDisabled()
                .setTemperature(0.2F)
                .setWaterColor(0)
//                .setBaseHeight(64.0F)
        );
        this.setRegistryName(Referense.MODID, NAME);
        this.topBlock = Blocks.BEDROCK.getDefaultState();
        this.fillerBlock = Blocks.BEDROCK.getDefaultState();
        spawnListEntryList.add(new SpawnListEntry(EntityEldritchGuardian.class, 100, 4, 14));
        spawnListEntryList.add(new SpawnListEntry(EntityMindSpider.class, 30, 20, 100));
        spawnListEntryList.add(new SpawnListEntry(EntityInhabitedZombie.class, 50, 4, 8));
        this.spawnableCaveCreatureList = spawnListEntryList;
        InitBiome.BIOMES.add(this);
        System.out.println("BiomeAncientLabyrinth is constructed, registry name: " + NAME);
    }


    @Override
    public void registerBiome() {
        ForgeRegistries.BIOMES.register(this);
        BiomeDictionary.addTypes(this, this.types);
        if (this.weight > 0) {
            BiomeManager.addBiome(biomeType, new BiomeManager.BiomeEntry(this, this.weight));
            BiomeManager.addSpawnBiome(this);
        }
    }
}
