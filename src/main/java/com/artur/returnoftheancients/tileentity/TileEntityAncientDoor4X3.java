package com.artur.returnoftheancients.tileentity;

import com.artur.returnoftheancients.blocks.BlockDummy;
import com.artur.returnoftheancients.handlers.RenderHandler;
import com.artur.returnoftheancients.init.InitBlocks;
import com.artur.returnoftheancients.tileentity.interf.ITileDoor;
import com.artur.returnoftheancients.tileentity.interf.ITileMultiBlock;
import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class TileEntityAncientDoor4X3 extends TileBase implements ITileMultiBlock, ITickable, ITileDoor {
    private final int height = 3;
    private final int width = 4;

    private Map<EnumDummyType, AxisAlignedBB> axisBBDummyOnClose;
    private Map<EnumDummyType, AxisAlignedBB> axisBBDummyOnOpen;
    public AxisAlignedBB alignedBB = Block.FULL_BLOCK_AABB;
    private EnumFacing.Axis axis = EnumFacing.Axis.Z;
    private EnumDoorState currentState;
    private final int maxMoveTick = 20;
    private int prevMoveTick = 20;
    private int moveTick = 20;

    public TileEntityAncientDoor4X3() {
        this.currentState = EnumDoorState.CLOSE;

        this.axisBBDummyOnClose = this.compileAxisBBDummyMap(this.axis, EnumDoorState.CLOSE);
        this.axisBBDummyOnOpen = this.compileAxisBBDummyMap(this.axis, EnumDoorState.OPEN);
    }

    @Override
    public @NotNull AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    @Override
    public void fillDummy() {
        EnumFacing offsetFacing = EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.POSITIVE, axis);
        UltraMutableBlockPos pos = UltraMutableBlockPos.getBlockPosFromPoll();

        for (int h = 0; h != height; h++) {
            for (int w = 0; w != width; w++) {
                pos.setPos(this.pos).up(h).offset(offsetFacing, w);

                boolean res = this.replaceDummy(pos, this.axisBBDummyMap(this.currentState).get(this.getDummyType(pos)));

                if (!res) {
                    return;
                }
            }
        }
    }

    @Override
    public void onDummyBroken(TileEntityDummy dummy) {
        this.breakAll();
    }

    @Override
    public void update() {
        this.prevMoveTick = this.moveTick;

        if (this.currentState == EnumDoorState.OPENING && this.moveTick > 0) {
            this.moveTick--;
        }
        if (this.currentState == EnumDoorState.CLOSING && this.moveTick < this.maxMoveTick) {
            this.moveTick++;
        }

        if (this.moveTick == 12) {
            this.replaceColumn(1);
            this.replaceColumn(2);
        } else
        if (this.moveTick == 2) {
            this.replaceColumn(0);
            this.replaceColumn(3);
        }

        if (this.moveTick == 0) {
            this.currentState = EnumDoorState.OPEN;
        } else
        if (this.moveTick == this.maxMoveTick) {
            this.currentState = EnumDoorState.CLOSE;
        }
    }

    public void onBlockBreak() {
        this.breakAll();
    }

    public void setAxis(EnumFacing.Axis axis) {
        if (axis == EnumFacing.Axis.Y) {
            throw new IllegalArgumentException();
        }

        if (this.axis != axis) {
            this.axisBBDummyOnClose = this.compileAxisBBDummyMap(axis, EnumDoorState.CLOSE);
            this.axisBBDummyOnOpen = this.compileAxisBBDummyMap(axis, EnumDoorState.OPEN);
        }

        this.axis = axis;
    }

    public EnumFacing.Axis axis() {
        return this.axis;
    }

    private void breakAll() {
        EnumFacing offsetFacing = EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.POSITIVE, axis);
        UltraMutableBlockPos pos = UltraMutableBlockPos.getBlockPosFromPoll();

        for (int h = 0; h != height; h++) {
            for (int w = 0; w != width; w++) {
                pos.setPos(this.pos).up(h).offset(offsetFacing, w);

                if (this.world.getBlockState(pos).getBlock() instanceof BlockDummy) {
                    BlockDummy.SAFE_BREAK = true;
                    this.world.destroyBlock(pos.toImmutable(), true);
                    BlockDummy.SAFE_BREAK = false;
                }
            }
        }

        this.world.destroyBlock(this.pos, true);
    }

    private void replaceColumn(int index) {
        if (index >= width) {
            return;
        }
        
        EnumFacing offsetFacing = EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.POSITIVE, axis);
        UltraMutableBlockPos pos = UltraMutableBlockPos.getBlockPosFromPoll();

        for (int h = 0; h != height; h++) {
            pos.setPos(this.pos).offset(offsetFacing, index).up(h);
            this.replaceDummy(pos, this.axisBBDummyMap(this.currentState).get(this.getDummyType(pos)));
        }
    }

    private boolean replaceDummy(BlockPos pos, AxisAlignedBB alignedBB) {
        if (alignedBB == Block.NULL_AABB) {
            BlockDummy.SAFE_BREAK = true;
            this.world.setBlockToAir(pos);
            BlockDummy.SAFE_BREAK = false;

            return true;
        } else {
            TileEntity dummy = this.world.getTileEntity(pos);

            if (dummy == null) {
                return this.placeDummy(pos, alignedBB);
            }

            if (dummy instanceof TileEntityDummy) {
                ((TileEntityDummy) dummy).setAlignedBB(alignedBB);
                return true;
            }

            if (dummy == this) {
                this.alignedBB = alignedBB;
                this.markDirty();
                return true;
            }

            return false;
        }
    }

    private boolean placeDummy(BlockPos pos, AxisAlignedBB alignedBB) {
        if (this.world.getBlockState(pos).getMaterial() != Material.AIR) {
            if (this.currentState != EnumDoorState.CLOSING) {
                this.breakAll();
                return false;
            } else {
                this.world.destroyBlock(pos.toImmutable(), true);
            }
        }

        this.world.setBlockState(pos, InitBlocks.DUMMY_ANCIENT_STONE.getDefaultState());
        TileEntity dummy = this.world.getTileEntity(pos);
        if (dummy instanceof TileEntityDummy) {
            ((TileEntityDummy) dummy).bindParent(this.pos);
            ((TileEntityDummy) dummy).setAlignedBB(alignedBB);
        }
        return true;
    }

    private EnumDummyType getDummyType(BlockPos pos) {
        UltraMutableBlockPos local = new UltraMutableBlockPos(pos).deduct(this.pos);

        if ((local.getCoordinate(this.axis) == width - 1 || local.getCoordinate(this.axis) == 0) && local.getY() == height - 1) {
            return EnumDummyType.CORNER_UP;
        }

        if (local.getY() == height - 1) {
            return EnumDummyType.ROOF;
        }

        if (local.getCoordinate(this.axis) == width - 1) {
            return EnumDummyType.WALL_FAR;
        }

        if (local.getCoordinate(this.axis) == 0) {
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
                map.put(EnumDummyType.WALL_NEAR, new AxisAlignedBB(5.0F / 16.0F, 0, 0, 11.0F / 16.0F, 1, 8.0F / 16.0F));
                map.put(EnumDummyType.WALL_FAR, new AxisAlignedBB(5.0F / 16.0F, 0, 8.0F / 16.0F, 11.0F / 16.0F, 1, 1));
                map.put(EnumDummyType.ROOF, new AxisAlignedBB(5.0F / 16.0F, 12.0F / 16.0F, 0, 11.0F / 16.0F, 1, 1));
                break;
            case CLOSE:
                map.put(EnumDummyType.DOOR, new AxisAlignedBB(7.0F / 16.0F, 0, 0, 9.0F / 16.0F, 1, 1));
                map.put(EnumDummyType.CORNER_UP, new AxisAlignedBB(5.0F / 16.0F, 0, 0, 11.0F / 16.0F, 1, 1));
                map.put(EnumDummyType.WALL_NEAR, new AxisAlignedBB(5.0F / 16.0F, 0, 0, 11.0F / 16.0F, 1, 1));
                map.put(EnumDummyType.WALL_FAR, new AxisAlignedBB(5.0F / 16.0F, 0, 0, 11.0F / 16.0F, 1, 1));
                map.put(EnumDummyType.ROOF, new AxisAlignedBB(5.0F / 16.0F, 0, 0, 11.0F / 16.0F, 1, 1));
                break;
        }

        if (axis == EnumFacing.Axis.X) {
            Map<EnumDummyType, AxisAlignedBB> map1 = new HashMap<>();
            map.forEach((k, v) -> {
                if (v != Block.NULL_AABB) {
                    map1.put(k, new AxisAlignedBB(v.minZ, v.minY, v.minX, v.maxZ, v.maxY, v.maxX));
                } else {
                    map1.put(k, Block.NULL_AABB);
                }
            });
            map = map1;
        }

        return map;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        if (compound.hasKey("alignedBBData")) {
            NBTTagCompound data = compound.getCompoundTag("alignedBBData");

            this.alignedBB = new AxisAlignedBB(data.getDouble("minX"), data.getDouble("minY"), data.getDouble("minZ"), data.getDouble("maxX"), data.getDouble("maxY"), data.getDouble("maxZ"));
        }

        if (compound.getBoolean("axis")) {
            this.axis = EnumFacing.Axis.X;
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt = super.writeToNBT(nbt);

        if (this.alignedBB != Block.FULL_BLOCK_AABB) {
            NBTTagCompound alignedBBData = new NBTTagCompound();

            alignedBBData.setDouble("minX", this.alignedBB.minX);
            alignedBBData.setDouble("minY", this.alignedBB.minY);
            alignedBBData.setDouble("minZ", this.alignedBB.minZ);

            alignedBBData.setDouble("maxX", this.alignedBB.maxX);
            alignedBBData.setDouble("maxY", this.alignedBB.maxY);
            alignedBBData.setDouble("maxZ", this.alignedBB.maxZ);

            nbt.setTag("alignedBBData", alignedBBData);
        }

        if (this.axis == EnumFacing.Axis.X) {
            nbt.setBoolean("axis", true);
        }

        return nbt;
    }

    @Override
    public NBTTagCompound writeSyncNBT(NBTTagCompound nbt) {
        return this.writeToNBT(nbt);
    }

    @Override
    public void readSyncNBT(NBTTagCompound nbt) {
        this.readFromNBT(nbt);
    }

    @SideOnly(Side.CLIENT)
    public float doorMoveProgress(float partialTicks) {
        return 1 - MathHelper.cos((float) ((Math.PI / 2) * (RenderHandler.interpolate(this.prevMoveTick, this.moveTick, partialTicks) / this.maxMoveTick)));
    }

    @Override
    public void open() {
        if (this.currentState.isBinary()) {
            this.currentState = EnumDoorState.OPENING;
        }
    }

    @Override
    public void close() {
        if (this.currentState.isBinary()) {
            this.currentState = EnumDoorState.CLOSING;
        }
    }

    @Override
    public boolean isOpen() {
        return this.currentState == EnumDoorState.OPEN;
    }

    @Override
    public boolean isClose() {
        return this.currentState == EnumDoorState.CLOSE;
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

        private boolean isBinary() {
            return this == OPEN || this == CLOSE;
        }
    }
}
