package com.artur114.thaumrota.common.blocks;

import com.artur114.thaumrota.common.generation.portallegacy.base.AncientPortalsProcessor;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockAncientWorldPortal extends BaseBlock {
    protected static final AxisAlignedBB PORTAL_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D);
    public static final String noCollisionNBT = "noCollisionNBT";

    public BlockAncientWorldPortal(String name, Material material, float hardness, float resistance, SoundType soundType) {
        super(name, material, hardness, resistance, soundType);

        this.setForCreative();
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return PORTAL_AABB;
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
        if (!entityIn.getEntityData().getBoolean(noCollisionNBT)) {
            if (entityIn instanceof EntityPlayerMP) {
                EntityPlayerMP player = (EntityPlayerMP) entityIn;
                AncientPortalsProcessor.onPlayerCollidePortal(player);
                player.fallDistance = 0;
            } else {
                entityIn.setDead();
            }
        }
    }
}
