package com.artur114.thaumrota.common.tileentity;

import com.artur114.bananalib.mc.base.tileabs.ITileBlockPlaceListener;
import com.artur114.bananalib.mc.math.m3d.vec.PosMc3IM;
import com.artur114.thaumrota.client.audio.SoundBlockAncientFan;
import com.artur114.thaumrota.client.audio.SoundBlockProjector;
import com.artur114.thaumrota.client.event.ClientEventsHandler;
import com.artur114.thaumrota.common.init.InitBlocks;
import com.artur114.thaumrota.common.init.InitSounds;
import com.artur114.thaumrota.common.util.math.UltraMutableBlockPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

public class TileEntityAncientProjector extends TileBase implements ITileBlockPlaceListener {
    private boolean enabled = true;
    private int distanceToPedestal;

    public void enable() {
        this.updatePedestal(this.enabled = true);
    }

    public void disable() {
        this.updatePedestal(this.enabled = false);
    }

    public void setState(boolean state) {
        this.updatePedestal(this.enabled = state);
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public int distanceToPedestal() {
        return this.distanceToPedestal;
    }

    @Override
    public void onLoad() {
        if (this.world.isRemote) {
            this.playSound();
        }
        super.onLoad();
    }

    @Override
    public void validate() {
        if (this.world.isRemote && this.isInvalid()) {
            this.playSound();
        }
        super.validate();
    }

    @SideOnly(Side.CLIENT)
    private void playSound() {
        ClientEventsHandler.SOUNDS_MANAGER.playTileSound(this, SoundBlockProjector::new);
    }

    @Override
    public @NotNull AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        PosMc3IM blockPos = PosMc3IM.obtain().set(pos).down();
        while (worldIn.isAirBlock(blockPos)) blockPos.down();
        this.distanceToPedestal = pos.getY() - (blockPos.getY() + 1);
        PosMc3IM.release(blockPos);
        this.updatePedestal(this.enabled);
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound compound) {
        compound = super.writeToNBT(compound);

        compound.setInteger("distanceToPedestal", this.distanceToPedestal);
        compound.setBoolean("enabled", this.enabled);

        return compound;
    }

    @Override
    public void readFromNBT(@NotNull NBTTagCompound compound) {
        super.readFromNBT(compound);

        this.distanceToPedestal = compound.getInteger("distanceToPedestal");
        this.enabled = compound.getBoolean("enabled");
    }

    @Override
    public NBTTagCompound writeSyncNBT(NBTTagCompound nbt) {
        return this.writeToNBT(nbt);
    }

    @Override
    public void readSyncNBT(NBTTagCompound nbt) {
        this.readFromNBT(nbt);
    }

    private void updatePedestal(boolean state) {
        if (!this.world.isRemote && state) this.world.playSound(null, this.pos, InitSounds.SPOTLIGHT, SoundCategory.BLOCKS, 1.0F, 1.0F);
        IBlockState blockState = state ? InitBlocks.PHANTOM_PEDESTAL.getDefaultState() : Blocks.AIR.getDefaultState();
        this.world.setBlockState(this.pos.add(0, -this.distanceToPedestal, 0), blockState);
    }
}