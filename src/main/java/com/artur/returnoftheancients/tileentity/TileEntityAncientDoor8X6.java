package com.artur.returnoftheancients.tileentity;

import com.artur.returnoftheancients.init.InitBlocks;
import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.HashMap;
import java.util.Map;

public class TileEntityAncientDoor8X6 extends TileEntityDoorBase {
    private static final Map<EnumDoorState, Map<EnumFacing.Axis, Map<EnumDummyType, AxisAlignedBB>>> axisAlignedBBMap;

    public TileEntityAncientDoor8X6() {
        super(30, 8, 6, InitBlocks.DUMMY_ANCIENT_STONE);
    }

    public void onBlockBreak() {
        this.breakAll();
    }

    @Override
    protected Map<EnumDoorState, Map<EnumFacing.Axis, Map<EnumDummyType, AxisAlignedBB>>> staticAxisAlignedBBMap() {
        return axisAlignedBBMap;
    }

    static {
        Map<EnumDummyType, AxisAlignedBB> open = new HashMap<>();

        open.put(EnumDummyType.CORNER_UP_NEAR, Block.FULL_BLOCK_AABB);
        open.put(EnumDummyType.CORNER_UP_FAR, Block.FULL_BLOCK_AABB);
        open.put(EnumDummyType.CORNER_DOWN_NEAR, Block.FULL_BLOCK_AABB);
        open.put(EnumDummyType.CORNER_DOWN_FAR, Block.FULL_BLOCK_AABB);

        open.put(EnumDummyType.FLOOR, new AxisAlignedBB(4.0F / 16.0F, 0, 0, 12.0F / 16.0F, 4.0F / 16.0F, 1));
        open.put(EnumDummyType.DOOR, Block.NULL_AABB);

        open.put(EnumDummyType.ROOF, new AxisAlignedBB(4.0F / 16.0F, 8.0F / 16.0F, 0, 12.0F / 16.0F, 1, 1));

        open.put(EnumDummyType.WALL_NEAR, new AxisAlignedBB(4.0F / 16.0F, 0, 0, 12.0F / 16.0F, 1, 10.0F / 16.0F));
        open.put(EnumDummyType.WALL_FAR, new AxisAlignedBB(4.0F / 16.0F, 0, 6.0F / 16.0F, 12.0F / 16.0F, 1, 1));

        Map<EnumDummyType, AxisAlignedBB> close = new HashMap<>();

        close.put(EnumDummyType.CORNER_UP_NEAR, Block.FULL_BLOCK_AABB);
        close.put(EnumDummyType.CORNER_UP_FAR, Block.FULL_BLOCK_AABB);
        close.put(EnumDummyType.CORNER_DOWN_NEAR, Block.FULL_BLOCK_AABB);
        close.put(EnumDummyType.CORNER_DOWN_FAR, Block.FULL_BLOCK_AABB);

        close.put(EnumDummyType.FLOOR, new AxisAlignedBB(4.0F / 16.0F, 0, 0, 12.0F / 16.0F, 1, 1));
        close.put(EnumDummyType.DOOR, new AxisAlignedBB(5.0F / 16.0F, 0, 0, 11.0F / 16.0F, 1, 1));

        close.put(EnumDummyType.ROOF, new AxisAlignedBB(4.0F / 16.0F, 0, 0, 12.0F / 16.0F, 1, 1));

        close.put(EnumDummyType.WALL_NEAR, new AxisAlignedBB(4.0F / 16.0F, 0, 0, 12.0F / 16.0F, 1, 1));
        close.put(EnumDummyType.WALL_FAR, new AxisAlignedBB(4.0F / 16.0F, 0, 0, 12.0F / 16.0F, 1, 1));

        axisAlignedBBMap = compileMap(open, close, EnumFacing.Axis.Z);
    }
}
