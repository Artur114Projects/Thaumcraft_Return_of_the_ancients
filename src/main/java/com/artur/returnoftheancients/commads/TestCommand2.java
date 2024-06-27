package com.artur.returnoftheancients.commads;

import com.artur.returnoftheancients.handlers.FreeTeleporter;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class TestCommand2 extends CommandBase {
    String NAME = "testcommand2", USAGE = "/testcommand2";

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
        EntityPlayer player = getCommandSenderAsPlayer(sender);
        FreeTeleporter.teleportToDimension(player, 0, 0, 100, 0);
        System.out.println(player.dimension);
    }
}