package com.artur.returnoftheancients.generation.portal.util;

import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class OffsetsUtil {
    public static final BlockPos[] portalCollideOffsetsArray;
    public static final BlockPos[][] portalLightOffsets;
    public static final BlockPos[] portalOffsetsArray;


    static {
        portalCollideOffsetsArray = initPortalCollideOffsetsArray();
        portalLightOffsets = initPortalLightOffsets();
        portalOffsetsArray = initPortalOffsetsArray();
    }

    private static BlockPos[] initPortalCollideOffsetsArray() {
        List<BlockPos> offsets = new ArrayList<>();

        UltraMutableBlockPos pos = UltraMutableBlockPos.obtain();
        pos.setPos(6, 0, 6);

        for (int x = 0; x != 4; x++) {
            for (int z = 0; z != 4; z++) {
                if ((x == 0 && z == 0) || (x == 3 && z == 3) || (x == 3 && z == 0) || (x == 0 && z == 3)) {
                    continue;
                }

                pos.pushPos();
                offsets.add(pos.add(x, 0, z).toImmutable());
                pos.popPos();
            }
        }

        UltraMutableBlockPos.release(pos);
        return offsets.toArray(new BlockPos[0]);
    }

    private static BlockPos[] initPortalOffsetsArray() {
        List<BlockPos> offsets = new ArrayList<>();

        UltraMutableBlockPos pos = UltraMutableBlockPos.obtain();
        pos.setPos(5, 0, 5);

        for (int x = 0; x != 6; x++) {
            for (int z = 0; z != 6; z++) {
                if ((x == 0 && z == 0) || (x == 5 && z == 5) || (x == 5 && z == 0) || (x == 0 && z == 5)) {
                    continue;
                }

                pos.pushPos();
                offsets.add(pos.add(x, 0, z).toImmutable());
                pos.popPos();
            }
        }

        UltraMutableBlockPos.release(pos);
        return offsets.toArray(new BlockPos[0]);
    }

    private static BlockPos[][] initPortalLightOffsets() {
        BlockPos[][] ret = new BlockPos[16][0];
        for (int i = 0; i != ret.length; i++) {
            ret[i] = initPortalLightOffset(i);
        }
        return ret;
    }

    private static BlockPos[] initPortalLightOffset(int range) {
        BlockPos[] init = initPortalCollideOffsetsArray();
        BlockPos[] ret = new BlockPos[] {
            new BlockPos(7, 0, 9),
            new BlockPos(8, 0, 9),

            new BlockPos(6, 0, 8),
            new BlockPos(6, 0, 7),

            new BlockPos(7, 0, 6),
            new BlockPos(8, 0, 6),

            new BlockPos(9, 0, 7),
            new BlockPos(9, 0, 8)
        };

        for (int i = 0; i != EnumFacing.HORIZONTALS.length; i++) {
            EnumFacing facing = EnumFacing.HORIZONTALS[i];

            for (int j = i * 2; j != (i * 2) + 2; j++) {
                ret[j] = ret[j].offset(facing, range);
            }
        }

        return ret;
    }

    public static BlockPos[] getCornerOffsets(int min, int max) {
        return new BlockPos[] {new BlockPos(min, 0, max), new BlockPos(min, 0, min), new BlockPos(max, 0, min), new BlockPos(max, 0, max)};
    }
}
