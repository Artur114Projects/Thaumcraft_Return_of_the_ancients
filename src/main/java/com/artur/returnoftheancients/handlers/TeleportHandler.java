package com.artur.returnoftheancients.handlers;


import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import org.jetbrains.annotations.NotNull;

public class TeleportHandler extends Teleporter
{
    public boolean makePortal(@NotNull Entity p_85188_1_)
    {
        return true;
    }

    @Override
    public void placeInPortal(@NotNull Entity player, float rotationYaw)
    {
        int x = MathHelper.floor(player.posX);
        int y = MathHelper.floor(player.posY) - 1;
        int z = MathHelper.floor(player.posZ);
        player.setLocationAndAngles((double) x, (double) y, (double) z, player.rotationYaw, 0.0F);
        player.motionX = player.motionY = player.motionZ = 0.0D;
    }

    public TeleportHandler(WorldServer world) {
        super(world);
    }

    public static void teleportToDimension(EntityPlayer player, int dimension, BlockPos pos) {
        teleportToDimension(player, dimension, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
    }

    public static void teleportToDimension(EntityPlayer player, int dimension, double x, double y, double z) {
        int oldDimension = player.world.provider.getDimension();
        EntityPlayerMP entityPlayerMP = (EntityPlayerMP) player;
        MinecraftServer server = ((EntityPlayerMP) player).world.getMinecraftServer();
        WorldServer worldServer = server.getWorld(dimension);
        player.addExperienceLevel(0);

        if (worldServer == null) {
            throw new IllegalArgumentException("Dimension: " + dimension + " doesn't exist!");
        }

        worldServer.getMinecraftServer().getPlayerList().transferPlayerToDimension(entityPlayerMP, dimension, new TeleportHandler(worldServer));
        player.setPositionAndUpdate(x, y, z);
        if (oldDimension == 1) {
            // For some reason teleporting out of the end does weird things. Compensate for that
            player.setPositionAndUpdate(x, y, z);
            worldServer.spawnEntity(player);
            worldServer.updateEntityWithOptionalForce(player, false);
        }
    }
}