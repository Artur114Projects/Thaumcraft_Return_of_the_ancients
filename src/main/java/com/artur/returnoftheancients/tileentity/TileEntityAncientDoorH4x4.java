package com.artur.returnoftheancients.tileentity;

import com.artur.returnoftheancients.blocks.BlockDummy;
import com.artur.returnoftheancients.init.InitBlocks;
import com.artur.returnoftheancients.init.InitSounds;
import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.block.Block;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import thaumcraft.client.fx.FXDispatcher;

import java.util.HashMap;
import java.util.Map;

public class TileEntityAncientDoorH4x4 extends TileEntityDoorBase implements TileEntityPedestalActive.ITileActivatedWithPedestal {
    private static final Map<EnumDoorState, Map<EnumFacing.Axis, Map<EnumDummyType, AxisAlignedBB>>> axisAlignedBBMap;
    private int startOpenTimer = 0;
    private int activesCount = 0;

    public TileEntityAncientDoorH4x4() {
        super(40, 4, 4, InitBlocks.DUMMY_ANCIENT_STONE);
    }

    public void onBlockBreak() {
        this.breakAll();
    }

    @Override
    public void activate(TileEntityPedestalActive tile) {
        this.activesCount++;

        if (this.activesCount == 4) {
            this.activesCount = 0;
            this.open();
        }
    }

    @Override
    public void update() {
        super.update();

        if (this.startOpenTimer > 0) {

            if (this.world.isRemote) {
                if (this.startOpenTimer == 40) {
                    ((WorldClient) this.world).playSound(this.pos, InitSounds.PNEUMATIC_PUFF_LONG.SOUND, SoundCategory.BLOCKS, 0.25F, 1, false);
                }

                if (this.startOpenTimer == 2) {
                    ((WorldClient) this.world).playSound(this.pos, InitSounds.DOOR_OPEN_1.SOUND, SoundCategory.BLOCKS, 0.5F, 1, false);
                }

                FXDispatcher.INSTANCE.drawVentParticles(pos.getX() + 4.0F / 16.0F, pos.getY() + (12.0F / 16.0F), pos.getZ() + 2, 0.0, 0.001, 0.0, 0x999999, 1.0F);
                FXDispatcher.INSTANCE.drawVentParticles(pos.getX() + 3 + (12.0F / 16.0F), pos.getY() + (12.0F / 16.0F), pos.getZ() + 2, 0.0, 0.001, 0.0, 0x999999, 1.0F);
            }

            this.startOpenTimer--;

            if (this.startOpenTimer == 0) {
                super.open();
            }
        }
    }

    @Override
    public void open() {
        if (this.currentState == EnumDoorState.CLOSE) {
            this.startOpenTimer = 40;
        }
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
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound nbt) {
        nbt = super.writeToNBT(nbt);

        nbt.setInteger("activesCount", this.activesCount);

        return nbt;
    }

    @Override
    public void readFromNBT(@NotNull NBTTagCompound compound) {
        super.readFromNBT(compound);

        this.activesCount = compound.getInteger("activesCount");
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

        open.put(EnumDummyType.DOOR, Block.NULL_AABB);

        open.put(EnumDummyType.FLOOR, new AxisAlignedBB(0, 6.0F / 16.0F, 0, 4.0F / 16.0F, 1, 1));
        open.put(EnumDummyType.ROOF, new AxisAlignedBB(12.0F / 16.0F, 6.0F / 16.0F, 0, 1, 1, 1));

        open.put(EnumDummyType.WALL_NEAR, new AxisAlignedBB(0, 6.0F / 16.0F, 0, 1, 1, 0.5));
        open.put(EnumDummyType.WALL_FAR, new AxisAlignedBB(0, 6.0F / 16.0F, 0.5, 1, 1, 1));

        Map<EnumDummyType, AxisAlignedBB> close = new HashMap<>();

        close.put(EnumDummyType.CORNER_UP_NEAR, Block.FULL_BLOCK_AABB);
        close.put(EnumDummyType.CORNER_UP_FAR, Block.FULL_BLOCK_AABB);
        close.put(EnumDummyType.CORNER_DOWN_NEAR, Block.FULL_BLOCK_AABB);
        close.put(EnumDummyType.CORNER_DOWN_FAR, Block.FULL_BLOCK_AABB);

        close.put(EnumDummyType.DOOR, new AxisAlignedBB(0, 9.0F / 16.0F, 0, 1, 13.0F / 16.0F, 1));

        close.put(EnumDummyType.FLOOR, new AxisAlignedBB(0, 6.0F / 16.0F, 0, 1, 1, 1));
        close.put(EnumDummyType.ROOF, new AxisAlignedBB(0, 6.0F / 16.0F, 0, 1, 1, 1));

        close.put(EnumDummyType.WALL_NEAR, new AxisAlignedBB(0, 6.0F / 16.0F, 0, 1, 1, 1));
        close.put(EnumDummyType.WALL_FAR, new AxisAlignedBB(0, 6.0F / 16.0F, 0, 1, 1, 1));

        axisAlignedBBMap = compileMap(open, close, EnumFacing.Axis.Z);
    }
}
