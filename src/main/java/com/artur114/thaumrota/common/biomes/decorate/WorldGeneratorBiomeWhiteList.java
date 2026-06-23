package com.artur114.thaumrota.common.biomes.decorate;

import com.artur114.bananalib.mc.BananaMC;
import com.artur114.thaumrota.common.handlers.MiscHandler;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.BiomeDictionary;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public abstract class WorldGeneratorBiomeWhiteList extends WorldGenerator {
    protected final CheckType checkType;
    protected Set<Biome> biomeSet;
    protected Biome[] biomes;

    protected WorldGeneratorBiomeWhiteList(CheckType checkType) {
        this.checkType = checkType;
    }

    @Override
    public boolean generate(@NotNull World worldIn, @NotNull Random rand, @NotNull BlockPos position) {
        if (this.biomes == null) {
            this.biomes = this.biomesWhiteList();
        }
        if (checkType.check(worldIn, position, this.biomes)) {
            return this.gen(worldIn, rand, position);
        }
        return false;
    }

    protected boolean isWhitelistedBiome(Biome biome) {
        if (this.biomes == null) {
            this.biomes = this.biomesWhiteList();
        }
        if (this.biomeSet == null) {
            this.biomeSet = new HashSet<>(Arrays.asList(this.biomes));
        }
        return this.biomeSet.contains(biome);
    }

    public abstract boolean gen(World world, Random rand, BlockPos pos);
    protected abstract Biome[] biomesWhiteList();

    public enum CheckType {
        BLOCK,
        FAST,
        FULL;

        public boolean check(World worldIn, BlockPos position, Biome[] biomes) {
            switch (this) {
                case BLOCK:
                    return BananaMC.arrayContains(biomes, worldIn.getBiome(position));
                case FAST:
                    return fastCheckChunkContainsAnyOnBiomeArray(worldIn.getChunkFromBlockCoords(position), biomes);
                case FULL:
                    return fullCheckChunkContainsAnyOnBiomeArray(worldIn.getChunkFromBlockCoords(position), biomes);
                default:
                    throw new IllegalStateException("WTF!?");
            }
        }

        private static boolean fullCheckChunkContainsAnyOnBiomeArray(Chunk chunk, Biome[] biomes) {
            byte[] chunkBiomeArray = chunk.getBiomeArray();
            for (Biome biome : biomes) {
                byte target = (byte) (Biome.getIdForBiome(biome) & 255);
                for (byte b : chunkBiomeArray) {
                    if (b == target) return true;
                }
            }
            return false;
        }

        private static boolean fastCheckChunkContainsAnyOnBiomeArray(Chunk chunk, Biome[] biomeArray) {
            byte[] chunkBiomeArray = chunk.getBiomeArray();
            return arrayContainsAny(biomeArray, chunkBiomeArray[0], chunkBiomeArray[15 * 16], chunkBiomeArray[15 + 15 * 16], chunkBiomeArray[15]);
        }

        private static boolean arrayContainsAny(Biome[] array, byte... params) {
            for (Biome biome : array) {
                int i = Biome.getIdForBiome(biome);
                for (int j : params) {
                    if (i == j) {
                        return true;
                    }
                }
            }
            return false;
        }

        private static boolean arrayContainsAny(byte[] array, byte... params) {
            for (byte i : array) {
                for (byte j : params) {
                    if (i == j) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}

