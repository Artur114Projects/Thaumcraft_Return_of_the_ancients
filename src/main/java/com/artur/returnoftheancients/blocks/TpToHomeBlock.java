package com.artur.returnoftheancients.blocks;

import com.artur.returnoftheancients.ancientworldutilities.WorldData;
import com.artur.returnoftheancients.handlers.EventsHandler;
import com.artur.returnoftheancients.handlers.FreeTeleporter;
import com.artur.returnoftheancients.handlers.Handler;
import com.artur.returnoftheancients.main.Main;
import com.artur.returnoftheancients.utils.interfaces.IALGS;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.Reader;

public class TpToHomeBlock extends BaseBlock {
    protected static final AxisAlignedBB HOME_PORTAL_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D);
    public TpToHomeBlock(String name, Material material, float hardness, float resistance, SoundType soundType) {
        super(name, material, hardness, resistance, soundType);
        this.setCreativeTab(Main.ReturnOfTheAncientsTab);
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return HOME_PORTAL_AABB;
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(@NotNull IBlockState blockState, @NotNull IBlockAccess worldIn, @NotNull BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public boolean isOpaqueCube(@NotNull IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(@NotNull IBlockState state) {
        return false;
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull Entity entityIn) {
        if (!entityIn.world.isRemote && entityIn instanceof EntityPlayerMP) {
            EntityPlayer player = (EntityPlayer) entityIn;
            if (player.getServer() != null && player.world != null) {
                EventsHandler.tpToHome(player, 0, 8, 3, 8);
                player.move(MoverType.PLAYER, 8, 3, 8);
            }
        }
    }
}
