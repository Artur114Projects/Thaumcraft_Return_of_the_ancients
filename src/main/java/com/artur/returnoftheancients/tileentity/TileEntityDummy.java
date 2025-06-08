package com.artur.returnoftheancients.tileentity;

import com.artur.returnoftheancients.tileentity.interf.ITileBBProvider;
import com.artur.returnoftheancients.tileentity.interf.ITileMultiBlock;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class TileEntityDummy extends TileBase implements ITileBBProvider {
    protected AxisAlignedBB alignedBB = Block.FULL_BLOCK_AABB;
    protected BlockPos parent = null;

    public void setBoundingBox(AxisAlignedBB alignedBB) {
        this.alignedBB = alignedBB;

        this.markDirty();
    }

    public AxisAlignedBB boundingBox() {
        return this.alignedBB;
    }

    public void bindParent(BlockPos pos) {
        this.parent = pos;
    }

    public void onBreak() {
        ITileMultiBlock tile = this.parent();
        if (tile != null) {
            tile.onDummyBroken(this);
        }
    }

    public BlockPos parentPos() {
        return this.parent;
    }

    public ITileMultiBlock parent() {
        TileEntity tile = this.world.getTileEntity(this.parent);

        if (tile instanceof ITileMultiBlock) {
            return (ITileMultiBlock) tile;
        }

        return null;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        if (compound.hasKey("alignedBBData")) {
            NBTTagCompound data = compound.getCompoundTag("alignedBBData");

            this.alignedBB = new AxisAlignedBB(data.getDouble("minX"), data.getDouble("minY"), data.getDouble("minZ"), data.getDouble("maxX"), data.getDouble("maxY"), data.getDouble("maxZ"));
        }

        if (compound.hasKey("parent")) {
            this.parent = BlockPos.fromLong(compound.getLong("parent"));
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

        if (this.parent != null) {
            nbt.setLong("parent", this.parent.toLong());
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
}
