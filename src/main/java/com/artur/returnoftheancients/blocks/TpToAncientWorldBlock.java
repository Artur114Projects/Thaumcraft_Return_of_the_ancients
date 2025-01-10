package com.artur.returnoftheancients.blocks;

import com.artur.returnoftheancients.ancientworldgeneration.main.AncientWorld;
import com.artur.returnoftheancients.generation.generators.portal.AncientPortalNaturalGeneration;
import com.artur.returnoftheancients.generation.generators.portal.base.AncientPortal;
import com.artur.returnoftheancients.generation.generators.portal.base.AncientPortalsProcessor;
import com.artur.returnoftheancients.handlers.ServerEventsHandler;
import com.artur.returnoftheancients.main.MainR;
import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.handlers.HandlerR;
import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TpToAncientWorldBlock extends BaseBlock{

    public static final String noCollisionNBT = "noCollisionNBT";
    protected static final AxisAlignedBB HOME_PORTAL_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D);
    public TpToAncientWorldBlock(String name, Material material, float hardness, float resistance, SoundType soundType) {
        super(name, material, hardness, resistance, soundType);
        this.setCreativeTab(MainR.ReturnOfTheAncientsTab);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(TextFormatting.RED + I18n.format("tile.tp_to_ancient_world_block.info"));
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
                ArrayList<String> ID = HandlerR.isPlayerUseUnresolvedItems(player);
                if ((ID.isEmpty() || !TRAConfigs.PortalSettings.checkItems) && (ServerEventsHandler.getDifficultyId() != 0 || !TRAConfigs.AncientWorldSettings.noPeaceful)) {
                    player.fallDistance = 0;
                    AncientPortalsProcessor.onPlayerCollidePortal(player);
                } else {
                    if (!ID.isEmpty()) {
                        HandlerR.sendMessageTranslate(player, Referense.MODID + ".portal.message");
                        player.sendMessage(new TextComponentString(ID.toString()));
                        ID.clear();
                    } else {
                        player.sendMessage(new TextComponentString(TextFormatting.DARK_PURPLE + "TC RETURN OF THE ANCIENTS: " + TextFormatting.RESET + "PEACEFUL DIFFICULTY ???"));
                    }
                    AncientPortal portal = AncientPortalsProcessor.getPortalOnPos(pos);
                    if (portal != null) {
                        portal.tpToHome(player, false);
                    }
                }
            }
        }
    }
}
