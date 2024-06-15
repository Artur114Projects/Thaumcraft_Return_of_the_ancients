package com.artur.returnoftheancients.blocks;

import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.generation.generators.AncientLabyrinthGenerator;
import com.artur.returnoftheancients.handlers.EventsHandler;
import com.artur.returnoftheancients.handlers.HandlerR;
import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class TpToAncientWorldBlock extends BaseBlock{
    public static boolean noCollision = false;

    public static final String noCollisionNBT = "noCollisionNBT";
    protected static final AxisAlignedBB HOME_PORTAL_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D);
    public TpToAncientWorldBlock(String name, Material material, float hardness, float resistance, SoundType soundType) {
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
        if (!entityIn.getEntityData().getBoolean(noCollisionNBT)) {
            if (entityIn instanceof EntityPlayerMP) {
                EntityPlayer player = (EntityPlayer) entityIn;
                ArrayList<String> ID = HandlerR.isPlayerUseUnresolvedItems(player);
                if (ID.isEmpty() && (EventsHandler.getDifficultyId() != 0 || !TRAConfigs.AncientWorldSettings.noPeaceful)) {
                    player.fallDistance = 0;
                    AncientLabyrinthGenerator.tpToAncientWorld(player);
                } else {
                    if (!ID.isEmpty()) {
                        player.sendMessage(new TextComponentTranslation(Referense.MODID + ".portal.message"));
                        player.sendMessage(new TextComponentString(ID.toString()));
                        ID.clear();
                    } else {
                        player.sendMessage(new TextComponentString("PEACEFUL DIFFICULTY ???"));
                    }
                    player.getEntityData().setBoolean(EventsHandler.tpToHomeNBT, true);
                    player.getEntityData().setBoolean(noCollisionNBT, true);
                }
            }
        }
    }
}
