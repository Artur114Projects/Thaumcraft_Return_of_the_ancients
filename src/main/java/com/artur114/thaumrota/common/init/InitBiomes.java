package com.artur114.thaumrota.common.init;

import com.artur114.bananalib.mc.registry.ann.RegistryEntry;
import com.artur114.bananalib.mc.registry.interf.ILoadStagePre;
import com.artur114.thaumrota.common.biomes.BiomeAncientLayer1;
import com.artur114.thaumrota.common.biomes.BiomeTaint;
import com.artur114.thaumrota.common.biomes.EBiome;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.world.biomes.BiomeHandler;

public class InitBiomes {
    public static final BiomeDictionary.Type TAINT_TYPE_L_SEA = BiomeDictionary.Type.getType("TAINT_R_L_SEA");
    public static final BiomeDictionary.Type TAINT_TYPE_EDGE = BiomeDictionary.Type.getType("TAINT_R_EDGE");
    public static final BiomeDictionary.Type TAINT_TYPE_L = BiomeDictionary.Type.getType("TAINT_R_L");
    public static final BiomeDictionary.Type TAINT_TYPE = BiomeDictionary.Type.getType("TAINT_R");

    @RegistryEntry
    private static final ILoadStagePre preInit = () -> {
        BiomeHandler.registerBiomeInfo(TAINT_TYPE, 0.04F, Aspect.FLUX, false, 0.0F);
        BiomeHandler.registerBiomeInfo(TAINT_TYPE_L, 0.04F, Aspect.FLUX, false, 0.0F);
        BiomeHandler.registerBiomeInfo(TAINT_TYPE_L_SEA, 0.04F, Aspect.FLUX, false, 0.0F);
    };
    @RegistryEntry
    public static final Biome PRE_TERMAL_ZONE = new BiomeTaint("pre_termal_zone_r", new Biome.BiomeProperties("Pre termal zone").setWaterColor(0x310042).setBaseHeight(0.4F).setRainfall(0.0F).setHeightVariation(0.1F).setRainDisabled(), EBiome.TAINT, BiomeTaint.TaintType.BEACH);
    @RegistryEntry
    public static final Biome TAINT_WASTELAND = new BiomeTaint("taint_wasteland_r", new Biome.BiomeProperties("Scorched Earth").setWaterColor(0x310042).setBaseHeight(0.2F).setRainfall(0.0F).setHeightVariation(0.1F).setRainDisabled(), EBiome.TAINT, BiomeTaint.TaintType.WASTELAND);
    @RegistryEntry
    public static final Biome INFERNAL_CRATER = new BiomeTaint("infernal_crater_r", new Biome.BiomeProperties("Infernal Crater").setWaterColor(0x310042).setBaseHeight(-1.8F).setRainfall(0.0F).setHeightVariation(0.2F).setRainDisabled(), EBiome.TAINT, BiomeTaint.TaintType.WASTELAND);
    @RegistryEntry
    public static final Biome TAINT_EDGE = new BiomeTaint("taint_edge_r", new Biome.BiomeProperties("Taint Land Edge").setWaterColor(0x310042).setBaseHeight(3.4F).setRainfall(0.7F).setHeightVariation(0.1F).setSnowEnabled().setTemperature(0.2F), EBiome.TAINT_EDGE, BiomeTaint.TaintType.EDGE);
    @RegistryEntry
    public static final Biome TAINT_EXTREME_MOUNTAINS = new BiomeTaint("taint_mountains_extreme_r", new Biome.BiomeProperties("Taint Extreme Rocks").setWaterColor(0x310042).setBaseHeight(2.6F).setRainfall(1.0F).setHeightVariation(0.6F).setTemperature(0.22F), EBiome.TAINT, BiomeTaint.TaintType.HILLS);
    @RegistryEntry
    public static final Biome TAINT_MOUNTAINS = new BiomeTaint("taint_mountains_r", new Biome.BiomeProperties("Taint Rocks").setWaterColor(0x310042).setBaseHeight(1.0F).setRainfall(1.0F).setHeightVariation(0.6F).setTemperature(0.22F), EBiome.TAINT, BiomeTaint.TaintType.HILLS);
    @RegistryEntry
    public static final Biome TAINT_PLATEAU = new BiomeTaint("taint_plateau_r", new Biome.BiomeProperties("Taint Plateau").setWaterColor(0x310042).setBaseHeight(0.1F).setRainfall(1.0F).setHeightVariation(0.0F), EBiome.TAINT, BiomeTaint.TaintType.NORMAL);
    @RegistryEntry
    public static final Biome TAINT_BEACH = new BiomeTaint("taint_beach_r", new Biome.BiomeProperties("Rotten Beach").setWaterColor(0x310042).setBaseHeight(0.01F).setRainfall(1.0F).setHeightVariation(0.0F), EBiome.TAINT, BiomeTaint.TaintType.BEACH);
    @RegistryEntry
    public static final Biome TAINT_SEA = new BiomeTaint("taint_sea_r", new Biome.BiomeProperties("Rotten Sea").setWaterColor(0x310042).setBaseHeight(-1.2F).setRainfall(1.0F).setHeightVariation(0.1F), EBiome.TAINT_SEA, BiomeTaint.TaintType.SEA);
    @RegistryEntry
    public static final Biome TAINT = new BiomeTaint("taint_r", new Biome.BiomeProperties("Taint Plains").setWaterColor(0x310042).setBaseHeight(0.2F).setRainfall(1.0F).setHeightVariation(0.1F), EBiome.TAINT, BiomeTaint.TaintType.NORMAL);
    @RegistryEntry
    public static final Biome ANCIENT_LABYRINTH = new BiomeAncientLayer1("ancient_layer1", new Biome.BiomeProperties("Ancient layer 1").setRainfall(0.0F).setRainDisabled(), EBiome.ANCIENT);
}