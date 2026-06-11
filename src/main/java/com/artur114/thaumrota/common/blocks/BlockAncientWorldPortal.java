package com.artur114.thaumrota.common.blocks;

import com.artur114.thaumrota.common.generation.portal.base.AncientPortal;
import com.artur114.thaumrota.common.generation.portal.base.AncientPortalsProcessor;
import com.artur114.thaumrota.common.event.ServerEventsHandler;
import com.artur114.thaumrota.common.misc.RotAConfigs;
import com.artur114.thaumrota.common.handlers.MiscHandler;
import com.artur114.thaumrota.main.ThaumRotA;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlockAncientWorldPortal extends BaseBlock {

    public static final String noCollisionNBT = "noCollisionNBT";
    protected static final AxisAlignedBB HOME_PORTAL_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D);
    public BlockAncientWorldPortal(String name, Material material, float hardness, float resistance, SoundType soundType) {
        super(name, material, hardness, resistance, soundType);

        this.setCreativeTab(ThaumRotA.CREATIVE_TAB);
        this.setForCreative();
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
        if (!entityIn.getEntityData().getBoolean(noCollisionNBT)) {
            if (entityIn instanceof EntityPlayerMP) {
                EntityPlayerMP player = (EntityPlayerMP) entityIn;
                List<String> ID = MiscHandler.isPlayerUseUnresolvedItems(player);
                if ((ID.isEmpty() || !RotAConfigs.PortalSettings.checkItems) && (ServerEventsHandler.getDifficultyId() != 0 || !RotAConfigs.AncientWorldSettings.noPeaceful)) {
                    player.fallDistance = 0;
                    AncientPortalsProcessor.onPlayerCollidePortal(player);
                } else {
                    if (!ID.isEmpty()) {
                        MiscHandler.sendMessageTranslate(player, ThaumRotA.MODID + ".portal.message");
                        player.sendMessage(new TextComponentString(ID.toString()));
                        ID.clear();
                    } else {
                        player.sendMessage(new TextComponentString(TextFormatting.DARK_PURPLE + "TC RETURN OF THE ANCIENTS: " + TextFormatting.RESET + "PEACEFUL DIFFICULTY ???"));
                    }
                    AncientPortal portal = AncientPortalsProcessor.getPortalOnPos(pos);
                    if (portal != null) {
                        portal.teleportToOverworld(player, false);
                    }
                }
            }
        }
    }
}
