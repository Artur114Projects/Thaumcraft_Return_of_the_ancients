package com.artur.returnoftheancients.generation.biomes.decorate;

import com.artur.returnoftheancients.handlers.HandlerR;
import com.artur.returnoftheancients.init.InitBiome;
import com.artur.returnoftheancients.init.InitBlocks;
import com.artur.returnoftheancients.utils.math.UltraMutableBlockPos;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;
import thaumcraft.api.ThaumcraftMaterials;
import thaumcraft.api.blocks.BlocksTC;

import java.util.Random;

public class WorldGensMisc {
    public final WorldGenerator REPLACE_VISIBLE_BLOCKS = new WorldGenReplaceVisibleBlocks();
    public final WorldGenerator ADD_TAINT_FEATURE = new WorldGenAddTaintFeature();
    public final WorldGenerator INFERNAL_CIRCLE = new WorldGenInfernalCircle();
    public final WorldGenerator ADD_SNOW = new WorldGenAddSnow();


    public static class WorldGenReplaceVisibleBlocks extends WorldGenerator {

        private final UltraMutableBlockPos blockPos = new UltraMutableBlockPos();

        @Override
        public boolean generate(World worldIn, Random rand, BlockPos position) {
            blockPos.setPos(position);
            for (blockPos.pushPos(); blockPos.getY() > worldIn.getSeaLevel() - 10; blockPos.down()) {
                for (EnumFacing facing : EnumFacing.values()) {
                    blockPos.pushPos();
                    blockPos.offset(facing);
                    if (worldIn.isAirBlock(blockPos)) {
                        blockPos.popPos();
                        Material material = worldIn.getBlockState(blockPos).getMaterial();
                        if (worldIn.getBlockState(blockPos).getMaterial() != ThaumcraftMaterials.MATERIAL_TAINT && !worldIn.isAirBlock(blockPos) && material != Material.SNOW && !material.isLiquid()) {
                            worldIn.setBlockState(blockPos, InitBlocks.TAINT_VOID_STONE.getDefaultState());
                        }
                        break;
                    }
                    blockPos.popPos();
                }
            }
            blockPos.popPos();
            return true;
        }
    }

    public static class WorldGenAddTaintFeature extends WorldGenerator {
        private final UltraMutableBlockPos blockPos = new UltraMutableBlockPos();

        @Override
        public boolean generate(World worldIn, Random rand, BlockPos position) {
            if (rand.nextInt(33) == 0) {
                blockPos.setPos(position);
                if (blockPos.getY() < 100 && worldIn.getBlockState(blockPos).getBlock() == BlocksTC.taintSoil) {
                    blockPos.pushPos();
                    IBlockState state = BlocksTC.taintFeature.getBlockState().getBaseState();
                    blockPos.up();
                    worldIn.setBlockState(blockPos, state.withProperty(BlockDirectional.FACING, EnumFacing.UP), 3);
                    blockPos.popPos();
                }
            }
            return false;
        }
    }

    public static class WorldGenAddSnow extends WorldGenerator {
        private final UltraMutableBlockPos blockPos = new UltraMutableBlockPos();

        @Override
        public boolean generate(World worldIn, Random rand, BlockPos position) {
            blockPos.setPos(position);
            if (blockPos.getY() >= 100) {
                if (blockPos.getY() > 110) {
                    if (worldIn.getBlockState(blockPos).getMaterial() == ThaumcraftMaterials.MATERIAL_TAINT) {
                        blockPos.down();
                    }
                    worldIn.setBlockState(blockPos.up(), Blocks.SNOW_LAYER.getDefaultState());
                } else if (rand.nextBoolean()) {
                    worldIn.setBlockState(blockPos.up(), Blocks.SNOW_LAYER.getDefaultState());
                }
            }
            return false;
        }
    }

    public static class WorldGenInfernalCircle extends WorldGenerator {
        private final UltraMutableBlockPos blockPos = new UltraMutableBlockPos();

        @Override
        public boolean generate(World worldIn, Random rand, BlockPos position) {
            blockPos.setPos(position).add(rand.nextInt(16), 0, rand.nextInt(16));
            if (HandlerR.getBiomeIdOnPos(worldIn, blockPos) != Biome.getIdForBiome(InitBiome.TAINT_WASTELAND)) {
                return false;
            }
            genCircle(worldIn, rand, blockPos, 4 + (rand.nextInt(2) + 1));
            return true;
        }

        private void genCircle(World worldIn, Random rand, UltraMutableBlockPos pos, int radius) {
            for (int range1 = 1; range1 <= radius; range1++) {
                int pointsOnCircle = 16 * range1;
                for (int i = 0; i < pointsOnCircle; i++) {
                    float angle = (float) ((Math.PI * 2.0) * i / pointsOnCircle);
                    int xOffset = (int) (range1 * MathHelper.cos(angle));
                    int zOffset = (int) (range1 * MathHelper.sin(angle));

                    pos.pushPos();
                    pos.add(xOffset, 0, zOffset);
                    pos.setY(HandlerR.calculateGenerationHeight(worldIn, pos.getX(), pos.getZ()));
                    if (range1 != radius || (rand == null || rand.nextInt(4) == 0)) {
                        if (worldIn.getBlockState(pos).getMaterial() == ThaumcraftMaterials.MATERIAL_TAINT) {
                            worldIn.setBlockState(pos, InitBlocks.INCANDESCENT_TAINT_VOID_STONE.getDefaultState());
                        }
                    }
                    pos.popPos();
                }
            }
        }

    }
}
