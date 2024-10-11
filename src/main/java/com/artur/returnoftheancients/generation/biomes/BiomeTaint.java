package com.artur.returnoftheancients.generation.biomes;

import com.artur.returnoftheancients.handlers.HandlerR;
import com.artur.returnoftheancients.init.InitBiome;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.blocks.world.taint.TaintHelper;
import thaumcraft.common.entities.monster.boss.EntityCultistPortalGreater;
import thaumcraft.common.entities.monster.tainted.EntityTaintSeedPrime;
import thaumcraft.common.world.aura.AuraChunk;
import thaumcraft.common.world.aura.AuraHandler;

import java.util.Random;

public class BiomeTaint extends BiomeBase {

    TaintType type;


    // TODO: Добавить больше существ в spawnableCreatureList
    public BiomeTaint(String registryName, BiomeProperties properties, EBiome eBiome, TaintType type) {
        super(registryName, properties, eBiome);
        this.spawnableCreatureList.clear();
        this.type = type;

        if (type == TaintType.EDGE) {
            this.decorator.extraTreeChance = 0F;
            this.topBlock = Blocks.STONE.getDefaultState();
            this.fillerBlock = Blocks.STONE.getDefaultState();
            this.decorator.treesPerChunk = -999;
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
        if (this.type == TaintType.EDGE) {
            for (int x = 0; x != 16; x++) {
                for (int z = 0; z != 16; z++) {
                    BlockPos currentPos = pos.add(x, 0, z);

                    int y = HandlerR.calculateGenerationHeight(worldIn, currentPos.getX(), currentPos.getZ());

                    if (y >= 100) {
                        worldIn.setBlockState(currentPos.add(0, y, 0), Blocks.SNOW_LAYER.getDefaultState());
                    }
                }
            }
        } else if (this.type == TaintType.NORMAL) {
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
        return 0x563367;
    }

    @Override
    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        return BIG_TREE_FEATURE;
    }

    public enum TaintType {
        EDGE, NORMAL
    }
}
