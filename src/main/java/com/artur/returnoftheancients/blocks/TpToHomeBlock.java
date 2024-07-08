package com.artur.returnoftheancients.blocks;

import com.artur.returnoftheancients.handlers.ServerEventsHandler;
import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.misc.WorldData;
import com.artur.returnoftheancients.utils.interfaces.IALGS;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TpToHomeBlock extends BaseBlock {
    protected static final AxisAlignedBB HOME_PORTAL_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D);
    public TpToHomeBlock(String name, Material material, float hardness, float resistance, SoundType soundType) {
        super(name, material, hardness, resistance, soundType);
//        this.setCreativeTab(MainR.ReturnOfTheAncientsTab);
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
                ServerEventsHandler.tpToHome((EntityPlayerMP) player);
                player.move(MoverType.PLAYER, TRAConfigs.PortalSettings.x + 3, 3, TRAConfigs.PortalSettings.z + 3);
                WorldData worldData = WorldData.get();
                worldData.saveData.setBoolean(IALGS.isAncientWorldGenerateKey, false);
                worldData.markDirty();
            }
        }
    }
}
