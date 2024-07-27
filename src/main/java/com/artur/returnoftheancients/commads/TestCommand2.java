package com.artur.returnoftheancients.commads;

import com.artur.returnoftheancients.ancientworldgeneration.main.AncientWorld;
import com.artur.returnoftheancients.generation.generators.AncientLabyrinthGenerator;
import com.artur.returnoftheancients.handlers.FreeTeleporter;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class TestCommand2 extends CommandBase {
    String NAME = "testden", USAGE = "/testden";

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
        AncientWorld.tpToAncientWorld(getCommandSenderAsPlayer(sender));
    }
}