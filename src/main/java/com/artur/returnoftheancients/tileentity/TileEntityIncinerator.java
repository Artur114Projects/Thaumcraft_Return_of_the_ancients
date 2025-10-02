package com.artur.returnoftheancients.tileentity;

import com.artur.returnoftheancients.handlers.RenderHandler;
import com.artur.returnoftheancients.tileentity.interf.ITileBlockPlaceListener;
import com.artur.returnoftheancients.tileentity.interf.ITileMultiBBProvider;
import com.artur.returnoftheancients.util.math.CoordinateMatrix;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class TileEntityIncinerator extends TileBase implements ITickable, ITileBlockPlaceListener, ITileMultiBBProvider {
    private EnumFacing face = EnumFacing.UP;
    private CoordinateMatrix matrix = null;
    private final int maxMoveProcess = 20;
    private int prevMoveProcess = 20;
    private int moveProcess = 20;

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        this.face = EnumFacing.getDirectionFromEntityLiving(pos, placer);
    }

    @Override
    public AxisAlignedBB[] boundingBoxes() {
        if (this.matrix == null) {
            this.compileMatrix();
        }
        float partialTicks = 1.0F;
        if (this.world.isRemote) partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();
        CoordinateMatrix move = this.matrix.child("move");
        move.clearTransforms(false);
        move.translate(((12.0F / 16.0F) * this.moveProcess(partialTicks)) * -this.face.getFrontOffsetX(), ((12.0F / 16.0F) * this.moveProcess(partialTicks)) * -this.face.getFrontOffsetY(), ((12.0F / 16.0F) * this.moveProcess(partialTicks)) * -this.face.getFrontOffsetZ());
        return this.matrix.allBoundingBoxes().toArray(new AxisAlignedBB[0]);
    }

    @Override
    public void update() {
        this.prevMoveProcess = this.moveProcess;

        if (this.moveProcess > 0 && this.power() == 0.0F) {
            this.moveProcess--;
        } else if (this.moveProcess < this.maxMoveProcess && this.power() > 0.0F) {
            this.moveProcess++;
        }
    }

    public EnumFacing face() {
        return this.face;
    }

    public float power() {
        return 0.0F;
    }

    public float moveProcess(float partialTicks) {
        return MathHelper.cos((float) ((Math.PI / 2.0F) * (RenderHandler.interpolate(this.prevMoveProcess, this.moveProcess, partialTicks) / this.maxMoveProcess)));
    }

    private void compileMatrix() {
        this.matrix = new CoordinateMatrix();
        this.matrix.putBoundingBox(new AxisAlignedBB(0.0 / 16.0, 0.0 / 16.0, 0.0 / 16.0, 16.0 / 16.0, 2.0 / 16.0, 16.0 / 16.0), 1);
        this.matrix.putBoundingBox(new AxisAlignedBB(1.0 / 16.0, 2.0 / 16.0, 1.0 / 16.0, 15.0 / 16.0, 4.0 / 16.0, 15.0 / 16.0), 2);
        this.matrix.child("move").putBoundingBox(new AxisAlignedBB(3.0 / 16.0, 4.0 / 16.0, 3.0 / 16.0, 13.0 / 16.0, 16.0 / 16.0, 13.0 / 16.0), 1);
        this.matrix.translate(-0.5F, -0.5F, -0.5F);
        if (this.face == EnumFacing.DOWN) {
            this.matrix.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        }
        if (this.face.getAxis().isHorizontal()) {
            this.matrix.rotate(90.0F * this.face.getAxisDirection().getOffset(), 1.0F * Math.abs(this.face.getFrontOffsetZ()), 0.0F, 1.0F * Math.abs(this.face.getFrontOffsetX()));
        }
        this.matrix.translate(0.5F, 0.5F, 0.5F);
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
