package com.artur.returnoftheancients.tileentity;

import com.artur.returnoftheancients.blocks.BlockDummy;
import com.artur.returnoftheancients.init.InitBlocks;
import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;

public class TileEntityAncientDoorH4x4 extends TileEntityDoorBase {
    private static final Map<EnumDoorState, Map<EnumFacing.Axis, Map<EnumDummyType, AxisAlignedBB>>> axisAlignedBBMap;
    public TileEntityAncientDoorH4x4() {
        super(40, 4, 4, InitBlocks.DUMMY_ANCIENT_STONE);
    }

    public void onBlockBreak() {
        this.breakAll();
    }

    @Override
    protected void breakAll() {
        UltraMutableBlockPos pos = UltraMutableBlockPos.getBlockPosFromPoll();

        for (int h = 0; h != height; h++) {
            for (int w = 0; w != width; w++) {
                pos.setPos(this.pos).add(h, 0, w);

                if (this.world.getBlockState(pos).getBlock() instanceof BlockDummy) {
                    BlockDummy.SAFE_BREAK = true;
                    this.world.destroyBlock(pos.toImmutable(), true);
                    BlockDummy.SAFE_BREAK = false;
                }
            }
        }

        UltraMutableBlockPos.returnBlockPosToPoll(pos);

        this.world.destroyBlock(this.pos, true);
    }

    @Override
    public void setAxis(EnumFacing.Axis axis) {
        this.axis = EnumFacing.Axis.Z;
    }

    @Override
    public void fillDummy() {
        UltraMutableBlockPos pos = UltraMutableBlockPos.getBlockPosFromPoll();

        for (int h = 0; h != this.height; h++) {
            for (int w = 0; w != this.width; w++) {
                pos.setPos(this.pos).add(h, 0, w);

                boolean res = this.replaceDummy(pos, this.staticAxisAlignedBBMap().get(this.currentState.toBinary()).get(this.axis).get(this.getDummyType(pos)));

                if (!res) {
                    return;
                }
            }
        }

        UltraMutableBlockPos.returnBlockPosToPoll(pos);
    }

    @Override
    protected EnumDummyType getDummyType(BlockPos pos) {
        UltraMutableBlockPos local = new UltraMutableBlockPos(pos).deduct(this.pos);

        if (local.getZ() == width - 1 && local.getX() == height - 1) {
            return EnumDummyType.CORNER_UP_FAR;
        }

        if (local.getZ() == 0 && local.getX() == height - 1) {
            return EnumDummyType.CORNER_UP_NEAR;
        }

        if (local.getZ() == width - 1 && local.getX() == 0) {
            return EnumDummyType.CORNER_DOWN_FAR;
        }

        if (local.getZ() == 0 && local.getX() == 0) {
            return EnumDummyType.CORNER_DOWN_NEAR;
        }

        if (local.getX() == height - 1) {
            return EnumDummyType.ROOF;
        }

        if (local.getX() == 0) {
            return EnumDummyType.FLOOR;
        }

        if (local.getZ() == width - 1) {
            return EnumDummyType.WALL_FAR;
        }

        if (local.getZ() == 0) {
            return EnumDummyType.WALL_NEAR;
        }

        return EnumDummyType.DOOR;

    }

    @Override
    protected void replaceSection(int index) {
        if (index >= width) {
            return;
        }

        UltraMutableBlockPos pos = UltraMutableBlockPos.getBlockPosFromPoll();

        for (int h = 0; h != height; h++) {
            pos.setPos(this.pos).add(h, 0, index);
            this.replaceDummy(pos, this.staticAxisAlignedBBMap().get(this.currentState.toBinary()).get(this.axis).get(this.getDummyType(pos)));
        }

        UltraMutableBlockPos.returnBlockPosToPoll(pos);
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
        open.put(EnumDummyType.FLOOR, Block.FULL_BLOCK_AABB);
        open.put(EnumDummyType.DOOR, Block.NULL_AABB);
        open.put(EnumDummyType.ROOF, Block.FULL_BLOCK_AABB);
        open.put(EnumDummyType.WALL_NEAR, Block.FULL_BLOCK_AABB);
        open.put(EnumDummyType.WALL_FAR, Block.FULL_BLOCK_AABB);

        Map<EnumDummyType, AxisAlignedBB> close = new HashMap<>();

        close.put(EnumDummyType.CORNER_UP_NEAR, Block.FULL_BLOCK_AABB);
        close.put(EnumDummyType.CORNER_UP_FAR, Block.FULL_BLOCK_AABB);
        close.put(EnumDummyType.CORNER_DOWN_NEAR, Block.FULL_BLOCK_AABB);
        close.put(EnumDummyType.CORNER_DOWN_FAR, Block.FULL_BLOCK_AABB);
        close.put(EnumDummyType.FLOOR, Block.FULL_BLOCK_AABB);
        close.put(EnumDummyType.DOOR, Block.FULL_BLOCK_AABB);
        close.put(EnumDummyType.ROOF, Block.FULL_BLOCK_AABB);
        close.put(EnumDummyType.WALL_NEAR, Block.FULL_BLOCK_AABB);
        close.put(EnumDummyType.WALL_FAR, Block.FULL_BLOCK_AABB);

        axisAlignedBBMap = compileMap(open, close, EnumFacing.Axis.Z);
    }
}
