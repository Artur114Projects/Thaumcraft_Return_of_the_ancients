package com.artur.returnoftheancients.blocks;

import com.artur.returnoftheancients.ancientworld.system.base.AncientLayer1EventsHandler;
import com.artur.returnoftheancients.generation.portal.base.AncientPortalsProcessor;
import com.artur.returnoftheancients.init.InitDimensions;
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

public class BlockTpToHome extends BaseBlock {
    protected static final AxisAlignedBB HOME_PORTAL_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D);
    public BlockTpToHome(String name, Material material, float hardness, float resistance, SoundType soundType) {
        super(name, material, hardness, resistance, soundType);
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
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
    public void onEntityCollidedWithBlock(@NotNull World worldIn, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull Entity entityIn) {
        if (entityIn instanceof EntityPlayerMP && entityIn.dimension == InitDimensions.ancient_world_dim_id) {
            if (!AncientLayer1EventsHandler.SERVER_MANAGER.playerElope((EntityPlayerMP) entityIn)) {
                AncientPortalsProcessor.teleportToOverworld((EntityPlayerMP) entityIn);
            }
        }
    }
}
