package com.artur.returnoftheancients.generation.biomes;

import com.artur.returnoftheancients.handlers.HandlerR;
import com.artur.returnoftheancients.init.InitBiome;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
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

    public final TaintType type;
    public static int taintChunks = 0;

    // TODO: Добавить обработку чанков с этим биомом и спавн молний.
    // TODO: Добавить больше существ в spawnableCreatureList.
    public BiomeTaint(String registryName, BiomeProperties properties, EBiome eBiome, TaintType type) {
        super(registryName, properties, eBiome);
        this.spawnableCreatureList.clear();
        this.type = type;

        if (type == TaintType.EDGE) {
            this.decorator.extraTreeChance = 0F;
            this.topBlock = Blocks.STONE.getDefaultState();
            this.fillerBlock = Blocks.STONE.getDefaultState();
            this.decorator.treesPerChunk = 0;
            this.decorator.deadBushPerChunk = 0;
            this.decorator.reedsPerChunk = 0;
            this.decorator.cactiPerChunk = 0;
        } else if (this.type == TaintType.NORMAL) {
            this.decorator.treesPerChunk = 1;
            this.spawnableCreatureList.add(new SpawnListEntry(EntityTaintSeedPrime.class, 4, 1, 1));
        }
    }


    @Override
    public void decorate(World worldIn, Random rand, BlockPos pos) {
        super.decorate(worldIn, rand, pos);
        BlockPos.MutableBlockPos currentPos = new BlockPos.MutableBlockPos(pos);
        for (int x = 0; x != 16; x++) {
            for (int z = 0; z != 16; z++) {
                int localX = x + pos.getX();
                int localZ = z + pos.getZ();
                currentPos.setPos(localX, HandlerR.calculateGenerationHeight(worldIn, localX, localZ), localZ);
                if (this.type == TaintType.EDGE) {
                    decorateEdge(worldIn, rand, currentPos);
                } else if (this.type == TaintType.NORMAL) {
                    decorateNormal(worldIn, rand, currentPos);
                }
            }
        }
    }



    // TODO: Вывести значения в конфиги
    public static void chunkHasBiomeUpdate(Chunk chunk) {
        World world = chunk.getWorld();
        if (world.rand.nextInt(((40 * taintChunks) + 2001) / 20) == 0) {
            int chunkArea = 16;
            ArrayList<Short> taintBiomeArea = new ArrayList<>();
            byte[] biomes = chunk.getBiomeArray();

            for (int i = 0; i < chunkArea; ++i) {
                for (int j = 0; j < chunkArea; ++j) {
                    int k = biomes[j + i * chunkArea];
                    if (BiomeDictionary.hasType(Biome.getBiomeForId(k), InitBiome.TAINT_TYPE_L)) {
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


    public void decorateNormal(World worldIn, Random rand, BlockPos pos) {
        if (rand.nextInt(33) == 0) {
            if (worldIn.getBlockState(pos).getBlock() == BlocksTC.taintSoil) {
                IBlockState state = BlocksTC.taintFeature.getBlockState().getBaseState();
                BlockPos posUp = pos.up();
                worldIn.setBlockState(posUp, state.withProperty(BlockDirectional.FACING, EnumFacing.UP), 3);
            }
        }
    }

    public void decorateEdge(World worldIn, Random rand, BlockPos pos) {
        if (pos.getY() >= 100) {
            if (worldIn.getBlockState(pos).getBlock().equals(Blocks.STONE)) {
                worldIn.setBlockState(pos.up(), Blocks.SNOW_LAYER.getDefaultState());
            }
        }
    }


    @Override
    public void registerBiome() {
        super.registerBiome();
    }

    public void registerBiomeP2() {
        this.topBlock = BlocksTC.taintSoil.getDefaultState();
        this.fillerBlock = BlocksTC.taintCrust.getDefaultState();
    }

    @Override
    public int getGrassColorAtPos(BlockPos pos) {
        return this.type == TaintType.EDGE ? super.getGrassColorAtPos(pos) : 0x563367;
    }

    @Override
    public boolean getEnableSnow() {
        return this.type == TaintType.EDGE;
    }

    @Override
    public int getFoliageColorAtPos(BlockPos pos) {
        return this.type == TaintType.EDGE ? super.getFoliageColorAtPos(pos) : 0x563367;
    }

    @Override
    public int getSkyColorByTemp(float currentTemperature) {
        return this.type == TaintType.EDGE ? super.getSkyColorByTemp(currentTemperature) : 0x1C1122;
    }

    @Override
    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        return BIG_TREE_FEATURE;
    }

    public enum TaintType {
        EDGE, NORMAL
    }
}
