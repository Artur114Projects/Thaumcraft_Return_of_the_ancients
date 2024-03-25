package com.artur.returnoftheancients.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class TileEntityBossTriggerBlock extends TileEntity {
    @Override
    public BlockPos getPos() {
        return pos;
    }
}