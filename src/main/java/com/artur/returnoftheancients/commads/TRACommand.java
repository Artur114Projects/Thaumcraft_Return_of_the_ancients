package com.artur.returnoftheancients.commads;

import com.artur.returnoftheancients.ancientworldlegacy.main.AncientWorld;
import com.artur.returnoftheancients.generation.portal.base.AncientPortalsProcessor;
import com.artur.returnoftheancients.misc.WorldData;
import com.artur.returnoftheancients.misc.WorldDataFields;
import com.artur.returnoftheancients.referense.Referense;
import com.artur.returnoftheancients.util.interfaces.IALGS;
import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class TRACommand extends CommandBase {

    String NAME = Referense.MODID, USAGE = "returnoftheancients.command.main.usage";


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
            case "tptoportal":
                UltraMutableBlockPos pos;
                if (args.length >= 2) {
                    pos = new UltraMutableBlockPos(AncientPortalsProcessor.getPortalPos(player.world, Integer.parseInt(args[1])));
                } else {
                    pos = new UltraMutableBlockPos(player.getPosition());
                    pos.setPos(AncientPortalsProcessor.getNearestPortalPos(player.world, pos));
                }
                player.sendMessage(new TextComponentString("Teleport complete!").setStyle(new Style().setColor(TextFormatting.GREEN)));
                player.connection.setPlayerLocation(pos.getX(), 100, pos.getZ(), player.rotationYaw, player.rotationPitch);
                break;
            default:
                player.sendMessage(new TextComponentString(TextFormatting.RED + "Usage: " + I18n.format(USAGE)));
                break;
        }
    }

    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, "tptoportal", "seed", "help", "updatedropprimalblade") : Collections.emptyList();
    }
}
