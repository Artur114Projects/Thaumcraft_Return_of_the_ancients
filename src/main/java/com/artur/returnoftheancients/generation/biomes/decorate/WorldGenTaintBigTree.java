package com.artur.returnoftheancients.generation.biomes.decorate;

import com.artur.returnoftheancients.ancientworldgeneration.structurebuilder.CustomGenStructure;
import com.artur.returnoftheancients.generation.generators.GenStructure;
import com.artur.returnoftheancients.utils.math.UltraMutableBlockPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.blocks.IBlockFacing;
import thaumcraft.common.blocks.world.taint.BlockTaintLog;

import java.util.Random;
// TODO: Сделать по нормальному

public class WorldGenTaintBigTree extends WorldGenAbstractTree {
    private final UltraMutableBlockPos blockPos = new UltraMutableBlockPos();

    public WorldGenTaintBigTree(boolean notify) {
        super(notify);
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        blockPos.setPos(position).down();
        long seed = rand.nextLong();

        Random random = new Random(seed);
        random.nextInt();

        if (!worldIn.getBlockState(blockPos).getBlock().equals(BlocksTC.taintSoil)) {
            return false;
        }

        if (blockPos.getY() > 80) {
            return false;
        }

        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            blockPos.pushPos();
            blockPos.offset(facing);
            int height = genTrunk(worldIn, random, blockPos, facing.getOpposite(), 5, 1);
            blockPos.offset(facing);
            blockPos.down();
            genBranch(worldIn, random, blockPos, facing, height);
            blockPos.popPos();
        }
        genTrunk(worldIn, random, blockPos,null, 9, 2);

        return true;
    }

    private int genTrunk(World worldIn, Random rand, UltraMutableBlockPos position, EnumFacing notGenFeature, int minTrunkHeight, int trunkHeightOffset) {
        Random random = new Random(rand.nextLong());
        random.nextInt();
        int trunkHeight = minTrunkHeight + (rand.nextInt(trunkHeightOffset + 1));

        for (int i = 0; i != trunkHeight; i++) {
            worldIn.setBlockState(position, BlocksTC.taintLog.getDefaultState());
            if (i != trunkHeight - 1) {
                for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                    if (facing == notGenFeature) {
                        continue;
                    }
                    position.pushPos();
                    position.offset(facing);
                    if (worldIn.isAirBlock(position) && random.nextInt(6) == 0) {
                        worldIn.setBlockState(position, BlocksTC.taintFeature.getDefaultState().withProperty(IBlockFacing.FACING, facing));
                    }
                    position.popPos();
                }
            }
            position.up();
        }
        return trunkHeight;
    }

    private void genBranch(World worldIn, Random rand, UltraMutableBlockPos position, EnumFacing offset, int trunkHeight) {
        Random random = new Random(rand.nextLong());
        random.nextInt();
        int branchHeight = MathHelper.floor(trunkHeight / 2.0F) + (rand.nextInt(2 + 1));

        for (int i = 0; i != branchHeight; i++) {
            IBlockState state = BlocksTC.taintLog.getDefaultState();
            if (i == 0) {
                state = BlocksTC.taintLog.getDefaultState().withProperty(BlockTaintLog.AXIS, offset.getAxis());
            }
            worldIn.setBlockState(position, state);
            if (i != branchHeight - 1) {
                for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                    position.pushPos();
                    position.offset(facing);
                    if (worldIn.isAirBlock(position) && random.nextInt(6) == 0) {
                        worldIn.setBlockState(position, BlocksTC.taintFeature.getDefaultState().withProperty(IBlockFacing.FACING, facing));
                    }
                    position.popPos();
                }
            } else {
                position.pushPos();
                position.offset(offset);
                worldIn.setBlockState(position, BlocksTC.taintLog.getDefaultState().withProperty(BlockTaintLog.AXIS, offset.getAxis()));
                for (EnumFacing facing : EnumFacing.values()) {
                    if ((facing == EnumFacing.DOWN || facing == EnumFacing.UP) || facing.getAxis() != offset.getAxis()) {
                        position.pushPos();
                        position.offset(facing);
                        if (worldIn.isAirBlock(position) && random.nextInt(4) == 0) {
                            worldIn.setBlockState(position, BlocksTC.taintFeature.getDefaultState().withProperty(IBlockFacing.FACING, facing));
                        }
                        position.popPos();
                    }
                }
                position.pushPos();
                position.offset(offset);
                if (worldIn.isAirBlock(position) && random.nextInt(2) == 0) {
                    worldIn.setBlockState(position, BlocksTC.taintFeature.getDefaultState().withProperty(IBlockFacing.FACING, offset));
                }
                position.popPos();
                position.popPos();
            }
            position.up();
        }
    }
}
