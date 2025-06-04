package com.artur.returnoftheancients.tileentity;

import com.artur.returnoftheancients.init.InitBlocks;
import com.artur.returnoftheancients.tileentity.interf.ITileMultiBlock;
import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class TileEntityAncientDoor4X3 extends TileBase implements ITileMultiBlock {
    private final int height = 3;
    private final int width = 4;

    private Map<EnumDummyType, AxisAlignedBB> axisBBDummyOnClose;
    private Map<EnumDummyType, AxisAlignedBB> axisBBDummyOnOpen;
    private EnumDoorState currentState;

    public TileEntityAncientDoor4X3() {
        this.currentState = EnumDoorState.CLOSE;

        this.axisBBDummyOnClose = this.compileAxisBBDummyMap(EnumFacing.Axis.Z, EnumDoorState.CLOSE);
        this.axisBBDummyOnOpen = this.compileAxisBBDummyMap(EnumFacing.Axis.Z, EnumDoorState.OPEN);
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(this.pos.add(-2, -2, -2), this.pos.add(3, 4, 5));
    }

    @Override
    public void fillDummy() {
        EnumFacing.Axis axis = EnumFacing.Axis.Z;
        EnumFacing offsetFacing = EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.POSITIVE, axis);
        UltraMutableBlockPos pos = UltraMutableBlockPos.getBlockPosFromPoll();

        for (int h = 0; h != height; h++) {
            for (int w = 0; w != width; w++) {
                pos.setPos(this.pos).up(h).offset(offsetFacing, w);

                this.replaceDummy(pos, this.axisBBDummyMap(this.currentState).get(this.getDummyType(pos)));
            }
        }
    }

    @Override
    public void onDummyBroken(TileEntityDummy dummy) {
        this.breakAll();
    }

    private void breakAll() {

    }

    private void replaceDummy(BlockPos pos, AxisAlignedBB alignedBB) {
        if (alignedBB == Block.NULL_AABB) {
            this.world.setBlockToAir(pos);
        } else {
            TileEntity dummy = this.world.getTileEntity(pos);

            if (dummy == null) {
                this.placeDummy(pos, alignedBB); return;
            }

            if (dummy instanceof TileEntityDummy) {
                ((TileEntityDummy) dummy).setAlignedBB(alignedBB);
            }
        }
    }

    private void placeDummy(BlockPos pos, AxisAlignedBB alignedBB) {
        if (this.world.getBlockState(pos).getMaterial() != Material.AIR) {
            if (this.currentState != EnumDoorState.CLOSING) {
                this.breakAll();
                return;
            } else {
                this.world.destroyBlock(pos, true);
            }
        }

        this.world.setBlockState(pos, InitBlocks.DUMMY_ANCIENT_STONE.getDefaultState());
        TileEntity dummy = this.world.getTileEntity(pos);
        if (dummy instanceof TileEntityDummy) {
            ((TileEntityDummy) dummy).bindParent(this.pos);
            ((TileEntityDummy) dummy).setAlignedBB(alignedBB);
        }
    }

    private EnumDummyType getDummyType(BlockPos pos) {
        UltraMutableBlockPos local = new UltraMutableBlockPos(pos).deduct(this.pos);

        if ((local.getZ() == width - 1 || local.getZ() == 0) && local.getY() == height - 1) {
            return EnumDummyType.CORNER_UP;
        }

        if (local.getY() == height - 1) {
            return EnumDummyType.ROOF;
        }

        if (local.getZ() == width - 1) {
            return EnumDummyType.WALL_FAR;
        }

        if (local.getZ() == 0) {
            return EnumDummyType.WALL_NEAR;
        }

        return EnumDummyType.DOOR;
    }

    @NotNull
    private Map<EnumDummyType, AxisAlignedBB> axisBBDummyMap(EnumDoorState state) {
        switch (state.toBinary()) {
            case OPEN:
                return this.axisBBDummyOnOpen;
            case CLOSE:
                return this.axisBBDummyOnClose;
            default:
                return null;
        }
    }

    private Map<EnumDummyType, AxisAlignedBB> compileAxisBBDummyMap(EnumFacing.Axis axis, EnumDoorState state) {
        Map<EnumDummyType, AxisAlignedBB> map = new HashMap<>();
        switch (state.toBinary()) {
            case OPEN:
                map.put(EnumDummyType.DOOR, Block.NULL_AABB);
                map.put(EnumDummyType.CORNER_UP, new AxisAlignedBB(5.0F / 16.0F, 0, 0, 11.0F / 16.0F, 1, 1));
                map.put(EnumDummyType.WALL_NEAR, new AxisAlignedBB(5.0F / 16.0F, 0, 0, 11.0F / 16.0F, 1, 1));
                map.put(EnumDummyType.WALL_FAR, new AxisAlignedBB(5.0F / 16.0F, 0, 0, 11.0F / 16.0F, 1, 1));
                map.put(EnumDummyType.ROOF, new AxisAlignedBB(5.0F / 16.0F, 0, 0, 11.0F / 16.0F, 1, 1));
                break;
            case CLOSE:
                map.put(EnumDummyType.DOOR, new AxisAlignedBB(7.0F / 16.0F, 0, 0, 9.0F / 16.0F, 1, 1));
                map.put(EnumDummyType.CORNER_UP, new AxisAlignedBB(5.0F / 16.0F, 0, 0, 11.0F / 16.0F, 1, 1));
                map.put(EnumDummyType.WALL_NEAR, new AxisAlignedBB(5.0F / 16.0F, 0, 0, 11.0F / 16.0F, 1, 1));
                map.put(EnumDummyType.WALL_FAR, new AxisAlignedBB(5.0F / 16.0F, 0, 0, 11.0F / 16.0F, 1, 1));
                map.put(EnumDummyType.ROOF, new AxisAlignedBB(5.0F / 16.0F, 0, 0, 11.0F / 16.0F, 1, 1));
                break;
        }
        return map;
    }

    private enum EnumDummyType {
        CORNER_UP, WALL_NEAR, WALL_FAR, ROOF, DOOR
    }

    public enum EnumDoorState {
        OPEN, CLOSE, OPENING, CLOSING;

        private EnumDoorState toBinary() {
            switch (this) {
                case OPENING:
                    return OPEN;
                case CLOSING:
                    return CLOSE;
                default:
                    return this;
            }
        }
    }
}
