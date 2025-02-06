package com.artur.returnoftheancients.generation.portal.generators;

import com.artur.returnoftheancients.utils.math.UltraMutableBlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import thaumcraft.api.blocks.BlocksTC;

public class GenAncientArch {
    private final UltraMutableBlockPos blockPos = new UltraMutableBlockPos();

    public void generate(World world, BlockPos start, BlockPos end) {
        blockPos.setPos(start);
        int prevYOffset = 0;
        int dx = end.getX() - start.getX();
        int dy = end.getY() - start.getY();
        int dz = end.getZ() - start.getZ();

        int length = Math.max(Math.abs(dx), Math.abs(dz));

        EnumFacing offset;
        if (Math.abs(dx) > Math.abs(dz)) {
            offset = EnumFacing.getFacingFromAxis(dx > 0 ? EnumFacing.AxisDirection.POSITIVE : EnumFacing.AxisDirection.NEGATIVE, EnumFacing.Axis.X);
        } else {
            offset = EnumFacing.getFacingFromAxis(dz > 0 ? EnumFacing.AxisDirection.POSITIVE : EnumFacing.AxisDirection.NEGATIVE, EnumFacing.Axis.Z);
        }


        for (int i = 0; i <= length; i++) {
            blockPos.pushPos();

            double t = (double) i / length;

            int yOffset = getYOffsetFromX(t, start, end);

            blockPos.offset(offset, i);

            if (yOffset != prevYOffset) {
                blockPos.pushPos();
                blockPos.setY(prevYOffset);
                world.setBlockState(blockPos, BlocksTC.stoneEldritchTile.getDefaultState());
                blockPos.popPos();
            }

            if (i != length && yOffset != getYOffsetFromX((double) (i + 1) / length, start, end)) {
                blockPos.pushPos();
                blockPos.setY(getYOffsetFromX((double) (i + 1) / length, start, end));
                world.setBlockState(blockPos, BlocksTC.stoneEldritchTile.getDefaultState());
                blockPos.popPos();
            }

            blockPos.setY(yOffset);

            world.setBlockState(blockPos, BlocksTC.stoneEldritchTile.getDefaultState());
            world.setBlockState(blockPos.addY(1), BlocksTC.stoneAncient.getDefaultState());

            prevYOffset = yOffset;

            blockPos.popPos();
        }
    }

    private int getYOffsetFromX(double t, BlockPos start, BlockPos end) {
        int dx = end.getX() - start.getX();
        int dz = end.getZ() - start.getZ();
        int dy = end.getY() - start.getY();

        int d = Math.max(Math.abs(dx), Math.abs(dz));

        int h = MathHelper.floor(Math.abs(dy) + MathHelper.clamp(d / 16.0D, 0.0D, 16.0D));

        double y = start.getY();
        y += dy * t;
        y += h * (4 * t * (1 -  t));

        return MathHelper.floor(y);
    }
}
