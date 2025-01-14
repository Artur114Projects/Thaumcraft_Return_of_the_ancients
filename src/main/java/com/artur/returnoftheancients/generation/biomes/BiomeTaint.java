package com.artur.returnoftheancients.generation.biomes;

import com.artur.returnoftheancients.generation.generators.WorldGenTaintBigTree;
import com.artur.returnoftheancients.handlers.HandlerR;
import com.artur.returnoftheancients.init.InitBiome;
import com.artur.returnoftheancients.init.InitBlocks;
import com.artur.returnoftheancients.utils.math.UltraMutableBlockPos;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.common.BiomeDictionary;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.entities.monster.tainted.EntityTaintSeedPrime;

import java.util.ArrayList;
import java.util.Random;

public class BiomeTaint extends BiomeBase {

    WorldGenAbstractTree BIG_TREE_TAINT_FEATURE = new WorldGenTaintBigTree(false);

    public final TaintType type;
    public static int taintChunks = 0;

    // TODO: Добавить больше существ в spawnableCreatureList.
    public BiomeTaint(String registryName, BiomeProperties properties, EBiome eBiome, TaintType type) {
        super(registryName, properties, eBiome);
        this.decorator.generateFalls = false;
        this.spawnableCreatureList.clear();
        this.type = type;

        if (this.type == TaintType.EDGE) {
            this.decorator.extraTreeChance = -1;
            this.topBlock = Blocks.STONE.getDefaultState();
            this.fillerBlock = Blocks.STONE.getDefaultState();
            this.decorator.treesPerChunk = -1;
            this.decorator.deadBushPerChunk = -1;
            this.decorator.reedsPerChunk = -1;
            this.decorator.cactiPerChunk = -1;
        } else if (this.type == TaintType.NORMAL) {
            this.decorator.treesPerChunk = -1;
            this.spawnableCreatureList.add(new SpawnListEntry(EntityTaintSeedPrime.class, 4, 1, 1));
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
                    if (HandlerR.arrayContainsAny(InitBiome.TAINT_BIOMES_L_ID, k)) {
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
                int y = HandlerR.calculateGenerationHeight(world, x, z);

                EntityLightningBolt lightningBolt = new EntityLightningBolt(world, x, y, z, true);
                world.addWeatherEffect(lightningBolt);
            }
        }
    }

    public static void decorateCustom(World worldIn, Random random, ChunkPos pos, byte[] biomeArray) {
        UltraMutableBlockPos blockPos = new UltraMutableBlockPos(pos);

        for (int i = 0; i != 16; i++) {
            for (int j = 0; j != 16; j++) {
                byte k = biomeArray[i + j * 16];
                if (HandlerR.arrayContainsAny(InitBiome.TAINT_BIOMES_L_ID, k)) {
                    blockPos.pushPos();
                    blockPos.add(i, 0, j);
                    decorateNormal(worldIn, random, blockPos, k);
                    blockPos.popPos();
                } else if (HandlerR.arrayContainsAny(InitBiome.TAINT_BIOMES_EDGE_ID, k)) {
                    blockPos.pushPos();
                    blockPos.add(i, 0, j);
                    decorateEdge(worldIn, random, blockPos, k);
                    blockPos.popPos();
                }
            }
        }
    }

    private static void decorateEdge(World worldIn, Random rand, UltraMutableBlockPos blockPos, byte biomeId) {
        blockPos.setY(HandlerR.calculateGenerationHeight(worldIn, blockPos.getX(), blockPos.getZ()));
        if (blockPos.getY() >= 100) {
            if (blockPos.getY() > 90) {
                worldIn.setBlockState(blockPos.up(), Blocks.SNOW_LAYER.getDefaultState());
            } else if (rand.nextBoolean()) {
                worldIn.setBlockState(blockPos.up(), Blocks.SNOW_LAYER.getDefaultState());
            }
        }
    }

    private static void decorateNormal(World worldIn, Random rand, UltraMutableBlockPos blockPos, byte biomeId) {
        if (rand.nextInt(33) == 0) {
            blockPos.setY(HandlerR.calculateGenerationHeight(worldIn, blockPos.getX(), blockPos.getZ()));
            if (worldIn.getBlockState(blockPos).getBlock() == BlocksTC.taintSoil) {
                IBlockState state = BlocksTC.taintFeature.getBlockState().getBaseState();
                blockPos.up();
                worldIn.setBlockState(blockPos, state.withProperty(BlockDirectional.FACING, EnumFacing.UP), 3);
                worldIn.checkLight(blockPos);
            }
        }
    }

    public void registerBiomeP2() {
        this.topBlock = BlocksTC.taintSoil.getDefaultState();
        this.fillerBlock = InitBlocks.TAINT_VOID_STONE.getDefaultState();
    }

    @Override
    public int getGrassColorAtPos(BlockPos pos) {
        return this.type == TaintType.EDGE ? super.getGrassColorAtPos(pos) : 0x16001e;
    }

    @Override
    public boolean getEnableSnow() {
        return this.type == TaintType.EDGE;
    }

    @Override
    public int getFoliageColorAtPos(BlockPos pos) {
        return this.type == TaintType.EDGE ? super.getFoliageColorAtPos(pos) : 0x16001e;
    }

    @Override
    public int getSkyColorByTemp(float currentTemperature) {
        return this.type == TaintType.EDGE ? super.getSkyColorByTemp(currentTemperature) : 0x16001e;
    }

    @Override
    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        return /* this.type == TaintType.NORMAL ? */ BIG_TREE_TAINT_FEATURE;
    }

    public enum TaintType {
        EDGE, NORMAL
    }
}
