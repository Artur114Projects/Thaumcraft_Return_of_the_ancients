package com.artur.returnoftheancients.tileentity;

import com.artur.returnoftheancients.client.fx.particle.RotateParticleFlame;
import com.artur.returnoftheancients.client.fx.particle.ParticleFlameCanCollide;
import com.artur.returnoftheancients.init.InitSounds;
import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.tileentity.interf.ITileBlockPlaceListener;
import com.artur.returnoftheancients.tileentity.interf.ITileMultiBBProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class TileEntityIncinerator extends TileBase implements ITileBlockPlaceListener, ITileMultiBBProvider {
    private EnumFacing face = EnumFacing.UP;

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        this.face = EnumFacing.getDirectionFromEntityLiving(pos, placer);
    }

    @Override
    public AxisAlignedBB[] boundingBoxes() {
        return new AxisAlignedBB[] {
            new AxisAlignedBB(0.0 / 16.0, 0.0 / 16.0, 0.0 / 16.0, 16.0 / 16.0, 2.0 / 16.0, 16.0 / 16.0),
            new AxisAlignedBB(1.0 / 16.0, 2.0 / 16.0, 1.0 / 16.0, 15.0 / 16.0, 4.0 / 16.0, 15.0 / 16.0),
            new AxisAlignedBB(3.0 / 16.0, 4.0 / 16.0, 3.0 / 16.0, 13.0 / 16.0, 16.0 / 16.0, 13.0 / 16.0)
        };
    }

    public EnumFacing face() {
        return this.face;
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
