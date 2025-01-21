package com.artur.returnoftheancients.generation.biomes.decorate;

import com.artur.returnoftheancients.ancientworldgeneration.structurebuilder.CustomGenStructure;
import com.artur.returnoftheancients.generation.generators.GenStructure;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

import java.util.Random;
// TODO: Сделать по нормальному

public class WorldGenTaintBigTree extends WorldGenAbstractTree {
    public WorldGenTaintBigTree(boolean notify) {
        super(notify);
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        if (false) {
            GenStructure.generateStructure(worldIn, position.getX(), position.getY(), position.getZ(), "taint_tree_big");
        }
        return false;
    }
}
