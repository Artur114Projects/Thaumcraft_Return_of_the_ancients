package com.artur.returnoftheancients.tileentity;

import com.artur.returnoftheancients.blocks.BlockDummy;
import com.artur.returnoftheancients.handlers.NBTHandler;
import com.artur.returnoftheancients.handlers.RenderHandler;
import com.artur.returnoftheancients.init.InitBlocks;
import com.artur.returnoftheancients.tileentity.interf.ITileBBProvider;
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

public abstract class TileEntityDoorBase extends TileBase implements ITickable, ITileDoor, ITileMultiBlock, ITileBBProvider, TileEntityPedestalActive.ITileActivatedWithPedestal {
    protected AxisAlignedBB alignedBB = Block.FULL_BLOCK_AABB;
    protected EnumFacing.Axis axis = EnumFacing.Axis.Z;
    protected EnumDoorState currentState;
    private final Block blockDummy;
    protected final int maxMoveTick;
    protected int prevMoveTick;
    protected final int height;
    protected final int width;
    protected int moveTick;

    protected TileEntityDoorBase(int maxMoveTick, int width, int height, Block blockDummy) {
        if (width % 2 == 1) {
            throw new IllegalArgumentException();
        }

        this.currentState = EnumDoorState.CLOSE;
        this.maxMoveTick = maxMoveTick;
        this.blockDummy = blockDummy;
        this.height = height;
        this.width = width;

        this.moveTick = this.prevMoveTick = this.maxMoveTick;
    }

    protected abstract Map<EnumDoorState, Map<EnumFacing.Axis, Map<EnumDummyType, AxisAlignedBB>>> staticAxisAlignedBBMap();

    @Override
    public void update() {
        this.prevMoveTick = this.moveTick;

        if (this.currentState == EnumDoorState.OPENING && this.moveTick > 0) {
            this.moveTick--;
        }
        if (this.currentState == EnumDoorState.CLOSING && this.moveTick < this.maxMoveTick) {
            this.moveTick++;
        }

        this.doReplace();

        if (this.moveTick == 0) {
            this.currentState = EnumDoorState.OPEN;
        } else
        if (this.moveTick == this.maxMoveTick) {
            this.currentState = EnumDoorState.CLOSE;
        }
    }

    @Override
    public void activate(TileEntityPedestalActive tile) {
        this.open();
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

    @Override
    public boolean isMoving() {
        return !this.currentState.isBinary();
    }

    @Override
    public void fillDummy() {
        EnumFacing offsetFacing = EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.POSITIVE, axis);
        UltraMutableBlockPos pos = UltraMutableBlockPos.getBlockPosFromPoll();

        for (int h = 0; h != this.height; h++) {
            for (int w = 0; w != this.width; w++) {
                pos.setPos(this.pos).up(h).offset(offsetFacing, w);

                boolean res = this.replaceDummy(pos, this.staticAxisAlignedBBMap().get(this.currentState.toBinary()).get(this.axis).get(this.getDummyType(pos)));

                if (!res) {
                    return;
                }
            }
        }

        UltraMutableBlockPos.returnBlockPosToPoll(pos);
    }

    @Override
    public void onDummyBroken(TileEntityDummy dummy) {
        this.breakAll();
    }

    protected void doReplace() {
        for (int i = this.width / 2; i != 0; i--) {
            if (this.moveTick == (this.maxMoveTick / (this.width / 2)) * i - ((this.maxMoveTick / (this.width / 2)) / 2)) {
                this.replaceSection(i - 1);
                this.replaceSection((this.width - 1) - (i - 1));
            }
        }
    }

    protected void replaceSection(int index) {
        this.replaceColumn(index);
    }

    protected void replaceColumn(int index) {
        if (index >= width) {
            return;
        }

        EnumFacing offsetFacing = EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.POSITIVE, axis);
        UltraMutableBlockPos pos = UltraMutableBlockPos.getBlockPosFromPoll();

        for (int h = 0; h != height; h++) {
            pos.setPos(this.pos).offset(offsetFacing, index).up(h);
            this.replaceDummy(pos, this.staticAxisAlignedBBMap().get(this.currentState.toBinary()).get(this.axis).get(this.getDummyType(pos)));
        }

        UltraMutableBlockPos.returnBlockPosToPoll(pos);
    }

    protected boolean replaceDummy(BlockPos pos, AxisAlignedBB alignedBB) {
        if (alignedBB == Block.NULL_AABB) {
            if (this.world.getTileEntity(pos) instanceof TileEntityDummy) {
                BlockDummy.SAFE_BREAK = true;
                this.world.setBlockToAir(pos);
                BlockDummy.SAFE_BREAK = false;
            }
            return true;
        } else {
            TileEntity dummy = this.world.getTileEntity(pos);

            if (dummy == null) {
                return this.placeDummy(pos, alignedBB);
            }

            if (dummy instanceof TileEntityDummy) {
                if (((TileEntityDummy) dummy).parentPos().equals(this.pos)) {
                    ((TileEntityDummy) dummy).setBoundingBox(alignedBB);
                    return true;
                } else {
                    this.breakAll();
                    return false;
                }
            }

            if (dummy == this) {
                this.alignedBB = alignedBB;
                this.markDirty();
                return true;
            }

            return false;
        }
    }

    protected boolean placeDummy(BlockPos pos, AxisAlignedBB alignedBB) {
        if (this.world.getBlockState(pos).getMaterial() != Material.AIR) {
            if (this.currentState != EnumDoorState.CLOSING) {
                this.breakAll();
                return false;
            } else {
                this.world.destroyBlock(pos.toImmutable(), true);
            }
        }

        this.world.setBlockState(pos, this.blockDummy.getDefaultState());
        TileEntity dummy = this.world.getTileEntity(pos);
        if (dummy instanceof TileEntityDummy) {
            ((TileEntityDummy) dummy).bindParent(this.pos);
            ((TileEntityDummy) dummy).setBoundingBox(alignedBB);
        }
        return true;
    }

    public void setAxis(EnumFacing.Axis axis) {
        if (axis == EnumFacing.Axis.Y) {
            throw new IllegalArgumentException();
        }
        
        this.axis = axis;
    }

    public EnumFacing.Axis axis() {
        return this.axis;
    }

    protected void breakAll() {
        EnumFacing offsetFacing = EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.POSITIVE, this.axis);
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
        UltraMutableBlockPos.returnBlockPosToPoll(pos);

        this.world.destroyBlock(this.pos, true);
    }

    protected EnumDummyType getDummyType(BlockPos pos) {
        UltraMutableBlockPos local = new UltraMutableBlockPos(pos).deduct(this.pos);

        if (local.getCoordinate(this.axis) == width - 1 && local.getY() == height - 1) {
            return EnumDummyType.CORNER_UP_FAR;
        }

        if (local.getCoordinate(this.axis) == 0 && local.getY() == height - 1) {
            return EnumDummyType.CORNER_UP_NEAR;
        }

        if (local.getCoordinate(this.axis) == width - 1 && local.getY() == 0) {
            return EnumDummyType.CORNER_DOWN_FAR;
        }

        if (local.getCoordinate(this.axis) == 0 && local.getY() == 0) {
            return EnumDummyType.CORNER_DOWN_NEAR;
        }

        if (local.getY() == height - 1) {
            return EnumDummyType.ROOF;
        }

        if (local.getY() == 0) {
            return EnumDummyType.FLOOR;
        }

        if (local.getCoordinate(this.axis) == width - 1) {
            return EnumDummyType.WALL_FAR;
        }

        if (local.getCoordinate(this.axis) == 0) {
            return EnumDummyType.WALL_NEAR;
        }

        return EnumDummyType.DOOR;
    }


    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    @Override
    public void readFromNBT(@NotNull NBTTagCompound compound) {
        super.readFromNBT(compound);

        if (compound.hasKey("alignedBBData")) {
            this.alignedBB = NBTHandler.axisAlignedBBFromNBT(compound.getCompoundTag("alignedBBData"));
        }

        if (compound.getBoolean("axis")) {
            this.axis = EnumFacing.Axis.X;
        }

        this.currentState = EnumDoorState.values()[compound.getInteger("currentState")];
        this.moveTick = compound.getInteger("moveTick");
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound nbt) {
        nbt = super.writeToNBT(nbt);

        if (this.alignedBB != Block.FULL_BLOCK_AABB) {
            nbt.setTag("alignedBBData", NBTHandler.axisAlignedBBToNBT(this.alignedBB));
        }

        if (this.axis == EnumFacing.Axis.X) {
            nbt.setBoolean("axis", true);
        }

        nbt.setInteger("currentState", currentState.ordinal());
        nbt.setInteger("moveTick", moveTick);

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
    public void setBoundingBox(AxisAlignedBB bb) {
        this.alignedBB = bb;
    }

    @Override
    public AxisAlignedBB boundingBox() {
        return this.alignedBB;
    }

    public enum EnumDummyType {
        CORNER_UP_NEAR, CORNER_UP_FAR, CORNER_DOWN_NEAR, CORNER_DOWN_FAR, WALL_NEAR, WALL_FAR, ROOF, FLOOR, DOOR
    }

    public enum EnumDoorState {
        CLOSE, OPEN, OPENING, CLOSING;

        public EnumDoorState toBinary() {
            switch (this) {
                case OPENING:
                    return OPEN;
                case CLOSING:
                    return CLOSE;
                default:
                    return this;
            }
        }

        public boolean isBinary() {
            return this == OPEN || this == CLOSE;
        }


    }

    protected static Map<EnumDummyType, AxisAlignedBB> rotate(Map<EnumDummyType, AxisAlignedBB> map) {
        Map<EnumDummyType, AxisAlignedBB> ret = new HashMap<>();

        map.forEach((k, v) -> {
            if (v != Block.NULL_AABB) {
                ret.put(k, new AxisAlignedBB(v.minZ, v.minY, v.minX, v.maxZ, v.maxY, v.maxX));
            } else {
                ret.put(k, Block.NULL_AABB);
            }
        });

        return ret;
    }

    protected static Map<EnumDoorState, Map<EnumFacing.Axis, Map<EnumDummyType, AxisAlignedBB>>> compileMap(Map<EnumDummyType, AxisAlignedBB> open, Map<EnumDummyType, AxisAlignedBB> close, EnumFacing.Axis defAxis) {
        Map<EnumDoorState, Map<EnumFacing.Axis, Map<EnumDummyType, AxisAlignedBB>>> ret = new HashMap<>();
        Map<EnumFacing.Axis, Map<EnumDummyType, AxisAlignedBB>> axisMapMapClose = new HashMap<>();
        Map<EnumFacing.Axis, Map<EnumDummyType, AxisAlignedBB>> axisMapMapOpen = new HashMap<>();

        axisMapMapClose.put(opposeHorizontal(defAxis), rotate(close));
        axisMapMapClose.put(defAxis, close);

        axisMapMapOpen.put(opposeHorizontal(defAxis), rotate(open));
        axisMapMapOpen.put(defAxis, open);

        ret.put(EnumDoorState.CLOSE, axisMapMapClose);
        ret.put(EnumDoorState.OPEN, axisMapMapOpen);

        return ret;
    }


    private static EnumFacing.Axis opposeHorizontal(EnumFacing.Axis axis) {
        switch (axis) {
            case X:
                return EnumFacing.Axis.Z;
            case Z:
                return EnumFacing.Axis.X;
            default:
                new IllegalArgumentException().printStackTrace(System.err); return axis;
        }
    }
}
