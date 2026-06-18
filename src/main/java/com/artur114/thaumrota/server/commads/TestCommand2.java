package com.artur114.thaumrota.server.commads;

import com.artur114.thaumrota.common.util.TeleportHandler;
import com.artur114.thaumrota.common.init.InitDimensions;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class TestCommand2 extends CommandBase {
    String NAME = "tptoancientworld", USAGE = "/tptoancientworld";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return USAGE;
    }


    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP player = getCommandSenderAsPlayer(sender);

        if (player.dimension != InitDimensions.ANCIENT_WORLD_ID) {
            TeleportHandler.teleportToDimension(player, InitDimensions.ANCIENT_WORLD_ID, new BlockPos(0, 61, 0));
            server.getWorld(InitDimensions.ANCIENT_WORLD_ID).setBlockState(new BlockPos(0, 60, 0), Blocks.STONE.getDefaultState());
        } else {
            TeleportHandler.teleportToDimension(player, player.getSpawnDimension(), this.getTeleportPos(player));
        }
    }

    private BlockPos getTeleportPos(EntityPlayerMP player) {
        BlockPos pos = player.getBedLocation(player.getSpawnDimension());
        if (pos == null) pos = player.world.getPrecipitationHeight(new BlockPos(8, 0, 8));
        return pos;
    }
}