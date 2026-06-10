package com.artur114.thaumrota.common.tileentity.interf;

import net.minecraft.util.math.AxisAlignedBB;

public interface ITileMultiBBProvider {
    AxisAlignedBB[] boundingBoxes();
}
