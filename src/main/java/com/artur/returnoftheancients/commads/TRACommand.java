package com.artur.returnoftheancients.commads;

import com.artur.returnoftheancients.ancientworldgeneration.main.AncientWorld;
import com.artur.returnoftheancients.misc.WorldData;
import com.artur.returnoftheancients.misc.WorldDataFields;
import com.artur.returnoftheancients.referense.Referense;
import com.artur.returnoftheancients.utils.interfaces.IALGS;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class TRACommand extends CommandBase {

    String NAME = Referense.MODID, USAGE = "/" + Referense.MODID + " updatedropprimalblade" + ", seed" + ", help";


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
        EntityPlayerMP player = getCommandSenderAsPlayer(sender);
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
                WorldData.get().markDirty();
                WorldDataFields.reload();
                player.sendMessage(new TextComponentString("Drop is update!").setStyle(new Style().setColor(TextFormatting.GREEN)));
                break;
            case "seed":
                long seed = AncientWorld.getSeed(player);
                if (seed == 0) {
                    player.sendMessage(new TextComponentString("you don't belong to any labyrinth").setStyle(new Style().setColor(TextFormatting.RED)));
                } else {
                    player.sendMessage(new TextComponentString(seed + "").setStyle(new Style().setColor(TextFormatting.GREEN)));
                }
                break;
            default:
                player.sendMessage(new TextComponentString(TextFormatting.RED + "Usage: " + USAGE));
                break;
        }
    }

}
