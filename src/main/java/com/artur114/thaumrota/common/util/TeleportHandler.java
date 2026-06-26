package com.artur114.thaumrota.common.util;


import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.jetbrains.annotations.NotNull;

public class TeleportHandler extends Teleporter {
    public boolean makePortal(@NotNull Entity p_85188_1_) {
        return true;
    }

    @Override
    public void placeInPortal(@NotNull Entity player, float rotationYaw) {
        int x = MathHelper.floor(player.posX);
        int y = MathHelper.floor(player.posY) - 1;
        int z = MathHelper.floor(player.posZ);
        player.setLocationAndAngles(x, y, z, rotationYaw, player.rotationPitch);
        player.motionX = player.motionY = player.motionZ = 0.0D;
    }

    public TeleportHandler(WorldServer world) {
        super(world);
    }

    public static void teleportToDimension(EntityPlayer player, int dimension, BlockPos pos) {
        teleportToDimension(player, dimension, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
    }

    public static void teleportToSpawnPoint(EntityPlayer playerRaw) {
        if (playerRaw instanceof EntityPlayerMP && ((EntityPlayerMP) playerRaw).mcServer != null) {
            EntityPlayerMP player = (EntityPlayerMP) playerRaw;
            MinecraftServer server = player.mcServer;
            int dimension = player.dimension;
            World world = server.getWorld(dimension);
            if (!world.provider.canRespawnHere()) {
                dimension = world.provider.getRespawnDimension(player);
            }
            boolean flag = player.isSpawnForced(dimension);
            BlockPos bedRaw = player.getBedLocation(dimension);
            BlockPos spawnPos = null;

            if (bedRaw != null) {
                spawnPos = EntityPlayer.getBedSpawnLocation(server.getWorld(dimension), bedRaw, flag);
            }

            if (spawnPos == null) {
                spawnPos = server.getWorld(dimension).provider.getRandomizedSpawnPoint();
            }

            teleportToDimension(player, dimension, spawnPos);
        }
    }

    public static void teleportToDimension(EntityPlayer player, int dimension, double x, double y, double z) {
        if (dimension == player.dimension) {
            player.setPositionAndUpdate(x, y, z);
            return;
        }
        int oldDimension = player.world.provider.getDimension();
        EntityPlayerMP entityPlayerMP = (EntityPlayerMP) player;
        MinecraftServer server = player.world.getMinecraftServer();
        if (server == null) throw new IllegalStateException();
        WorldServer worldServer = server.getWorld(dimension);

        if (worldServer.getMinecraftServer() == null) {
            throw new IllegalArgumentException("Dimension: " + dimension + " doesn't exist!");
        }

        worldServer.getMinecraftServer().getPlayerList().transferPlayerToDimension(entityPlayerMP, dimension, new TeleportHandler(worldServer));
        player.setPositionAndUpdate(x, y, z);
        if (oldDimension == 1) {
            player.setPositionAndUpdate(x, y, z);
            worldServer.spawnEntity(player);
            worldServer.updateEntityWithOptionalForce(player, false);
        }
    }
}