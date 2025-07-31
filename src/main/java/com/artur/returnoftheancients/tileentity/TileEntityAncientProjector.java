package com.artur.returnoftheancients.tileentity;

import com.artur.returnoftheancients.client.audio.BlockProjectorSound;
import com.artur.returnoftheancients.init.InitSounds;
import com.artur.returnoftheancients.init.InitTileEntity;
import com.artur.returnoftheancients.tileentity.interf.ITileBlockPlaceListener;
import com.artur.returnoftheancients.util.EnumAssetLocation;
import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
            Minecraft.getMinecraft().getSoundHandler().playSound(new BlockProjectorSound(this));
        }
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        UltraMutableBlockPos blockPos = UltraMutableBlockPos.getBlockPosFromPoll().setPos(pos).down();

        while (worldIn.isAirBlock(blockPos)) {
            blockPos.down();
        }

        blockPos.up();

        this.distanceToPedestal = pos.getY() - blockPos.getY();

        this.updatePedestal(this.enabled);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);

        compound.setInteger("distanceToPedestal", this.distanceToPedestal);
        compound.setBoolean("enabled", this.enabled);

        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
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
        if (!this.world.isRemote && state) this.world.playSound(null, this.pos, InitSounds.SPOTLIGHT.SOUND, SoundCategory.BLOCKS, 1.0F, 1.0F);
        IBlockState blockState = state ? InitTileEntity.PHANTOM_PEDESTAL.getDefaultState() : Blocks.AIR.getDefaultState();
        this.world.setBlockState(this.pos.add(0, -this.distanceToPedestal, 0), blockState);
    }
}