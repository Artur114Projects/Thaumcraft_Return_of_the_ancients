package com.artur.returnoftheancients.generation.portal.util;

import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class PortalUtil {
    public static final BlockPos[] portalCollideOffsetsArray;
    public static final BlockPos[] portalOffsetsArray;


    static {
        portalCollideOffsetsArray = initPortalCollideOffsetsArray();
        portalOffsetsArray = initPortalOffsetsArray();
    }

    private static BlockPos[] initPortalCollideOffsetsArray() {
        List<BlockPos> offsets = new ArrayList<>();

        UltraMutableBlockPos pos = UltraMutableBlockPos.getBlockPosFromPoll();
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

        UltraMutableBlockPos.returnBlockPosToPoll(pos);
        return offsets.toArray(new BlockPos[0]);
    }

    private static BlockPos[] initPortalOffsetsArray() {
        List<BlockPos> offsets = new ArrayList<>();

        UltraMutableBlockPos pos = UltraMutableBlockPos.getBlockPosFromPoll();
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

        UltraMutableBlockPos.returnBlockPosToPoll(pos);
        return offsets.toArray(new BlockPos[0]);
    }
}
