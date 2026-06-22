package com.artur114.thaumrota.common.tileentity;

import com.artur114.bananalib.math.BananaMath;
import com.artur114.bananalib.math.m3d.box.Box3DM;
import com.artur114.bananalib.math.m3d.box.IBox3DM;
import com.artur114.bananalib.math.m3d.matrix.IMatrix3FM;
import com.artur114.bananalib.math.m3d.matrix.Matrix3D;
import com.artur114.bananalib.math.m3d.matrix.Matrix3FM;
import com.artur114.bananalib.math.m3d.vec.IVec3D;
import com.artur114.bananalib.math.m3d.vec.IVec3DM;
import com.artur114.bananalib.math.m3d.vec.Vec3D;
import com.artur114.bananalib.math.m3d.vec.Vec3DM;
import com.artur114.bananalib.mc.BananaMC;
import com.artur114.bananalib.mc.base.tileabs.ITileBlockPlaceListener;
import com.artur114.bananalib.mc.base.tileabs.ITileBlockUseListener;
import com.artur114.bananalib.mc.base.tileabs.ITileMultiBBProvider;
import com.artur114.bananalib.mc.math.m3d.box.AbbMc3D;
import com.artur114.thaumrota.client.event.ClientEventsHandler;
import com.artur114.thaumrota.common.config.RotAConfig;
import com.artur114.thaumrota.common.config.client.EnumFXQuality;
import com.artur114.thaumrota.common.init.InitItems;
import com.artur114.thaumrota.common.util.math.CoordinateMatrix;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import thaumcraft.client.fx.FXDispatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TileEntityAncientPiston extends TileBase implements ITileMultiBBProvider, ITileBlockPlaceListener, ITileBlockUseListener, ITickable {
    private final IMatrix3FM rotateMat = new Matrix3FM();
    private final Random rand = new Random();
    private EnumFacing face = EnumFacing.UP;
    private IBox3DM[] moveBoxes = null;
    private IVec3DM particleVec = null;
    private IVec3D particlePos = null;
    private AbbMc3D[] boxes = null;
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
    public void update() {
        if (this.world.isRemote) {
            if (RotAConfig.client.graphicQuality == EnumFXQuality.POTATO) {
                return;
            }
            if (!BananaMC.isInPlayerView(this.pos, RotAConfig.client.graphicQuality.particleMaxDist())) {
                return;
            }
            if (this.particlePos == null) {
                this.compileMatrix();
            }

            float move = this.moveProcess(1, 40.0F);
            float bound = 0.2F;
            if (move > 1 - bound) {
                float norm = Math.min((move - (1 - bound)) * (1 / bound) + 0.4F, 1);
                int maxParticles = (int) (10 * norm);

                Vec3DM axis = Vec3DM.obtain();
                axis.set(this.face.getDirectionVec().getX(), this.face.getDirectionVec().getY(), this.face.getDirectionVec().getZ());
                Matrix3FM rotMatrix = Matrix3FM.obtain();

                for (int i = 0; i != maxParticles; i++) {
                    rotMatrix.setIdentity().rotate(360.0F * ((float) i / maxParticles) + this.rand.nextFloat() * 45.0F, axis.x(), axis.y(), axis.z());
                    IVec3D rot = rotMatrix.transform(this.particleVec.pushPos());
                    FXDispatcher.INSTANCE.drawVentParticles(this.particlePos.x(), this.particlePos.y(), this.particlePos.z(), rot.x(), rot.y(), rot.z(), 0xa3a3a3, 0.7F);
                    this.particleVec.popPos();
                }

                Matrix3FM.release(rotMatrix);
                Vec3DM.release(axis);
            }
        }
    }

    @Override
    public List<AxisAlignedBB> boundingBoxes() {
        if (this.boxes == null || this.moveBoxes == null) {
            this.compileMatrix();
        }

        this.moveBoxes[0].pushBox();
        this.moveBoxes[1].pushBox();
        this.moveBoxes();

        AbbMc3D[] ret = Arrays.copyOf(this.boxes, 5);
        ret[3] = new AbbMc3D(this.moveBoxes[0]);
        ret[4] = new AbbMc3D(this.moveBoxes[1]);

        this.moveBoxes[0].popBox();
        this.moveBoxes[1].popBox();

        return Arrays.asList(ret);
    }

    private void moveBoxes() {
        float moveRange = 9.0F / 16.0F;
        float angle = (float) (2.0 * Math.PI * this.moveProcess(this.partialTicks(), 40.0F));
        float cos = MathHelper.cos(angle);
        float move = moveRange * (cos - 1.0F) / 2.0F;
        this.rotateMat.pushMatrix();
        this.rotateMat.localTranslate(0, -(moveRange + move), 0);
        this.rotateMat.transform(this.moveBoxes);
        this.rotateMat.popMatrix();
    }

    private float partialTicks() {
        if (this.world.isRemote) {
            return Minecraft.getMinecraft().getRenderPartialTicks();
        } else {
            return 1;
        }
    }

    private void compileMatrix() {
        this.rotateMat.setIdentity();

        switch (this.face) {
            case DOWN:
                this.rotateMat.rotateXAround(0.5, 0.5, 0.5, 180.0F);
                break;
            case EAST:
                this.rotateMat.rotateZAround(0.5, 0.5, 0.5, -90.0F);
                break;
            case WEST:
                this.rotateMat.rotateZAround(0.5, 0.5, 0.5, 90.0F);
                break;
            case NORTH:
                this.rotateMat.rotateXAround(0.5, 0.5, 0.5, -90.0F);
                break;
            case SOUTH:
                this.rotateMat.rotateXAround(0.5, 0.5, 0.5, 90.0F);
                break;
        }

        this.moveBoxes = new IBox3DM[2];
        this.boxes = new AbbMc3D[3];

        this.boxes[0] = new AbbMc3D(this.rotateMat.transform(BananaMC.createBAABBFromPixels(1, 0, 1, 15, 2, 15)));
        this.boxes[1] = new AbbMc3D(this.rotateMat.transform(BananaMC.createBAABBFromPixels(2, 2, 2, 14, 4, 14)));
        this.boxes[2] = new AbbMc3D(this.rotateMat.transform(BananaMC.createBAABBFromPixels(3, 4, 3, 13, 5, 13)));

        this.moveBoxes[0] = BananaMC.createBox3DMFromPixels(4, 2, 4, 12, 14, 12).grow(0.005);
        this.moveBoxes[1] = BananaMC.createBox3DMFromPixels(3, 14, 3, 13, 16, 13).grow(0.005);

        this.particlePos = this.rotateMat.transform(new Vec3D(0.5, 5.0 / 16.0, 0.5)).add(this.pos.getX(), this.pos.getY(), this.pos.getZ());

        Vec3DM axis = Vec3DM.obtain();
        axis.set(this.face.getDirectionVec().getX(), this.face.getDirectionVec().getY(), this.face.getDirectionVec().getZ());
        Vec3DM helper = Vec3DM.obtain();
        if (Math.abs(axis.y()) < 0.99) {
            helper.set(0, 1, 0);
        } else {
            helper.set(1, 0, 0);
        }
        this.particleVec = new Vec3DM(axis.cross(helper).normalize());

        Vec3DM.release(helper);
        Vec3DM.release(axis);
    }

    public float moveProcess(float partialTicks, float max) {
        if (this.world.isRemote) {
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
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound compound) {
        compound = super.writeToNBT(compound);
        compound.setInteger("face", this.face.ordinal());
        compound.setInteger("offset", this.offset);
        return compound;
    }

    @Override
    public void readFromNBT(@NotNull NBTTagCompound compound) {
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
