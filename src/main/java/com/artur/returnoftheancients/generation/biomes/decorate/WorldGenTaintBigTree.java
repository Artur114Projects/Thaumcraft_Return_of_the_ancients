package com.artur.returnoftheancients.generation.biomes.decorate;

import com.artur.returnoftheancients.util.interfaces.Function4;
import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import thaumcraft.api.ThaumcraftMaterials;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.blocks.IBlockFacing;
import thaumcraft.common.blocks.world.taint.BlockTaintLog;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class WorldGenTaintBigTree extends WorldGenAbstractTree {

    public WorldGenTaintBigTree(boolean notify) {
        super(notify);
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        UltraMutableBlockPos blockPos = UltraMutableBlockPos.obtain();
        try {
            long seed = rand.nextLong();

            if (worldIn.getBlockState(blockPos.setPos(position).down()).getBlock() != BlocksTC.taintSoil) {
                return false;
            }

            if (blockPos.getY() > 80) {
                return false;
            }

            if (this.checkPosition(worldIn, seed, blockPos.setPos(position).down())) {
                this.generate(worldIn, seed, blockPos.setPos(position).down());
                return true;
            }

            return false;
        } finally {
            UltraMutableBlockPos.release(blockPos);
        }
    }

    private boolean checkPosition(World worldIn, long seed, UltraMutableBlockPos pos) {
        Random rand = new Random(seed); rand.nextInt();
        int firstTrunkHeight = 3 + rand.nextInt(2 + 1);
        int maxBranchHeight = 3 + rand.nextInt(2 + 1);
        int minBranchHeight = 3;

        int lastTrunkHeight = 2 + maxBranchHeight + 3;

        AtomicBoolean flag = new AtomicBoolean(true);

        Function4<World, BlockPos, IBlockState, Boolean, Boolean> def = (world, pos12, state, isReplace) -> {
            if (state.getBlock() != BlocksTC.taintFeature) {
                if ((!isReplace && !world.isAirBlock(pos12)) || (isReplace && world.getBlockState(pos12).getMaterial() != ThaumcraftMaterials.MATERIAL_TAINT)) {
                    flag.set(false); return false;
                }
            }
            return true;
        };

        this.applyFirstTrunkPositions(pos, worldIn, firstTrunkHeight, def);
        if (!flag.get()) return false;
        this.applyMainBranchesPositions(pos, worldIn, rand, minBranchHeight, maxBranchHeight, def);
        if (!flag.get()) return false;
        this.applyLastTrunkPositions(pos, worldIn, lastTrunkHeight, def);
        if (!flag.get()) return false;
        this.applyLastBranchesPositions(pos, worldIn, def);
        return flag.get();
    }

    private void generate(World worldIn, long seed, UltraMutableBlockPos pos) {
        Random rand = new Random(seed); rand.nextInt();
        int firstTrunkHeight = 3 + rand.nextInt(2 + 1);
        int maxBranchHeight = 3 + rand.nextInt(2 + 1);
        int minBranchHeight = 3;

        int lastTrunkHeight = 2 + maxBranchHeight + 3;

        Function4<World, BlockPos, IBlockState, Boolean, Boolean> def = (world, pos12, state, isReplace) -> {
            if (state.getBlock() == BlocksTC.taintFeature) {
                if (rand.nextFloat() < 0.2F && world.isAirBlock(pos12)) {
                    world.setBlockState(pos12, state);
                }
            } else {
                world.setBlockState(pos12, state);
            }
            return true;
        };

        this.applyFirstTrunkPositions(pos, worldIn, firstTrunkHeight, def);
        this.applyMainBranchesPositions(pos, worldIn, rand, minBranchHeight, maxBranchHeight, def);
        this.applyLastTrunkPositions(pos, worldIn, lastTrunkHeight, def);
        this.applyLastBranchesPositions(pos, worldIn, def);
    }

    private void applyLastBranchesPositions(UltraMutableBlockPos blockPos, World world, Function4<World, BlockPos, IBlockState, Boolean, Boolean> apply) {
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            blockPos.pushPos();

            if (!apply.apply(world, blockPos.offset(facing), BlocksTC.taintLog.getDefaultState().withProperty(BlockTaintLog.AXIS, facing.getAxis()), false)) {
                return;
            }
            for (EnumFacing offset : EnumFacing.VALUES) {
                if (offset == facing.getOpposite() || offset == EnumFacing.UP) {
                    continue;
                }
                blockPos.pushPos();
                blockPos.offset(offset);
                if (!apply.apply(world, blockPos, BlocksTC.taintFeature.getDefaultState().withProperty(IBlockFacing.FACING, offset), false)) {
                    return;
                }
                blockPos.popPos();
            }

            if (!apply.apply(world, blockPos.up(), BlocksTC.taintLog.getDefaultState(), false)) {
                return;
            }
            for (EnumFacing offset : EnumFacing.VALUES) {
                if (offset.getAxis() == facing.getAxis() || offset == EnumFacing.DOWN) {
                    continue;
                }
                blockPos.pushPos();
                blockPos.offset(offset);
                if (!apply.apply(world, blockPos, BlocksTC.taintFeature.getDefaultState().withProperty(IBlockFacing.FACING, offset), false)) {
                    return;
                }
                blockPos.popPos();
            }

            if (!apply.apply(world, blockPos.offset(facing), BlocksTC.taintLog.getDefaultState().withProperty(BlockTaintLog.AXIS, facing.getAxis()), false)) {
                return;
            }
            for (EnumFacing offset : EnumFacing.VALUES) {
                if (offset.getAxis() == facing.getAxis()) {
                    continue;
                }
                blockPos.pushPos();
                blockPos.offset(offset);
                if (!apply.apply(world, blockPos, BlocksTC.taintFeature.getDefaultState().withProperty(IBlockFacing.FACING, offset), false)) {
                    return;
                }
                blockPos.popPos();
            }

            blockPos.popPos();
        }
    }

    private void applyMainBranchesPositions(UltraMutableBlockPos blockPos, World world, Random rand, int minBranchH, int maxBranchH, Function4<World, BlockPos, IBlockState, Boolean, Boolean> apply) {
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            int height = maxBranchH == minBranchH ? maxBranchH : rand.nextInt(maxBranchH - minBranchH) + minBranchH;
            blockPos.pushPos();

            if (!apply.apply(world, blockPos.offset(facing), BlocksTC.taintLog.getDefaultState().withProperty(BlockTaintLog.AXIS, facing.getAxis()), false)) {
                return;
            }
            for (EnumFacing offset : EnumFacing.VALUES) {
                if (offset == facing.getOpposite() || offset == EnumFacing.UP) {
                    continue;
                }
                blockPos.pushPos();
                blockPos.offset(offset);
                if (!apply.apply(world, blockPos, BlocksTC.taintFeature.getDefaultState().withProperty(IBlockFacing.FACING, offset), false)) return;
                blockPos.popPos();
            }

            if (!apply.apply(world, blockPos.up(), BlocksTC.taintLog.getDefaultState(), false)) {
                return;
            }
            for (EnumFacing offset : EnumFacing.VALUES) {
                if (offset.getAxis() == facing.getAxis() || offset == EnumFacing.DOWN) {
                    continue;
                }
                blockPos.pushPos();
                blockPos.offset(offset);
                if (!apply.apply(world, blockPos, BlocksTC.taintFeature.getDefaultState().withProperty(IBlockFacing.FACING, offset), false)) return;
                blockPos.popPos();
            }

            if (!apply.apply(world, blockPos.offset(facing), BlocksTC.taintLog.getDefaultState().withProperty(BlockTaintLog.AXIS, facing.getAxis()), false)) {
                return;
            }
            for (EnumFacing offset : EnumFacing.VALUES) {
                if (offset == facing.getOpposite() || offset == EnumFacing.UP) {
                    continue;
                }
                blockPos.pushPos();
                blockPos.offset(offset);
                if (!apply.apply(world, blockPos, BlocksTC.taintFeature.getDefaultState().withProperty(IBlockFacing.FACING, offset), false)) return;
                blockPos.popPos();
            }

            blockPos.up();
            for (int i = 0; i != height + 1; i++) {
                blockPos.pushPos();
                blockPos.up(i);
                boolean flag = (height == 3 && i == 2) || (height > 3 && i == 3);

                if (flag) {
                    blockPos.pushPos();
                    if (!apply.apply(world, blockPos.offset(facing), BlocksTC.taintLog.getDefaultState().withProperty(BlockTaintLog.AXIS, facing.getAxis()), false)) {
                        return;
                    }
                    for (EnumFacing offset : EnumFacing.VALUES) {
                        if (offset == facing.getOpposite()) {
                            continue;
                        }
                        blockPos.pushPos();
                        blockPos.offset(offset);
                        if (!apply.apply(world, blockPos, BlocksTC.taintFeature.getDefaultState().withProperty(IBlockFacing.FACING, offset), false)) return;
                        blockPos.popPos();
                    }
                    blockPos.popPos();
                }

                if (!apply.apply(world, blockPos, BlocksTC.taintLog.getDefaultState(), false)){
                    return;
                }
                for (EnumFacing offset : EnumFacing.HORIZONTALS) {
                    blockPos.pushPos();
                    blockPos.offset(offset);
                    if (!apply.apply(world, blockPos, BlocksTC.taintFeature.getDefaultState().withProperty(IBlockFacing.FACING, offset), false)) return;
                    blockPos.popPos();
                }

                blockPos.popPos();
            }

            blockPos.popPos();
        }
    }

    private void applyLastTrunkPositions(UltraMutableBlockPos blockPos, World world, int trunkHeight, Function4<World, BlockPos, IBlockState, Boolean, Boolean> apply) {
        for (int i = 0; i != trunkHeight + 1; i++) {
            blockPos.pushPos();
            blockPos.up(i);

            boolean flag = i == 0 || i == 1 || i >= trunkHeight - 2;

            IBlockState state = i == trunkHeight ? BlocksTC.taintGeyser.getDefaultState() : BlocksTC.taintLog.getDefaultState();
            if (!apply.apply(world, blockPos, state, false)) {
                return;
            }
            if (!flag) {
                for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                    blockPos.pushPos();
                    blockPos.offset(facing);
                    if (!apply.apply(world, blockPos, BlocksTC.taintFeature.getDefaultState().withProperty(IBlockFacing.FACING, facing), false))
                        return;
                    blockPos.popPos();
                }
            }
            blockPos.popPos();
        }
        blockPos.up(trunkHeight - 2);
    }

    private void applyFirstTrunkPositions(UltraMutableBlockPos blockPos, World world, int trunkHeight, Function4<World, BlockPos, IBlockState, Boolean, Boolean> apply) {
        if (!apply.apply(world, blockPos, BlocksTC.taintLog.getDefaultState(), true)) return;
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            blockPos.pushPos();
            blockPos.offset(facing);
            if (!apply.apply(world, blockPos, BlocksTC.taintLog.getDefaultState().withProperty(BlockTaintLog.AXIS, facing.getAxis()), true)) return;
            blockPos.popPos();
        }

        for (int i = 0; i != trunkHeight + 1; i++) {
            blockPos.pushPos();
            blockPos.up(i);
            if (!apply.apply(world, blockPos, BlocksTC.taintLog.getDefaultState(), i == 0)) return;
            for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                blockPos.pushPos();
                blockPos.offset(facing);
                if (!apply.apply(world, blockPos, BlocksTC.taintFeature.getDefaultState().withProperty(IBlockFacing.FACING, facing), false)) return;
                blockPos.popPos();
            }
            blockPos.popPos();
        }
        blockPos.up(trunkHeight + 1);
    }
}
