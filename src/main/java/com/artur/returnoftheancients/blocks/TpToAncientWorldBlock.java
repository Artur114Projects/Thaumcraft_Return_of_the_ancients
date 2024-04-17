package com.artur.returnoftheancients.blocks;

import com.artur.returnoftheancients.ancientworldutilities.Configs;
import com.artur.returnoftheancients.ancientworldutilities.LoadingGui;
import com.artur.returnoftheancients.ancientworldutilities.OldLoadingGui;
import com.artur.returnoftheancients.generation.generators.AncientLabyrinthGenerator;
import com.artur.returnoftheancients.handlers.EventsHandler;
import com.artur.returnoftheancients.handlers.FreeTeleporter;
import com.artur.returnoftheancients.handlers.Handler;
import com.artur.returnoftheancients.main.Main;
import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static com.artur.returnoftheancients.init.InitDimensions.ancient_world_dim_id;

public class TpToAncientWorldBlock extends BaseBlock{

    public static boolean noCollision = false;
    protected static final AxisAlignedBB HOME_PORTAL_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D);
    public TpToAncientWorldBlock(String name, Material material, float hardness, float resistance, SoundType soundType) {
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
        if (!noCollision) {
            if (entityIn instanceof EntityPlayerMP) {
                EntityPlayer player = (EntityPlayer) entityIn;
                ArrayList<String> ID = Handler.isPlayerUseUnresolvedItems(player);
                if (ID.isEmpty() && (EventsHandler.getDifficultyId() != 0 || !Configs.AncientWorldSettings.noPeaceful)) {
                    player.fallDistance = 0;
                    if (!Configs.Any.useOldLoadingGui) {
                        FMLCommonHandler.instance().showGuiScreen(new LoadingGui());
                    } else {
                        FMLCommonHandler.instance().showGuiScreen(new OldLoadingGui());
                    }
                    AncientLabyrinthGenerator.genAncientLabyrinth(player);
                    Minecraft.getMinecraft().displayGuiScreen(null);
                    if (player.isCreative()) {
                        FreeTeleporter.teleportToDimension(player, ancient_world_dim_id, 8, 125, -10);
                    } else {
                        FreeTeleporter.teleportToDimension(player, ancient_world_dim_id, 8, 253, 8);
                    }
                } else {
                    if (!ID.isEmpty()) {
                        player.sendMessage(new TextComponentTranslation(Referense.MODID + ".portal.message"));
                        player.sendMessage(new TextComponentString(ID.toString()));
                        ID.clear();
                    } else {
                        player.sendMessage(new TextComponentString("PEACEFUL DIFFICULTY ???"));
                    }
                    EventsHandler.tpToHome = true;
                    noCollision = true;
                    if (Minecraft.getMinecraft().player != null)
                        Minecraft.getMinecraft().player.motionY = 1;
                }
            }
        }
    }
}
