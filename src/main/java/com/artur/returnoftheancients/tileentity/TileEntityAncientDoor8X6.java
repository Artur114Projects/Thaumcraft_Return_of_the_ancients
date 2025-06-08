package com.artur.returnoftheancients.tileentity;

import com.artur.returnoftheancients.init.InitBlocks;
import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.HashMap;
import java.util.Map;

public class TileEntityAncientDoor8X6 extends TileEntityDoorBase {
    public TileEntityAncientDoor8X6() {
        super(30, 8, 6, InitBlocks.DUMMY_ANCIENT_STONE);
    }

    @Override
    public AxisAlignedBB boundingBox() {
        return Block.FULL_BLOCK_AABB;
    }

    public void onBlockBreak() {
        this.breakAll();
    }

    @Override
    protected Map<EnumDoorState, Map<EnumFacing.Axis, Map<EnumDummyType, AxisAlignedBB>>> staticAxisAlignedBBMap() {
        return compileMap(new HashMap<>(), new HashMap<>(), EnumFacing.Axis.Z);
    }
}
