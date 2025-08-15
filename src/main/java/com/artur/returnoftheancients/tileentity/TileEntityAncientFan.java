package com.artur.returnoftheancients.tileentity;

import com.artur.returnoftheancients.client.audio.BlockAncientFanSound;
import com.artur.returnoftheancients.client.fx.particle.ParticleFlameCanCollide;
import com.artur.returnoftheancients.handlers.MiscHandler;
import com.artur.returnoftheancients.tileentity.interf.ITileBlockPlaceListener;
import com.artur.returnoftheancients.tileentity.interf.ITileBurner;
import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityAncientFan extends TileBase implements ITileBlockPlaceListener, ITickable, ITileBurner {
    private EnumFacing.Axis axis = EnumFacing.Axis.Y;
    private final float activeSpinSpeed = 6.0F;
    private final int maxActiveTime = 40;
    private boolean isActive = false;
    private int prevActiveTime = 0;
    private int redStoneLevel = 0;
    private int activeTime = 0;

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (Math.abs(placer.rotationPitch) > 50) {
            this.axis = EnumFacing.Axis.Y;
        } else {
            this.axis = placer.getHorizontalFacing().getAxis();
        }
    }

    public EnumFacing.Axis axis() {
        return this.axis;
    }

    public boolean canConnectRedstone(EnumFacing facing) {
        return facing != EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.POSITIVE, this.axis) && facing != EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.NEGATIVE, this.axis);
    }

    @SideOnly(Side.CLIENT)
    public float spinSpeed(float pct) {
        return MiscHandler.interpolate(this.localSpinSpeed(), this.activeSpinSpeed, MiscHandler.interpolate(this.prevActiveTime, this.activeTime, pct) / this.maxActiveTime);
    }

    @Override
    public void update() {
        this.prevActiveTime = this.activeTime;

        if (this.isActive && this.activeTime < this.maxActiveTime) {
            this.activeTime++;
        }
        if (!this.isActive && this.activeTime > 0) {
            this.activeTime--;
        }

        if (this.world.isRemote && this.spinSpeed(1) > 4) {
            this.spawnParticles();
        }

        if (this.world.isRemote) {
            this.checkRedStone();
        }
    }

    @Override
    public void onLoad() {
        if (this.world.isRemote) {
            Minecraft.getMinecraft().getSoundHandler().playSound(new BlockAncientFanSound(this));
        }
        super.onLoad();
    }

    @Override
    public void validate() {
        if (this.world.isRemote && this.isInvalid()) {
            Minecraft.getMinecraft().getSoundHandler().playSound(new BlockAncientFanSound(this));
        }
        super.validate();
    }

    private float localSpinSpeed() {
        return 3.0F + 3.0F * (this.redStoneLevel / 15.0F);
    }

    private void checkRedStone() {
        UltraMutableBlockPos blockPos = UltraMutableBlockPos.getBlockPosFromPoll();
        this.redStoneLevel = 0;
        for (EnumFacing facing : EnumFacing.VALUES) {
            if (facing.getAxis() == this.axis) {
                continue;
            }

            int r = world.getRedstonePower(blockPos.setPos(this.pos).offset(facing), facing.getOpposite());
            if (this.redStoneLevel < r) this.redStoneLevel = r;
        }
        UltraMutableBlockPos.returnBlockPosToPoll(blockPos);
    }

    @SideOnly(Side.CLIENT)
    private void spawnParticles() {
        EnumFacing facing0 = EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.POSITIVE, this.axis);
        EnumFacing facing1 = EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.NEGATIVE, this.axis);
        UltraMutableBlockPos blockPos = UltraMutableBlockPos.getBlockPosFromPoll();
        if (this.world.isAirBlock(blockPos.setPos(this.pos).offset(facing0))) {
            float speed = 0.6F + (0.3F * (this.spinSpeed(1) - 4) / 2.0F);
            double x = this.pos.getX() + 0.5 + (facing0.getFrontOffsetX() == 0 ? ((this.world.rand.nextFloat() * 0.8) + 0.1F) - 0.5F : 0);
            double y = this.pos.getY() + 0.5 + (facing0.getFrontOffsetY() == 0 ? ((this.world.rand.nextFloat() * 0.8) + 0.1F) - 0.5F : 0);
            double z = this.pos.getZ() + 0.5 + (facing0.getFrontOffsetZ() == 0 ? ((this.world.rand.nextFloat() * 0.8) + 0.1F) - 0.5F : 0);
            this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y, z, speed * facing0.getFrontOffsetX(), speed * facing0.getFrontOffsetY(), speed * facing0.getFrontOffsetZ());

            if (this.spinSpeed(1) >= 6 && world.rand.nextFloat() < 0.25F) {
                x = this.pos.getX() + 0.5 + (facing0.getFrontOffsetX() == 0 ? ((this.world.rand.nextFloat() * 0.8) + 0.1F) - 0.5F : 0);
                y = this.pos.getY() + 0.5 + (facing0.getFrontOffsetY() == 0 ? ((this.world.rand.nextFloat() * 0.8) + 0.1F) - 0.5F : 0);
                z = this.pos.getZ() + 0.5 + (facing0.getFrontOffsetZ() == 0 ? ((this.world.rand.nextFloat() * 0.8) + 0.1F) - 0.5F : 0);
                Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleFlameCanCollide(this.world, x, y, z, (speed + 0.1F) * facing0.getFrontOffsetX(), (speed + 0.1F) * facing0.getFrontOffsetY(), (speed + 0.1F) * facing0.getFrontOffsetZ()));
            }
        }
        if (this.world.isAirBlock(blockPos.setPos(this.pos).offset(facing1))) {
            float speed = 0.6F + (0.3F * (this.spinSpeed(1) - 4) / 2.0F);
            double x = this.pos.getX() + 0.5 + (facing1.getFrontOffsetX() == 0 ? ((this.world.rand.nextFloat() * 0.8) + 0.1F) - 0.5F : 0);
            double y = this.pos.getY() + 0.5 + (facing1.getFrontOffsetY() == 0 ? ((this.world.rand.nextFloat() * 0.8) + 0.1F) - 0.5F : 0);
            double z = this.pos.getZ() + 0.5 + (facing1.getFrontOffsetZ() == 0 ? ((this.world.rand.nextFloat() * 0.8) + 0.1F) - 0.5F : 0);
            this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y, z, speed * facing1.getFrontOffsetX(), speed * facing1.getFrontOffsetY(), speed * facing1.getFrontOffsetZ());

            if (this.spinSpeed(1) >= 6 && world.rand.nextFloat() < 0.25F) {
                x = this.pos.getX() + 0.5 + (facing1.getFrontOffsetX() == 0 ? ((this.world.rand.nextFloat() * 0.8) + 0.1F) - 0.5F : 0);
                y = this.pos.getY() + 0.5 + (facing1.getFrontOffsetY() == 0 ? ((this.world.rand.nextFloat() * 0.8) + 0.1F) - 0.5F : 0);
                z = this.pos.getZ() + 0.5 + (facing1.getFrontOffsetZ() == 0 ? ((this.world.rand.nextFloat() * 0.8) + 0.1F) - 0.5F : 0);
                Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleFlameCanCollide(this.world, x, y, z, (speed + 0.1F) * facing1.getFrontOffsetX(), (speed + 0.1F) * facing1.getFrontOffsetY(), (speed + 0.1F) * facing1.getFrontOffsetZ()));
            }

        }
        UltraMutableBlockPos.returnBlockPosToPoll(blockPos);
    }

    @Override
    public void deactivate() {
        this.isActive = false;
    }

    @Override
    public void activate() {
        this.isActive = true;
    }

    @Override
    public boolean isActive() {
        return this.isActive;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        this.axis = EnumFacing.Axis.values()[compound.getInteger("axis")];
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);
        compound.setInteger("axis", this.axis.ordinal());
        return compound;
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
