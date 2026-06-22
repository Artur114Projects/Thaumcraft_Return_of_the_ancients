package com.artur114.thaumrota.common.biomes.decorate;

import com.artur114.bananalib.mc.BananaMC;
import com.artur114.thaumrota.common.handlers.MiscHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.BiomeDictionary;

import java.util.Random;

public abstract class WorldGeneratorBiomeWhiteList extends WorldGenerator {
    protected final CheckType checkType;
    protected byte[] biomes;

    protected WorldGeneratorBiomeWhiteList(CheckType checkType) {
        this.checkType = checkType;
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        if (this.biomes == null) {
            Biome[] rawBiomes = this.biomesWhiteList();
            this.biomes = new byte[rawBiomes.length];
            for (int i = 0; i != this.biomes.length; i++) {
                this.biomes[i] = (byte) (Biome.getIdForBiome(rawBiomes[i]) & 255);
            }
        }
        if (checkType.check(worldIn, position, this.biomes)) {
            return this.gen(worldIn, rand, position);
        }
        return false;
    }

    public abstract boolean gen(World world, Random rand, BlockPos pos);
    protected abstract Biome[] biomesWhiteList();

    public enum CheckType {
        BLOCK,
        FAST,
        FULL;

        public boolean check(World worldIn, BlockPos position, byte[] biomes) {
            switch (this) {
                case BLOCK:
                    return BananaMC.arrayContains(biomes, BananaMC.biomeIdOnPos(worldIn, position));
                case FAST:
                    return fastCheckChunkContainsAnyOnBiomeArray(worldIn.getChunkFromBlockCoords(position), biomes);
                case FULL:
                    return fullCheckChunkContainsAnyOnBiomeArray(worldIn.getChunkFromBlockCoords(position), biomes);
                default:
                    throw new IllegalStateException("WTF!?");
            }
        }

        private static boolean fullCheckChunkContainsAnyOnBiomeArray(Chunk chunk, byte[] biomeArray) {
            byte[] chunkBiomeArray = chunk.getBiomeArray();
            return arrayContainsAny(chunkBiomeArray, biomeArray);
        }

        private static boolean fastCheckChunkContainsAnyOnBiomeArray(Chunk chunk, byte[] biomeArray) {
            byte[] chunkBiomeArray = chunk.getBiomeArray();
            return arrayContainsAny(biomeArray, chunkBiomeArray[0], chunkBiomeArray[15 * 16], chunkBiomeArray[15 + 15 * 16], chunkBiomeArray[15]);
        }

        private static boolean arrayContainsAny(byte[] array, byte... params) {
            for (int i : array) {
                for (int j : params) {
                    if (i == j) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}

