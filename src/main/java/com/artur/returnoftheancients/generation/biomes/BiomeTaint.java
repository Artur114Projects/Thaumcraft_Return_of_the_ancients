package com.artur.returnoftheancients.generation.biomes;

import com.artur.returnoftheancients.generation.biomes.decorate.*;
import com.artur.returnoftheancients.handlers.MiscHandler;
import com.artur.returnoftheancients.init.InitBiome;
import com.artur.returnoftheancients.init.InitBlocks;
import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.entities.monster.tainted.*;

import java.util.ArrayList;
import java.util.Random;

public class BiomeTaint extends BiomeBase {
    private static final WorldGensMisc WORLD_GENS_MISC = new WorldGensMisc();

    private final WorldGenAbstractTree BIG_TREE_TAINT_FEATURE = new WorldGenTaintBigTree(false);
    private final WorldGenAbstractTree INFERNAL_SPIRES = new WorldGenInfernalSpires(false);
    private final WorldGenAbstractTree TREE_TAINT_FEATURE = new WorldGenTaintTree(false);
    private final WorldGenAbstractTree ROTTEN_SPIRES = new WorldGenRottenSpires(false);


    public final TaintType type;
    public static int taintChunks = 0;

    public BiomeTaint(String registryName, BiomeProperties properties, EBiome eBiome, TaintType type) {
        super(registryName, properties, eBiome);
        this.spawnableWaterCreatureList.clear();
        this.spawnableCaveCreatureList.clear();
        this.decorator.generateFalls = false;
        this.spawnableCreatureList.clear();
        this.spawnableMonsterList.clear();
        this.type = type;

        if (this.type == TaintType.EDGE) {
            this.decorator.extraTreeChance = -1;
            this.topBlock = Blocks.STONE.getDefaultState();
            this.fillerBlock = Blocks.STONE.getDefaultState();
            this.decorator.treesPerChunk = -1;
            this.decorator.deadBushPerChunk = -1;
            this.decorator.reedsPerChunk = -1;
            this.decorator.cactiPerChunk = -1;
            this.decorator.grassPerChunk = -1;
            this.decorator.flowersPerChunk = -1;
        } else if (type == TaintType.NORMAL || type == TaintType.HILLS) {
            if (type == TaintType.NORMAL) {
                this.decorator.extraTreeChance = 0.8F;
            }
            this.decorator.treesPerChunk = 1;
            this.spawnableCreatureList.add(new SpawnListEntry(EntityTaintSeedPrime.class, 3, 1, 1));
            this.spawnableMonsterList.add(new SpawnListEntry(EntityTaintSeedPrime.class, 3, 1, 1));
            this.spawnableCreatureList.add(new SpawnListEntry(EntityTaintSeed.class, 6, 1, 1));
            this.spawnableMonsterList.add(new SpawnListEntry(EntityTaintSeed.class, 6, 1, 1));
            this.spawnableCreatureList.add(new SpawnListEntry(EntityTaintSwarm.class, 4, 1, 2));
            this.spawnableMonsterList.add(new SpawnListEntry(EntityTaintSwarm.class, 4, 1, 2));
            this.spawnableCreatureList.add(new SpawnListEntry(EntityTaintacle.class, 12, 1, 2));
            this.spawnableMonsterList.add(new SpawnListEntry(EntityTaintacle.class, 12, 1, 2));
            this.spawnableCreatureList.add(new SpawnListEntry(EntityTaintCrawler.class, 4, 1, 6));
            this.spawnableMonsterList.add(new SpawnListEntry(EntityTaintCrawler.class, 4, 1, 6));
        } else if (type == TaintType.SEA) {
            this.decorator.extraTreeChance = 0.8F;
            this.decorator.treesPerChunk = 0;
            this.decorator.deadBushPerChunk = -1;
            this.decorator.reedsPerChunk = -1;
            this.decorator.cactiPerChunk = -1;
        } else if (type == TaintType.WASTELAND) {
            this.decorator.extraTreeChance = 0.2F;
            this.decorator.treesPerChunk = 0;
            this.decorator.deadBushPerChunk = -1;
            this.decorator.reedsPerChunk = -1;
            this.decorator.cactiPerChunk = -1;
        } else if (type == TaintType.BEACH) {
            this.decorator.extraTreeChance = -1;
            this.decorator.treesPerChunk = -1;
            this.decorator.deadBushPerChunk = -1;
            this.decorator.reedsPerChunk = -1;
            this.decorator.cactiPerChunk = -1;
        } else {
            this.decorator.extraTreeChance = -1;
            this.decorator.treesPerChunk = -1;
            this.decorator.deadBushPerChunk = -1;
            this.decorator.reedsPerChunk = -1;
            this.decorator.cactiPerChunk = -1;
        }
    }

    // TODO: Вывести значения в конфиги
    public static void chunkHasBiomeUpdate(Chunk chunk) {
        World world = chunk.getWorld();
        if (world.rand.nextInt(((80 * taintChunks) + 2000) / 40) == 0) {
            ArrayList<Short> taintBiomeArea = new ArrayList<>(16 * 16);
            byte[] biomes = chunk.getBiomeArray();

            for (int i = 0; i < 16; ++i) {
                for (int j = 0; j < 16; ++j) {
                    byte k = biomes[j + i * 16];
                    if (MiscHandler.arrayContains(InitBiome.TAINT_BIOMES_L_ID, k)) {
                        taintBiomeArea.add((short) ((i << 8) | (j & 0xFF)));
                    }
                }
            }

            if (taintBiomeArea.isEmpty()) {
                return;
            }

            for (int i = 0; i < world.rand.nextInt(3) + 1; i++) {
                short localPos = taintBiomeArea.get(world.rand.nextInt(taintBiomeArea.size()));
                byte localX = (byte) ((localPos >> 8) & 0xFF);
                byte localZ = (byte) (localPos & 0xFF);

                int x = (chunk.x << 4) + localX;
                int z = (chunk.z << 4) + localZ;
                int y = MiscHandler.calculateGenerationHeight(world, x, z);

                EntityLightningBolt lightningBolt = new EntityLightningBolt(world, x, y, z, true);
                world.addWeatherEffect(lightningBolt);
            }
        }
    }

    public static boolean canSpawn(Entity entity, BiomeTaint biome) {
        return biome.type != TaintType.HILLS || !(entity.posY > 100);
    }

    public static void decorateCustom(World worldIn, Random random, ChunkPos pos, byte[] biomeArray) {
        UltraMutableBlockPos blockPos = UltraMutableBlockPos.getBlockPosFromPoll().setPos(pos);
        Chunk chunk = worldIn.getChunkFromBlockCoords(blockPos);

        for (int i = 0; i != 16; i++) {
            for (int j = 0; j != 16; j++) {
                byte k = biomeArray[i + j * 16];
                if (MiscHandler.arrayContains(InitBiome.TAINT_BIOMES_L_ID, k)) {
                    blockPos.pushPos();
                    blockPos.add(i, 0, j);
                    decorateNormal(worldIn, random, blockPos, k);
                    blockPos.popPos();
                } else if (MiscHandler.arrayContains(InitBiome.TAINT_BIOMES_EDGE_ID, k)) {
                    blockPos.pushPos();
                    blockPos.add(i, 0, j);
                    decorateEdge(worldIn, random, blockPos, k);
                    blockPos.popPos();
                }
            }
        }

        if (MiscHandler.fastCheckChunkContainsAnyOnBiomeArray(chunk, InitBiome.TAINT_BIOMES_L_ID)) {
            decorateChunkNormal(worldIn, random,  blockPos);
        }

        UltraMutableBlockPos.returnBlockPosToPoll(blockPos);
    }

    private static void decorateEdge(World worldIn, Random rand, UltraMutableBlockPos blockPos, byte biomeId) {
        blockPos.setWorldY(worldIn);
        WORLD_GENS_MISC.ADD_SNOW.generate(worldIn, rand, blockPos);
    }

    private static void decorateNormal(World worldIn, Random rand, UltraMutableBlockPos blockPos, byte biomeId) {
        blockPos.setWorldY(worldIn);
        WORLD_GENS_MISC.REMOVE_LIQUIDS.generate(worldIn, rand, blockPos);
        WORLD_GENS_MISC.REPLACE_VISIBLE_BLOCKS.generate(worldIn, rand, blockPos);
        WORLD_GENS_MISC.ADD_TAINT_FEATURE.generate(worldIn, rand, blockPos);
        WORLD_GENS_MISC.ADD_SNOW.generate(worldIn, rand, blockPos);
    }

    private static void decorateChunkNormal(World worldIn, Random rand, UltraMutableBlockPos blockPos) {
        WORLD_GENS_MISC.CLEAN_VOID_STONE.generate(worldIn, rand, blockPos);
        WORLD_GENS_MISC.INFERNAL_CIRCLE.generate(worldIn, rand, blockPos);
        WORLD_GENS_MISC.LAVA_STAIRS.generate(worldIn, rand,  blockPos);
    }

    public void registerBiomeP2() {
        this.fillerBlock = InitBlocks.TAINT_VOID_STONE.getDefaultState();
        this.topBlock = BlocksTC.taintSoil.getDefaultState();
    }

    public void registerBiomeP2(IBlockState topBlock, IBlockState fillerBlock) {
        this.fillerBlock = fillerBlock;
        this.topBlock = topBlock;
    }

    @Override
    public boolean getEnableSnow() {
        return this.type == TaintType.EDGE;
    }

    @Override
    public int getGrassColorAtPos(BlockPos pos) {
        return 0x16001e;
    }

    @Override
    public int getFoliageColorAtPos(BlockPos pos) {
        return 0x16001e;
    }

    @Override
    public int getSkyColorByTemp(float currentTemperature) {
        return this.type != TaintType.WASTELAND ? 0x16001e : 0;
    }

    @Override
    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        if (type == TaintType.NORMAL) {
            return rand.nextFloat() < 0.25F ? BIG_TREE_TAINT_FEATURE : TREE_TAINT_FEATURE;
        } else if (type == TaintType.HILLS) {
            return TREE_TAINT_FEATURE;
        } else if (type == TaintType.SEA) {
            return ROTTEN_SPIRES;
        } else if (type == TaintType.WASTELAND) {
            return INFERNAL_SPIRES;
        } else {
            return TREE_TAINT_FEATURE;
        }
    }

    public enum TaintType {
        EDGE, NORMAL, SEA, HILLS, WASTELAND, BEACH
    }
}
