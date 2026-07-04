package com.artur114.thaumrota.common.tileentity;

import com.artur114.bananalib.mc.base.BTileBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public abstract class TileBase extends BTileBase {
    @Override
    @SideOnly(Side.CLIENT)
    protected void readSyncNBT(NBTTagCompound nbt) {}
    @Override
    protected NBTTagCompound writeSyncNBT(NBTTagCompound nbt) {return nbt;}
}
