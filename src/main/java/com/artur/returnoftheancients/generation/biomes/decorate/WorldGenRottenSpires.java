package com.artur.returnoftheancients.generation.biomes.decorate;

import com.artur.returnoftheancients.handlers.HandlerR;
import com.artur.returnoftheancients.init.InitBlocks;
import com.artur.returnoftheancients.utils.math.UltraMutableBlockPos;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenRottenSpires extends WorldGenerator {
    private final IBlockState taintVoidStone = InitBlocks.TAINT_VOID_STONE.getDefaultState();

    private final UltraMutableBlockPos blockPos = new UltraMutableBlockPos();

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
         blockPos.setPos(position).add(8, 0, 8);
        int blockY = calculateGenerationHeight(worldIn, blockPos);

        int finalSpireHeight = 60;
        int spireHeightOffset = 10;

        int spireHeight = blockY + (finalSpireHeight - (rand.nextInt(spireHeightOffset) + 1));

        blockPos.setY(blockY);

        for (blockPos.pushPos(); blockPos.getY() < spireHeight; blockPos.up()) {
            int fY = spireHeight - blockPos.getY();
            int radius = getRadius(fY);
            Random lRand = null;
            if (radius != getRadius(fY - 1)) {
                lRand = rand;
            }
            genRhombus(worldIn, lRand, blockPos, radius);
        }

        blockPos.popPos();
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
                if (range1 != radius || (rand == null || rand.nextInt(4) == 0)) {
                    worldIn.setBlockState(pos, taintVoidStone);
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
        float g = 20;
        return MathHelper.floor((y * y) / (g * g));
    }


    public int calculateGenerationHeight(World world, UltraMutableBlockPos pos) {
        pos.pushPos();
        pos.setY(world.getHeight());

        while (pos.getY() >= 0) {
            Block block = world.getBlockState(pos).getBlock();
            if (block != Blocks.AIR && block != Blocks.WATER) {
                break;
            }
            pos.add(0, -1, 0);
        }
        int y = pos.getY();
        pos.popPos();
        return y;
    }
}
