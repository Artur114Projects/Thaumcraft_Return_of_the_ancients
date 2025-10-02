package com.artur.returnoftheancients.util.math;

import net.minecraft.util.math.AxisAlignedBB;

public class MathUtils {
    public static AxisAlignedBB createBoundingBox(int x, int y, int z, int x1, int y1, int z1) {
        return new AxisAlignedBB(x / 16.0F, y / 16.0F, z / 16.0F, x1 / 16.0F, y1 / 16.0F, z1 / 16.0F);
    }
}
