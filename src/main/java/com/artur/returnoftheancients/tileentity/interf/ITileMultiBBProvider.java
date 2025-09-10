package com.artur.returnoftheancients.tileentity.interf;

import net.minecraft.util.math.AxisAlignedBB;

public interface ITileMultiBBProvider {
    AxisAlignedBB[] boundingBoxes();
}
