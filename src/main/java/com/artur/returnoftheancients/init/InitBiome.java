package com.artur.returnoftheancients.init;

import java.util.ArrayList;
import java.util.List;

import com.artur.returnoftheancients.generation.biomes.BiomeAncientLabyrinth;
import com.artur.returnoftheancients.utils.interfaces.IBiome;
import net.minecraft.world.biome.Biome;

public class InitBiome {


    public static final List<Biome> BIOMES = new ArrayList<>();
    public static final Biome ANCIENT_LABYRINTH = new BiomeAncientLabyrinth();

    public static void initBiomes() {
        for (Biome biome : BIOMES) {
            if (biome instanceof IBiome) {
                ((IBiome) biome).registerBiome();
            }
        }
    }
}