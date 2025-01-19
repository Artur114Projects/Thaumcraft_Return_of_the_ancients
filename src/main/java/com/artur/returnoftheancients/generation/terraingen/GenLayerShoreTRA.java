package com.artur.returnoftheancients.generation.terraingen;


import com.artur.returnoftheancients.handlers.HandlerR;
import com.artur.returnoftheancients.init.InitBiome;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeJungle;
import net.minecraft.world.biome.BiomeMesa;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraftforge.common.BiomeDictionary;

public class GenLayerShoreTRA extends GenLayer {
    public GenLayerShoreTRA(long seed, GenLayer parent) {
        super(seed);
        this.parent = parent;
    }

    public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
        int[] aint = this.parent.getInts(areaX - 2, areaY - 2, areaWidth + 4, areaHeight + 4);
        int[] aint1 = IntCache.getIntCache(areaWidth * areaHeight);

        for (int i = 0; i < areaHeight; ++i) {
            for (int j = 0; j < areaWidth; ++j) {
                this.initChunkSeed((long) (j + areaX), (long) (i + areaY));
                int j1 = j + 2;
                int i1 = i + 2;
                int areaWidth1 = areaWidth + 4;
                int k = aint[j1 + i1 * areaWidth1];
                Biome biome = Biome.getBiome(k);

                if (k == Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND)) {
                    int j2 = aint[j1 + 0 + (i1 - 1) * areaWidth1];
                    int i3 = aint[j1 + 1 + (i1 + 0) * areaWidth1];
                    int l3 = aint[j1 - 1 + (i1 + 0) * areaWidth1];
                    int k4 = aint[j1 + 0 + (i1 + 1) * areaWidth1];

                    if (j2 != Biome.getIdForBiome(Biomes.OCEAN) && i3 != Biome.getIdForBiome(Biomes.OCEAN) && l3 != Biome.getIdForBiome(Biomes.OCEAN) && k4 != Biome.getIdForBiome(Biomes.OCEAN)) {
                        aint1[j + i * areaWidth] = k;
                    } else {
                        aint1[j + i * areaWidth] = Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND_SHORE);
                    }
                } else if (biome != null && biome.getBiomeClass() == BiomeJungle.class) {
                    int j4 = aint[j1 + 0 + (i1 - 1) * areaWidth1];
                    int i2 = aint[j1 + 1 + (i1 + 0) * areaWidth1];
                    int l2 = aint[j1 - 1 + (i1 + 0) * areaWidth1];
                    int k3 = aint[j1 + 0 + (i1 + 1) * areaWidth1];

                    if (this.isJungleCompatible(i2) && this.isJungleCompatible(l2) && this.isJungleCompatible(k3) && this.isJungleCompatible(j4)) {
                        if (!isBiomeOceanic(i2) && !isBiomeOceanic(l2) && !isBiomeOceanic(k3) && !isBiomeOceanic(j4)) {
                            aint1[j + i * areaWidth] = k;
                        } else {
                            aint1[j + i * areaWidth] = Biome.getIdForBiome(Biomes.BEACH);
                        }
                    } else {
                        aint1[j + i * areaWidth] = Biome.getIdForBiome(Biomes.JUNGLE_EDGE);
                    }
                } else if (k != Biome.getIdForBiome(Biomes.EXTREME_HILLS) && k != Biome.getIdForBiome(Biomes.EXTREME_HILLS_WITH_TREES) && k != Biome.getIdForBiome(Biomes.EXTREME_HILLS_EDGE)) {
                    if (biome != null && biome.isSnowyBiome()) {
                        this.replaceIfNeighborOcean(aint, aint1, j, i, areaWidth, k, Biome.getIdForBiome(Biomes.COLD_BEACH));
                    } else if (k != Biome.getIdForBiome(Biomes.MESA) && k != Biome.getIdForBiome(Biomes.MESA_ROCK)) {
                        if (k != Biome.getIdForBiome(Biomes.OCEAN) && k != Biome.getIdForBiome(Biomes.DEEP_OCEAN) && k != Biome.getIdForBiome(Biomes.RIVER) && k != Biome.getIdForBiome(Biomes.SWAMPLAND)) {
                            if (!HandlerR.arrayContains(InitBiome.TAINT_BIOMES_INT_ID, k)) {
                                int j3 = aint[j1 + 0 + (i1 - 1) * areaWidth1];
                                int i4 = aint[j1 + 1 + (i1 + 0) * areaWidth1];
                                int l1 = aint[j1 - 1 + (i1 + 0) * areaWidth1];
                                int k2 = aint[j1 + 0 + (i1 + 1) * areaWidth1];

                                if (!isBiomeOceanic(l1) && !isBiomeOceanic(k2) && !isBiomeOceanic(j3) && !isBiomeOceanic(i4)) {
                                    aint1[j + i * areaWidth] = k;
                                } else {
                                    aint1[j + i * areaWidth] = Biome.getIdForBiome(Biomes.BEACH);
                                }
                            } else {
                                if (isAllBiomesOnRangeTaint0(aint, i1, j1, areaWidth1, 2)) { // my
                                    aint1[j + i * areaWidth] = k;
                                } else {
                                    aint1[j + i * areaWidth] = Biome.getIdForBiome(InitBiome.TAINT_EDGE);
                                }
                            }
                        } else {
                            aint1[j + i * areaWidth] = k;
                        }
                    } else {
                        int j2 = aint[j1 + 0 + (i1 - 1) * areaWidth1];
                        int i3 = aint[j1 + 1 + (i1 + 0) * areaWidth1];
                        int l3 = aint[j1 - 1 + (i1 + 0) * areaWidth1];
                        int k4 = aint[j1 + 0 + (i1 + 1) * areaWidth1];

                        if (!isBiomeOceanic(l3) && !isBiomeOceanic(i3) && !isBiomeOceanic(j2) && !isBiomeOceanic(k4)) {
                            if (this.isMesa(l3) && this.isMesa(i3) && this.isMesa(j2) && this.isMesa(k4)) {
                                aint1[j + i * areaWidth] = k;
                            } else {
                                aint1[j + i * areaWidth] = Biome.getIdForBiome(Biomes.DESERT);
                            }
                        } else {
                            aint1[j + i * areaWidth] = k;
                        }
                    }
                } else {
                    this.replaceIfNeighborOcean(aint, aint1, j, i, areaWidth, k, Biome.getIdForBiome(Biomes.STONE_BEACH));
                }

            }
        }

        return aint1;
    }

    private boolean isAllBiomesOnRangeTaint0(int[] aint, int i1, int j1, int areaWidth1, int range) {
        for (int i = i1 - range; i != i1 + range + 1; i++) {
            for (int j = j1 - range; j != j1 + range + 1; j++) {
                if (i == i1 && j == j1) {
                    continue;
                }
                if (i == i1 - range && j == j1 - range) {
                    continue;
                }
                if (i == i1 - range && j == j1 + range) {
                    continue;
                }
                if (i == i1 + range && j == j1 - range) {
                    continue;
                }
                if (i == i1 + range && j == j1 + range) {
                    continue;
                }
                if (!isTaint(aint[j + i * areaWidth1])) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isAllBiomesOnRangeTaint1(int[] aint, int i1, int j1, int areaWidth1, int range) {
        for (int range1 = 1; range1 != range + 1; range1++) {
            for (int i = 0; i != 4 * range1; i++) {
                double angle = ((Math.PI * 2) / 4 * range1) * i;
                if (!isTaint(aint[MathHelper.floor((j1 + range1 * Math.cos(angle)) + (i1 + range1 * Math.sin(angle)) * areaWidth1)])) {
                    return false;
                }
            }
        }
        return true;
    }


    private void replaceIfNeighborOcean(int[] aint, int[] aint1, int j, int i, int areaWidth, int k, int biomeIDA) {
        if (isBiomeOceanic(k)) {
            aint1[j + i * areaWidth] = k;
        } else {
            int j2 = j + 2;
            int i2 = i + 2;
            int areaWidth1 = areaWidth + 4;
            int j1 = aint[j2 + 0 + (i2 - 1) * areaWidth1];
            int i1 = aint[j2 + 1 + (i2 + 0) * areaWidth1];
            int l1 = aint[j2 - 1 + (i2 + 0) * areaWidth1];
            int k1 = aint[j2 + 0 + (i2 + 1) * areaWidth1];

            if (!isBiomeOceanic(i1) && !isBiomeOceanic(j1) && !isBiomeOceanic(k1) && !isBiomeOceanic(l1)) {
                aint1[j + i * areaWidth] = k;
            } else {
                aint1[j + i * areaWidth] = biomeIDA;
            }
        }
    }

    private boolean isJungleCompatible(int biomeIDA) {
        Biome biome = Biome.getBiome(biomeIDA);
        if (biome != null && biome.getBiomeClass() == BiomeJungle.class) {
            return true;
        } else {
            return biomeIDA == Biome.getIdForBiome(Biomes.JUNGLE_EDGE) || biomeIDA == Biome.getIdForBiome(Biomes.JUNGLE) || biomeIDA == Biome.getIdForBiome(Biomes.JUNGLE_HILLS) || biomeIDA == Biome.getIdForBiome(Biomes.FOREST) || biomeIDA == Biome.getIdForBiome(Biomes.TAIGA) || isBiomeOceanic(biomeIDA);
        }
    }

    private boolean isMesa(int biomeIDA) {
        return Biome.getBiome(biomeIDA) instanceof BiomeMesa;
    }

    private boolean isTaint(int biomeIDA) {
        Biome biome = Biome.getBiome(biomeIDA);
        if (biome == null) {
            return false;
        }
        return BiomeDictionary.hasType(biome, InitBiome.TAINT_TYPE);
    }
}