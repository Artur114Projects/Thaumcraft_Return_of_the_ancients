package com.artur.returnoftheancients.commads;

import com.artur.returnoftheancients.ancientworldutilities.GenPortal;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;



public class GenAncientLabyrinth extends CommandBase {
    String NAME = "portal", USAGE = "/portal";

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
        if (args.length == 3) {
            GenPortal.start(0, Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        } else {
            EntityPlayer player = getCommandSenderAsPlayer(sender);
            player.motionX = 0;
            player.motionZ = 0;
            GenPortal.start(0, player.getPosition());
        }
    }
}
