package com.artur.returnoftheancients.commads;

import com.artur.returnoftheancients.misc.WorldData;
import com.artur.returnoftheancients.referense.Referense;
import com.artur.returnoftheancients.utils.interfaces.IALGS;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.jetbrains.annotations.NotNull;

public class TRACommand extends CommandBase {

    String NAME = Referense.MODID, USAGE = "/" + Referense.MODID + " updatedropprimalblade";


    @Override
    public @NotNull String getName() {
        return NAME;
    }

    @Override
    public @NotNull String getUsage(@NotNull ICommandSender sender) {
        return USAGE;
    }

    @Override
    public void execute(@NotNull MinecraftServer server, @NotNull ICommandSender sender, String @NotNull [] args) throws CommandException {
        EntityPlayer player = getCommandSenderAsPlayer(sender);
        if (args.length == 0) {
            player.sendMessage(new TextComponentString(TextFormatting.RED + "Usage: " + USAGE));
            return;
        }
        switch (args[0]) {
            case "help":
                player.sendMessage(new TextComponentString(TextFormatting.RED + "Usage: " + USAGE));
                break;
            case "updatedropprimalblade":
                WorldData.get().saveData.setBoolean(IALGS.isPrimalBladeDropKey, false);
                break;
            default:
                player.sendMessage(new TextComponentString(TextFormatting.RED + "Usage: " + USAGE));
                break;
        }
    }

}
