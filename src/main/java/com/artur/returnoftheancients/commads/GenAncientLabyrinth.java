package com.artur.returnoftheancients.commads;

import com.artur.returnoftheancients.ancientworldgeneration.main.AncientWorld;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;



public class GenAncientLabyrinth extends CommandBase {
    String NAME = "please", USAGE = "/please";

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

    }
}
