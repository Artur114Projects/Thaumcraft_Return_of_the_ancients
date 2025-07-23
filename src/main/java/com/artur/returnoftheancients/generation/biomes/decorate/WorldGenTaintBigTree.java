package com.artur.returnoftheancients.generation.biomes.decorate;

import com.artur.returnoftheancients.util.interfaces.Function2;
import com.artur.returnoftheancients.util.interfaces.Function3;
import com.artur.returnoftheancients.util.interfaces.Function4;
import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.blocks.world.taint.BlockTaintLog;

import java.util.Random;

public class WorldGenTaintBigTree extends WorldGenAbstractTree {

    public WorldGenTaintBigTree(boolean notify) {
        super(notify);
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        UltraMutableBlockPos blockPos = UltraMutableBlockPos.getBlockPosFromPoll();
        blockPos.setPos(position).down();
        long seed = rand.nextLong();

        if (!worldIn.getBlockState(blockPos).getBlock().equals(BlocksTC.taintSoil)) {
            UltraMutableBlockPos.returnBlockPosToPoll(blockPos); return false;
        }

        if (blockPos.getY() > 80) {
            UltraMutableBlockPos.returnBlockPosToPoll(blockPos); return false;
        }

        int firstTrunkHeight = 3 + rand.nextInt(2 + 1);



        return true;
    }

    private void applyFirstTrunkPositions(UltraMutableBlockPos blockPos, World world, int trunkHeight, Function4<World, BlockPos, IBlockState, Boolean, Boolean> apply) {
        if (!apply.apply(world, blockPos, BlocksTC.taintLog.getDefaultState(), true)) {return;}
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            blockPos.pushPos();
            blockPos.offset(facing);
            if (!apply.apply(world, blockPos, BlocksTC.taintLog.getDefaultState().withProperty(BlockTaintLog.AXIS, facing.getAxis()), true)) {return;}
            blockPos.popPos();
        }

        for (int i = 0; i != trunkHeight + 1; i++) {
            blockPos.pushPos();
            blockPos.up(i);
            if (!apply.apply(world, blockPos, BlocksTC.taintLog.getDefaultState(), false)) {return;}
            blockPos.popPos();
        }
        blockPos.up(trunkHeight + 1);
    }
}
