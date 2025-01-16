package com.artur.returnoftheancients.generation.terraingen;


import com.artur.returnoftheancients.generation.biomes.BiomeTaint;
import com.artur.returnoftheancients.init.InitBiome;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
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
        int[] aint = this.parent.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
        int[] aint2P = this.parent.getInts(areaX - 2, areaY - 2, areaWidth + 4, areaHeight + 4); // TODO: избавится!!!
        int[] aint1 = IntCache.getIntCache(areaWidth * areaHeight);

        for (int i = 0; i < areaHeight; ++i) {
            for (int j = 0; j < areaWidth; ++j) {
                this.initChunkSeed((long) (j + areaX), (long) (i + areaY));
                int k = aint[j + 1 + (i + 1) * (areaWidth + 2)];
                Biome biome = Biome.getBiome(k);

                if (k == Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND)) {
                    int j2 = aint[j + 1 + (i + 0) * (areaWidth + 2)];
                    int i3 = aint[j + 2 + (i + 1) * (areaWidth + 2)];
                    int l3 = aint[j + 0 + (i + 1) * (areaWidth + 2)];
                    int k4 = aint[j + 1 + (i + 2) * (areaWidth + 2)];

                    if (j2 != Biome.getIdForBiome(Biomes.OCEAN) && i3 != Biome.getIdForBiome(Biomes.OCEAN) && l3 != Biome.getIdForBiome(Biomes.OCEAN) && k4 != Biome.getIdForBiome(Biomes.OCEAN)) {
                        aint1[j + i * areaWidth] = k;
                    } else {
                        aint1[j + i * areaWidth] = Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND_SHORE);
                    }
                } else if (biome != null && biome.getBiomeClass() == BiomeJungle.class) {
                    int i2 = aint[j + 1 + (i + 0) * (areaWidth + 2)];
                    int l2 = aint[j + 2 + (i + 1) * (areaWidth + 2)];
                    int k3 = aint[j + 0 + (i + 1) * (areaWidth + 2)];
                    int j4 = aint[j + 1 + (i + 2) * (areaWidth + 2)];

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
                            if (!BiomeDictionary.hasType(Biome.getBiomeForId(k), InitBiome.TAINT_TYPE)) {
                                int l1 = aint[j + 1 + (i + 0) * (areaWidth + 2)];
                                int k2 = aint[j + 2 + (i + 1) * (areaWidth + 2)];
                                int j3 = aint[j + 0 + (i + 1) * (areaWidth + 2)];
                                int i4 = aint[j + 1 + (i + 2) * (areaWidth + 2)];

                                if (!isBiomeOceanic(l1) && !isBiomeOceanic(k2) && !isBiomeOceanic(j3) && !isBiomeOceanic(i4)) {
                                    aint1[j + i * areaWidth] = k;
                                } else {
                                    aint1[j + i * areaWidth] = Biome.getIdForBiome(Biomes.BEACH);
                                }
                            } else {
                                int l1 = aint2P[j + 2 + (i + 1) * (areaWidth + 4)];
                                int k1 = aint2P[j + 3 + (i + 2) * (areaWidth + 4)];
                                int j1 = aint2P[j + 1 + (i + 2) * (areaWidth + 4)];
                                int i1 = aint2P[j + 2 + (i + 3) * (areaWidth + 4)];

                                int l2 = aint2P[j + 3 + (i + 1) * (areaWidth + 4)];
                                int i2 = aint2P[j + 4 + (i + 2) * (areaWidth + 4)];
                                int j2 = aint2P[j + 1 + (i + 3) * (areaWidth + 4)];
                                int k2 = aint2P[j + 0 + (i + 2) * (areaWidth + 4)];

                                int k3 = aint2P[j + 2 + (i + 4) * (areaWidth + 4)];
                                int l3 = aint2P[j + 2 + (i + 0) * (areaWidth + 4)];
                                int i3 = aint2P[j + 1 + (i + 1) * (areaWidth + 4)];
                                int j3 = aint2P[j + 3 + (i + 3) * (areaWidth + 4)];

                                if (isTaint(l1) && isTaint(i1) && isTaint(j1) && isTaint(k1) && isTaint(l2) && isTaint(i2) && isTaint(j2) && isTaint(k2) && isTaint(l3) && isTaint(i3) && isTaint(j3) && isTaint(k3)) {
                                    aint1[j + i * areaWidth] = k;
                                } else {
                                    aint1[j + i * areaWidth] = Biome.getIdForBiome(InitBiome.TAINT_EDGE);
                                }
                            }
                        } else {
                            aint1[j + i * areaWidth] = k;
                        }
                    } else {
                        int l1 = aint[j + 1 + (i + 0) * (areaWidth + 2)];
                        int i1 = aint[j + 2 + (i + 1) * (areaWidth + 2)];
                        int j1 = aint[j + 0 + (i + 1) * (areaWidth + 2)];
                        int k1 = aint[j + 1 + (i + 2) * (areaWidth + 2)];

                        if (!isBiomeOceanic(l1) && !isBiomeOceanic(i1) && !isBiomeOceanic(j1) && !isBiomeOceanic(k1)) {
                            if (this.isMesa(l1) && this.isMesa(i1) && this.isMesa(j1) && this.isMesa(k1)) {
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

    private void replaceIfNeighborOcean(int[] aint, int[] aint1, int j, int i, int areaWidth, int k, int biomeIDA) {
        if (isBiomeOceanic(k)) {
            aint1[j + i * areaWidth] = k;
        } else {
            int i1 = aint[j + 1 + (i + 0) * (areaWidth + 2)];
            int j1 = aint[j + 2 + (i + 1) * (areaWidth + 2)];
            int k1 = aint[j + 0 + (i + 1) * (areaWidth + 2)];
            int l1 = aint[j + 1 + (i + 2) * (areaWidth + 2)];

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