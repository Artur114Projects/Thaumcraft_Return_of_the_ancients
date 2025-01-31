package com.artur.returnoftheancients.generation.portal.generators;

import com.artur.returnoftheancients.utils.math.UltraMutableBlockPos;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import thaumcraft.api.blocks.BlocksTC;

public class GenAncientArch {
    private final UltraMutableBlockPos blockPos = new UltraMutableBlockPos();
    private final int baseSize = 32;

    public void generate(World world, BlockPos pos, EnumFacing offset, int length) {
        blockPos.setPos(pos);
        int prevYOffset = 0;
        double scale = (double) baseSize / length;

        for (int i = 0; i <= length; i++) {
            blockPos.pushPos();
            int yOffset = getYOffsetFromX(i * scale);

            if (yOffset != prevYOffset) {
                blockPos.pushPos();
                world.setBlockState(blockPos.addY(prevYOffset), BlocksTC.stoneEldritchTile.getDefaultState());
                blockPos.popPos();
            }

            if (i != length && yOffset != getYOffsetFromX((i + 1) * scale)) {
                blockPos.pushPos();
                world.setBlockState(blockPos.addY(getYOffsetFromX((i + 1) * scale)), BlocksTC.stoneEldritchTile.getDefaultState());
                blockPos.popPos();
            }

            blockPos.addY(yOffset);

            world.setBlockState(blockPos, BlocksTC.stoneEldritchTile.getDefaultState());
            world.setBlockState(blockPos.addY(1), BlocksTC.stoneAncient.getDefaultState());

            prevYOffset = yOffset;

            blockPos.popPos();

            blockPos.offset(offset);
        }
    }


    private int getYOffsetFromX(double x) {
        final int[] offsetArray = new int[] {0, 1, 2, 3, 3, 4, 4, 4, 5, 5, 5};

        if (x < offsetArray.length) {
            return offsetArray[MathHelper.floor(x)];
        } else if (x > baseSize - offsetArray.length) {
            return offsetArray[MathHelper.floor(baseSize - x)];
        } else {
            return 6;
        }
    }
}
