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
            if (pos.getY() >= 74) {
                worldIn.setBlockState(pos, Blocks.SNOW_LAYER.getDefaultState());
            }
        } else if (this.type == TaintType.NORMAL) {
            if (!worldIn.isRemote) {
                if (!TaintHelper.isNearTaintSeed(worldIn, pos)) {
                    ItemMonsterPlacer.spawnCreature(worldIn, EntityList.getKey(EntityTaintSeedPrime.class), pos.getX(), pos.getY() + 1, pos.getZ());
                }
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
    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        return BIG_TREE_FEATURE;
    }

    public enum TaintType {
        EDGE, NORMAL
    }
}
