package com.artur114.returnoftheancients.tileentity.interf;

import net.minecraft.util.math.AxisAlignedBB;

public interface ITileMultiBBProvider {
    AxisAlignedBB[] boundingBoxes();
}
