package com.artur.returnoftheancients.commads;

import com.artur.returnoftheancients.handlers.TeleportHandler;
import com.artur.returnoftheancients.init.InitDimensions;
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

        if (player.dimension != InitDimensions.ancient_world_dim_id) {
            TeleportHandler.teleportToDimension(player, InitDimensions.ancient_world_dim_id, new BlockPos(0, 61, 0));
            server.getWorld(InitDimensions.ancient_world_dim_id).setBlockState(new BlockPos(0, 60, 0), Blocks.STONE.getDefaultState());
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