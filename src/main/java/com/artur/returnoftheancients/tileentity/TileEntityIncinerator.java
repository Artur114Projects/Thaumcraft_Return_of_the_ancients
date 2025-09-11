package com.artur.returnoftheancients.tileentity;

import com.artur.returnoftheancients.tileentity.interf.ITileBlockPlaceListener;
import com.artur.returnoftheancients.tileentity.interf.ITileMultiBBProvider;
import com.artur.returnoftheancients.util.math.CoordinateMatrix;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityIncinerator extends TileBase implements ITileBlockPlaceListener, ITileMultiBBProvider {
    private AxisAlignedBB[] boundingBox = null;
    private EnumFacing face = EnumFacing.UP;

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        this.face = EnumFacing.getDirectionFromEntityLiving(pos, placer);
    }

    @Override
    public AxisAlignedBB[] boundingBoxes() {
        if (this.boundingBox == null) {
            this.compileBoundingBox();
        }
        return this.boundingBox;
    }

    public EnumFacing face() {
        return this.face;
    }

    private void compileBoundingBox() {
        CoordinateMatrix matrix = new CoordinateMatrix();
        matrix.putBoundingBox(new AxisAlignedBB(0.0 / 16.0, 0.0 / 16.0, 0.0 / 16.0, 16.0 / 16.0, 2.0 / 16.0, 16.0 / 16.0), 1);
        matrix.putBoundingBox(new AxisAlignedBB(1.0 / 16.0, 2.0 / 16.0, 1.0 / 16.0, 15.0 / 16.0, 4.0 / 16.0, 15.0 / 16.0), 2);
        matrix.putBoundingBox(new AxisAlignedBB(3.0 / 16.0, 4.0 / 16.0, 3.0 / 16.0, 13.0 / 16.0, 16.0 / 16.0, 13.0 / 16.0), 3);
        matrix.translate(-0.5F, -0.5F, -0.5F);
        if (this.face == EnumFacing.DOWN) {
            matrix.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        }
        if (this.face.getAxis().isHorizontal()) {
            matrix.rotate(90.0F * this.face.getAxisDirection().getOffset(), 1.0F * Math.abs(this.face.getFrontOffsetZ()), 0.0F, 1.0F * Math.abs(this.face.getFrontOffsetX()));
        }
        matrix.translate(0.5F, 0.5F, 0.5F);
        this.boundingBox = new AxisAlignedBB[] {
            matrix.getBoundingBox(1),
            matrix.getBoundingBox(2),
            matrix.getBoundingBox(3),
        };
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);
        compound.setInteger("face", this.face.ordinal());
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        if (compound.hasKey("face")) {
            this.face = EnumFacing.values()[compound.getInteger("face")];
        }
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
