package com.artur.returnoftheancients.init;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.artur.returnoftheancients.generation.biomes.BiomeAncientLayer1;
import com.artur.returnoftheancients.generation.biomes.BiomeTaint;
import com.artur.returnoftheancients.generation.biomes.EBiome;
import com.artur.returnoftheancients.util.interfaces.IBiome;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.world.biomes.BiomeHandler;

public class InitBiome {

    public static final BiomeDictionary.Type TAINT_TYPE_L_SEA = BiomeDictionary.Type.getType("TAINT_R_L_SEA");
    public static final BiomeDictionary.Type TAINT_TYPE_EDGE = BiomeDictionary.Type.getType("TAINT_R_EDGE");
    public static final BiomeDictionary.Type TAINT_TYPE_L = BiomeDictionary.Type.getType("TAINT_R_L");
    public static final BiomeDictionary.Type TAINT_TYPE = BiomeDictionary.Type.getType("TAINT_R");

    public static final List<Biome> BIOMES = new ArrayList<>();

    public static final Biome TAINT_WASTELAND = new BiomeTaint("taint_wasteland_r", new Biome.BiomeProperties("Scorched Earth").setWaterColor(0x310042).setBaseHeight(0.2F).setRainfall(0.0F).setHeightVariation(0.1F).setRainDisabled(), EBiome.TAINT, BiomeTaint.TaintType.WASTELAND);
    public static final Biome TAINT_EDGE = new BiomeTaint("taint_edge_r", new Biome.BiomeProperties("Taint Land Edge").setWaterColor(0x310042).setBaseHeight(3.4F).setRainfall(0.7F).setHeightVariation(0.1F).setSnowEnabled().setTemperature(0.2F), EBiome.TAINT_EDGE, BiomeTaint.TaintType.EDGE);
    public static final Biome TAINT_EXTREME_MOUNTAINS = new BiomeTaint("taint_mountains_extreme_r", new Biome.BiomeProperties("Taint Extreme Rocks").setWaterColor(0x310042).setBaseHeight(2.6F).setRainfall(1.0F).setHeightVariation(0.6F).setTemperature(0.22F), EBiome.TAINT, BiomeTaint.TaintType.HILLS);
    public static final Biome TAINT_MOUNTAINS = new BiomeTaint("taint_mountains_r", new Biome.BiomeProperties("Taint Rocks").setWaterColor(0x310042).setBaseHeight(1.0F).setRainfall(1.0F).setHeightVariation(0.6F).setTemperature(0.22F), EBiome.TAINT, BiomeTaint.TaintType.HILLS);
    public static final Biome TAINT_PLATEAU = new BiomeTaint("taint_plateau_r", new Biome.BiomeProperties("Taint Plateau").setWaterColor(0x310042).setBaseHeight(0.1F).setRainfall(1.0F).setHeightVariation(0.0F), EBiome.TAINT, BiomeTaint.TaintType.NORMAL);
    public static final Biome TAINT_BEACH = new BiomeTaint("taint_beach_r", new Biome.BiomeProperties("Rotten Beach").setWaterColor(0x310042).setBaseHeight(0.01F).setRainfall(1.0F).setHeightVariation(0.0F), EBiome.TAINT, BiomeTaint.TaintType.BEACH);
    public static final Biome TAINT_SEA = new BiomeTaint("taint_sea_r", new Biome.BiomeProperties("Rotten Sea").setWaterColor(0x310042).setBaseHeight(-1.2F).setRainfall(1.0F).setHeightVariation(0.1F), EBiome.TAINT_SEA, BiomeTaint.TaintType.SEA);
    public static final Biome TAINT = new BiomeTaint("taint_r", new Biome.BiomeProperties("Taint Plains").setWaterColor(0x310042).setBaseHeight(0.2F).setRainfall(1.0F).setHeightVariation(0.1F), EBiome.TAINT, BiomeTaint.TaintType.NORMAL);
    public static final Biome ANCIENT_LABYRINTH = new BiomeAncientLayer1("ancient_layer1", new Biome.BiomeProperties("Ancient layer 1").setRainfall(0.0F).setRainDisabled().setWaterColor(0), EBiome.ANCIENT);

    public static byte[] TAINT_BIOMES_ID = new byte[0];
    public static byte[] TAINT_BIOMES_L_ID = new byte[0];
    public static byte[] TAINT_BIOMES_EDGE_ID = new byte[0];
    public static byte[] TAINT_BIOMES_L_SEA_ID = new byte[0];

    public static int[] TAINT_BIOMES_INT_ID = new int[0];
    public static int[] TAINT_BIOMES_L_INT_ID = new int[0];
    public static int[] TAINT_BIOMES_EDGE_INT_ID = new int[0];
    public static int[] TAINT_BIOMES_L_MUTATION_INT_ID = new int[0];


    public static void initBiomes() {
        for (Biome biome : BIOMES) {
            if (biome instanceof IBiome) {
                ((IBiome) biome).registerBiome();
            }
        }

        registerBiomeInfo();
    }

    public static void registerBiomeInfo() {
        BiomeHandler.registerBiomeInfo(TAINT_TYPE, 0.0F, Aspect.FLUX, false, 0.0F);
        BiomeHandler.registerBiomeInfo(TAINT_TYPE_L, 0.0F, Aspect.FLUX, false, 0.0F);
        BiomeHandler.registerBiomeInfo(TAINT_TYPE_L_SEA, 0.0F, Aspect.FLUX, false, 0.0F);
    }

    public static void registerBiomeArrays() {
        TAINT_BIOMES_ID = getBiomeArrayWithType(TAINT_TYPE);
        TAINT_BIOMES_L_ID = getBiomeArrayWithType(TAINT_TYPE_L);
        TAINT_BIOMES_EDGE_ID = getBiomeArrayWithType(TAINT_TYPE_EDGE);
        TAINT_BIOMES_L_SEA_ID = getBiomeArrayWithType(TAINT_TYPE_L_SEA);

        TAINT_BIOMES_INT_ID = getIntBiomeArrayWithType(TAINT_TYPE);
        TAINT_BIOMES_L_INT_ID = getIntBiomeArrayWithType(TAINT_TYPE_L);
        TAINT_BIOMES_EDGE_INT_ID = getIntBiomeArrayWithType(TAINT_TYPE_EDGE);
        TAINT_BIOMES_L_MUTATION_INT_ID = registerTaintBiomeMutations();
    }

    private static int[] registerTaintBiomeMutations() {
        return new int[] {
                Biome.getIdForBiome(TAINT_MOUNTAINS),
                Biome.getIdForBiome(TAINT_SEA),
                Biome.getIdForBiome(TAINT_WASTELAND)
        };
    }

    private static byte[] getBiomeArrayWithType(BiomeDictionary.Type type) {
        Set<Biome> biomes = BiomeDictionary.getBiomes(type);
        Iterator<Biome> iterator = biomes.iterator();
        byte[] array = new byte[biomes.size()];
        for (int i = 0; i != biomes.size(); i++) {
            array[i] = (byte) (Biome.getIdForBiome(iterator.next()) & 255);
        }
        return array;
    }

    private static int[] getIntBiomeArrayWithType(BiomeDictionary.Type type) {
        Set<Biome> biomes = BiomeDictionary.getBiomes(type);
        Iterator<Biome> iterator = biomes.iterator();
        int[] array = new int[biomes.size()];
        for (int i = 0; i != biomes.size(); i++) {
            array[i] = Biome.getIdForBiome(iterator.next());
        }
        return array;
    }
}