package com.artur.returnoftheancients.tileentity;

import com.artur.returnoftheancients.util.EnumAssetLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;

public class TileEntityAncientProjector extends TileBase {

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    @Override
    public boolean shouldRenderInPass(int pass) {
        return true;
    }
}
