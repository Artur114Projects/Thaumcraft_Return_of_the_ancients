package com.artur.returnoftheancients.tileentity;

import com.artur.returnoftheancients.client.event.ClientEventsHandler;
import com.artur.returnoftheancients.init.InitItems;
import com.artur.returnoftheancients.tileentity.interf.ITileBlockPlaceListener;
import com.artur.returnoftheancients.tileentity.interf.ITileBlockUseListener;
import com.artur.returnoftheancients.tileentity.interf.ITileMultiBBProvider;
import com.artur.returnoftheancients.util.math.CoordinateMatrix;
import com.artur.returnoftheancients.util.math.MathUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class TileEntityAncientPiston extends TileBase implements ITileMultiBBProvider, ITileBlockPlaceListener, ITileBlockUseListener {
    private final CoordinateMatrix matrix = new CoordinateMatrix();
    private EnumFacing face = EnumFacing.UP;
    private int offset = 0;

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        this.face = EnumFacing.getDirectionFromEntityLiving(pos, placer);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (playerIn.getHeldItem(hand).getItem() == InitItems.DEBUG_CARROT) {
            this.offset += playerIn.isSneaking() ? -5 : 5;
            this.offset %= 40;
            playerIn.sendStatusMessage(new TextComponentString("Offset: " + this.offset), true);
            return true;
        }
        return false;
    }

    @Override
    public AxisAlignedBB[] boundingBoxes() {
        synchronized (this.matrix) {
            if (this.matrix.isEmpty()) {
                this.compileMatrix();
            }

            this.moveMatrix();

            return this.matrix.allBoundingBoxesArr();
        }
    }

    private void moveMatrix() {
        CoordinateMatrix move = this.matrix.child("move");
        move.clearTransforms(false);
        float delta = (-9.0F / 16.0F - (9.0F / 16.0F * MathHelper.cos((float) ((Math.PI * this.moveProcess(40.0F)) + (Math.PI / 2.0F)))));
        move.translate(delta * this.face.getFrontOffsetX(), delta * this.face.getFrontOffsetY(), delta * this.face.getFrontOffsetZ());
    }

    private void compileMatrix() {
        this.matrix.putBoundingBox(MathUtils.createBoundingBox(1, 0, 1, 15, 2, 15), 1);
        this.matrix.putBoundingBox(MathUtils.createBoundingBox(2, 2, 2, 14, 4, 14), 2);
        this.matrix.putBoundingBox(MathUtils.createBoundingBox(3, 4, 3, 13, 5, 13), 3);
        CoordinateMatrix move = this.matrix.child("move");
        move.putBoundingBox(MathUtils.createBoundingBox(4, 2, 4, 12, 14, 12), 1);
        move.putBoundingBox(MathUtils.createBoundingBox(3, 14, 3, 13, 16, 13), 2);
        this.matrix.translate(-0.5F, -0.5F, -0.5F);
        if (this.face == EnumFacing.DOWN) {
            this.matrix.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        }
        if (this.face.getAxis().isHorizontal()) {
            this.matrix.rotate(90.0F * this.face.getAxisDirection().getOffset(), 1.0F * Math.abs(this.face.getFrontOffsetZ()), 0.0F, 1.0F * Math.abs(this.face.getFrontOffsetX()));
        }
        this.matrix.translate(0.5F, 0.5F, 0.5F);
    }

    public float moveProcess(float max) {
        if (this.world.isRemote) {
            float partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();
            return (float) ((ClientEventsHandler.GLOBAL_TICK_MANAGER.interpolatedUnloadGameTickCounter(partialTicks) + this.offset) % max) / max;
        } else {
            MinecraftServer server = this.world.getMinecraftServer();
            if (server == null) return 0;
            return ((server.getTickCounter() + this.offset) % max) / max;
        }
    }

    public EnumFacing face() {
        return this.face;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);
        compound.setInteger("face", this.face.ordinal());
        compound.setInteger("offset", this.offset);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        if (compound.hasKey("face")) {
            this.face = EnumFacing.values()[compound.getInteger("face")];
            this.offset = compound.getInteger("offset");
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
