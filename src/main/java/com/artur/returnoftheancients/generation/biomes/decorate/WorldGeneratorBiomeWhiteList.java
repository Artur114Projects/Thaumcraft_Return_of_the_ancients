package com.artur.returnoftheancients.generation.biomes.decorate;

import com.artur.returnoftheancients.handlers.MiscHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

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
                    return MiscHandler.arrayContains(biomes, MiscHandler.getBiomeIdOnPos(worldIn, position));
                case FAST:
                    return MiscHandler.fastCheckChunkContainsAnyOnBiomeArray(worldIn.getChunkFromBlockCoords(position), biomes);
                case FULL:
                    return MiscHandler.fullCheckChunkContainsAnyOnBiomeArray(worldIn.getChunkFromBlockCoords(position), biomes);
                default:
                    throw new IllegalStateException("WTF!?");
            }
        }
    }
}

