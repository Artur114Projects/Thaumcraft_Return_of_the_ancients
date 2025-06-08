package com.artur.returnoftheancients.tileentity.interf;

import net.minecraft.util.math.AxisAlignedBB;

public interface ITileBBProvider {
    void setBoundingBox(AxisAlignedBB bb);
    AxisAlignedBB boundingBox();
}
