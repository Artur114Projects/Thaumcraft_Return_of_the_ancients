package com.artur.returnoftheancients.handlers;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;

public class NBTHandler {
    public static NBTTagCompound axisAlignedBBToNBT(AxisAlignedBB alignedBB) {
        NBTTagCompound alignedBBData = new NBTTagCompound();

        alignedBBData.setDouble("minX", alignedBB.minX);
        alignedBBData.setDouble("minY", alignedBB.minY);
        alignedBBData.setDouble("minZ", alignedBB.minZ);

        alignedBBData.setDouble("maxX", alignedBB.maxX);
        alignedBBData.setDouble("maxY", alignedBB.maxY);
        alignedBBData.setDouble("maxZ", alignedBB.maxZ);

        return alignedBBData;
    }

    public static AxisAlignedBB axisAlignedBBFromNBT(NBTTagCompound data) {
        return new AxisAlignedBB(data.getDouble("minX"), data.getDouble("minY"), data.getDouble("minZ"), data.getDouble("maxX"), data.getDouble("maxY"), data.getDouble("maxZ"));
    }
}
