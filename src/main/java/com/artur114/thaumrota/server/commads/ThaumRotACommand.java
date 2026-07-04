package com.artur114.thaumrota.server.commads;

import com.artur114.bananalib.mc.math.m3d.vec.PosMc3IM;
import com.artur114.thaumrota.common.generation.portallegacy.base.AncientPortalsProcessor;
import com.artur114.thaumrota.common.worldstate.ancientworld.system.base.AncientLayer1;
import com.artur114.thaumrota.common.worldstate.ancientworld.system.base.AncientLayer1StaticManager;
import com.artur114.thaumrota.main.ThaumRotA;
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
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.Collections;
import java.util.List;

public class ThaumRotACommand extends CommandBase {

    String NAME = ThaumRotA.MODID, USAGE = "thaumrota.command.main.usage";


    @Override
    public @NotNull String getName() {
        return NAME;
    }

    @Override
    public @NotNull String getUsage(@NotNull ICommandSender sender) {
        return USAGE;
    }

    @Override
    public void execute(@NotNull MinecraftServer server, @NotNull ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP player = getCommandSenderAsPlayer(sender);
        if (args.length == 0) {
            player.sendMessage(new TextComponentString(TextFormatting.RED + "Usage: " + USAGE));
            return;
        }
        switch (args[0]) {
            case "seed":
                AncientLayer1 sector = AncientLayer1StaticManager.sectorForPlayer(player);
                if (sector == null) {
                    player.sendMessage(new TextComponentString("you don't belong to any sector").setStyle(new Style().setColor(TextFormatting.RED)));
                } else {
                    player.sendMessage(new TextComponentString("seed " + sector.seed() + " copped to clipboard").setStyle(new Style().setColor(TextFormatting.GREEN)));
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(sector.seed() + ""), null);
                }
                break;
            case "tptoportal":
                PosMc3IM pos;
                if (args.length >= 2) {
                    pos = new PosMc3IM().setChunk(AncientPortalsProcessor.getPortalPos(player.world, parseInt(args[1])));
                } else {
                    pos = new PosMc3IM(player.getPosition());
                    pos.setChunk(AncientPortalsProcessor.getNearestPortalPos(player.world, pos));
                }
                player.sendMessage(new TextComponentString("Teleport complete!").setStyle(new Style().setColor(TextFormatting.GREEN)));
                player.connection.setPlayerLocation(pos.getX(), 100, pos.getZ(), player.rotationYaw, player.rotationPitch);
                break;
            default:
                player.sendMessage(new TextComponentString(TextFormatting.RED + "Usage: " + I18n.format(USAGE)));
                break;
        }
    }

    public @NotNull List<String> getTabCompletions(@NotNull MinecraftServer server, @NotNull ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, "tptoportal", "seed", "help") : Collections.emptyList();
    }
}
