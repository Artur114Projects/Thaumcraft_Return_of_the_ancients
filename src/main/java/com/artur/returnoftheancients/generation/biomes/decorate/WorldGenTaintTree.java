package com.artur.returnoftheancients.generation.biomes.decorate;

import com.artur.returnoftheancients.utils.math.UltraMutableBlockPos;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import thaumcraft.api.ThaumcraftMaterials;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.blocks.IBlockFacing;
import thaumcraft.common.blocks.world.taint.BlockTaintFeature;
import thaumcraft.common.blocks.world.taint.BlockTaintLog;

import java.util.Random;

public class WorldGenTaintTree extends WorldGenAbstractTree {

    private final UltraMutableBlockPos blockPos = new UltraMutableBlockPos();

    public WorldGenTaintTree(boolean notify) {
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

        blockPos.pushPos();
        if (!checkPos(worldIn, seed, blockPos)) {
            return false;
        }
        blockPos.popPos();

        int trunkHeight = genTrunk(worldIn, random, blockPos);

        blockPos.down();
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            blockPos.pushPos();
            blockPos.offset(facing);
            genBranch(worldIn, random, blockPos, facing, trunkHeight);
            blockPos.popPos();
        }

        blockPos.setPos(0, 0, 0);
        return true;
    }

    @Override
    protected boolean canGrowInto(Block blockType) {
        return blockType.equals(BlocksTC.taintSoil);
    }

    private boolean checkPos(World worldIn, long seed, UltraMutableBlockPos position) {
        Random random = new Random(seed);
        random.nextInt();

        random.nextLong();
        int minTrunkHeight = 4;
        int trunkHeight = minTrunkHeight + (random.nextInt(2) + 1);

        for (int i = 0; i != trunkHeight; i++) {
            if (i != 0 && !worldIn.isAirBlock(position)) {
                return false;
            }
            position.up();
        }

        position.down();
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            position.pushPos();
            position.offset(facing);

            random.nextLong();
            int branchHeight = MathHelper.floor(trunkHeight / 2.0F) + (random.nextInt(3) + 1);
            for (int i = 0; i != branchHeight; i++) {
                if (!worldIn.isAirBlock(position)) {
                    return false;
                }
                if (i == branchHeight - 1) {
                    position.pushPos();
                    position.offset(facing);
                    if (!worldIn.isAirBlock(position)) {
                        return false;
                    }
                    position.popPos();
                }
                position.up();
            }
            position.popPos();
        }

        return true;
    }



    private int genTrunk(World worldIn, Random rand, UltraMutableBlockPos position) {
        Random random = new Random(rand.nextLong());
        random.nextInt();
        int minTrunkHeight = 4;
        int trunkHeight = minTrunkHeight + (rand.nextInt(2) + 1);

        for (int i = 0; i != trunkHeight; i++) {
            worldIn.setBlockState(position, BlocksTC.taintLog.getDefaultState());
            if (i != trunkHeight - 1) {
                for (EnumFacing facing : EnumFacing.HORIZONTALS) {
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
        int branchHeight = MathHelper.floor(trunkHeight / 2.0F) + (rand.nextInt(3) + 1);

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
