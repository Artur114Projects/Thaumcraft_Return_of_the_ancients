package com.artur.returnoftheancients.generation.biomes.decorate;

import com.artur.returnoftheancients.init.InitBiome;
import com.artur.returnoftheancients.init.InitBlocks;
import com.artur.returnoftheancients.utils.math.UltraMutableBlockPos;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenInfernalSpires extends WorldGenAbstractTree {
    private final IBlockState taintVoidStone = InitBlocks.TAINT_VOID_STONE.getDefaultState();

    private final UltraMutableBlockPos blockPos = new UltraMutableBlockPos();

    public WorldGenInfernalSpires(boolean notify) {
        super(notify);
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        Chunk chunk = worldIn.getChunkFromBlockCoords(position);
        byte[] biomeArray = chunk.getBiomeArray();
        byte taintId = (byte) (Biome.getIdForBiome(InitBiome.TAINT_WASTELAND) & 255);

        for (byte b : biomeArray) {
            if (b != taintId) {
                return false;
            }
        }

        blockPos.setPos(chunk.getPos()).add(8, 0, 8);
        int blockY = calculateGenerationHeight(worldIn, blockPos);

        int finalSpireHeight = 28;
        int spireHeightOffset = 2;

        int spireHeight = blockY + (finalSpireHeight - ((rand.nextInt(spireHeightOffset) + 1)));

        blockPos.setY(blockY);

        for (blockPos.pushPos(); blockPos.getY() < spireHeight; blockPos.up()) {
            int fY = spireHeight - blockPos.getY();
            int radius = getRadius(fY);
            boolean isBreak = radius != getRadius(fY - 1);
            genCircle(worldIn, rand, blockPos, radius, isBreak);
        }

        blockPos.popPos();
        return true;
    }

    private void genCircle(World worldIn, Random rand, UltraMutableBlockPos pos, int radius, boolean isBreak) {
        if (radius == 0) {
            IBlockState state = rand.nextInt(4) == 0 ? InitBlocks.INCANDESCENT_TAINT_VOID_STONE.getDefaultState() : taintVoidStone;
            worldIn.setBlockState(pos, state);

            return;
        }
        for (int range1 = 1; range1 <= radius; range1++) {
            int pointsOnCircle = 16 * range1;
            for (int i = 0; i < pointsOnCircle; i++) {
                float angle = (float) ((Math.PI * 2.0) * i / pointsOnCircle);
                int xOffset = (int) (range1 * MathHelper.cos(angle));
                int zOffset = (int) (range1 * MathHelper.sin(angle));

                pos.pushPos();
                pos.add(xOffset, 0, zOffset);
                if (range1 != radius || (!isBreak || rand.nextInt(4) == 0)) {
                    IBlockState state = range1 == radius && rand.nextInt(4) == 0 ? InitBlocks.INCANDESCENT_TAINT_VOID_STONE.getDefaultState() : taintVoidStone;
                    worldIn.setBlockState(pos, state);
                }
                pos.popPos();
            }
        }
    }

    private void genRhombus(World worldIn, Random rand, UltraMutableBlockPos pos, int radius) {
        for (int x = 0; x != radius * 2 + 1; x++) {
            for (int z = 0; z != radius * 2 + 1; z++) {
                final int i = Math.abs(x - (radius)) + Math.abs(z - (radius));
                if (i <= radius) {
                    pos.pushPos();
                    pos.add(x - radius , 0, z - radius);
                    if (i != radius || (rand == null || rand.nextInt(4) == 0)) {
                        worldIn.setBlockState(pos, taintVoidStone);
                    }
                    pos.popPos();
                }
            }
        }
    }

    private int getRadius(int y) {
        if (y <= 0) return 0;
        float g = 16;
        return MathHelper.floor((y * y) / (g * g));
    }


    public int calculateGenerationHeight(World world, UltraMutableBlockPos pos) {
        pos.pushPos();
        pos.setY(world.getHeight());

        while (pos.getY() >= 0) {
            Block block = world.getBlockState(pos).getBlock();
            if (block != Blocks.AIR) {
                break;
            }
            pos.down();
        }
        int y = pos.getY();
        pos.popPos();
        return y;
    }
}
