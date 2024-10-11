package com.artur.returnoftheancients.init;

import java.util.ArrayList;
import java.util.List;

import com.artur.returnoftheancients.generation.biomes.BiomeAncientLabyrinth;
import com.artur.returnoftheancients.generation.biomes.BiomeTaint;
import com.artur.returnoftheancients.generation.biomes.EBiome;
import com.artur.returnoftheancients.utils.interfaces.IBiome;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.world.biomes.BiomeHandler;

public class InitBiome {

    public static final BiomeDictionary.Type TAINT_TYPE = BiomeDictionary.Type.getType("TAINT_R");
    public static final BiomeDictionary.Type TAINT_TYPE_EDGE = BiomeDictionary.Type.getType("TAINT_EDGE_R", BiomeDictionary.Type.COLD);

    public static final List<Biome> BIOMES = new ArrayList<>();

    public static final Biome TAINT_EDGE = new BiomeTaint("taint_edge_r", new Biome.BiomeProperties("Taint edge").setBaseHeight(3.0F).setRainfall(0.7F).setHeightVariation(0.1F).setSnowEnabled(), EBiome.TAINT_EDGE, BiomeTaint.TaintType.EDGE);
    public static final Biome TAINT = new BiomeTaint("taint_r", new Biome.BiomeProperties("Taint").setWaterColor(0x563367).setBaseHeight(0.1F).setRainfall(1.0F).setHeightVariation(0.0F), EBiome.TAINT, BiomeTaint.TaintType.NORMAL);
    public static final Biome ANCIENT_LABYRINTH = new BiomeAncientLabyrinth("ancient_entry", new Biome.BiomeProperties("Ancient entry").setRainDisabled().setWaterColor(0), EBiome.ANCIENT);

    public static void initBiomes() {
        BiomeHandler.registerBiomeInfo(TAINT_TYPE, 0.0F, Aspect.FLUX, false, 0.0F);
        for (Biome biome : BIOMES) {
            if (biome instanceof IBiome) {
                ((IBiome) biome).registerBiome();
            }
        }
    }
}