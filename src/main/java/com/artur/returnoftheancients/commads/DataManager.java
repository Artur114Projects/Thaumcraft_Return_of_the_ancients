package com.artur.returnoftheancients.commads;

import com.artur.returnoftheancients.misc.WorldData;

import com.artur.returnoftheancients.util.interfaces.IALGS;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import org.jetbrains.annotations.NotNull;



public class DataManager extends CommandBase {
    String NAME = "data", USAGE = "/data";



    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return USAGE;
    }

    @Override
    public void execute(@NotNull MinecraftServer server, @NotNull ICommandSender sender, String @NotNull []args) throws CommandException {
        EntityPlayerMP playerMP = getCommandSenderAsPlayer(sender);
        WorldData worldData = WorldData.get();
        if (args[0].equals("set") && args.length == 4) {
            if (args[1].equals("setnodropprimalblade")) {
                WorldData.get().saveData.setBoolean(IALGS.isPrimalBladeDropKey, false);
                return;
            }
            boolean data = false;
            switch (args[3]) {
                case "0":
                    data = false;
                break;
                case "1":
                    data = true;
                break;
            }
            switch (args[1]) {
                case "boolean":
                    worldData.saveData.setBoolean(args[2], data);
                break;
            }
            worldData.markDirty();
            playerMP.sendMessage(new TextComponentString("Data save"));
        } else if (args[0].equals("get") && args.length == 3) {
            switch (args[1]) {
                case "int":
                    playerMP.sendMessage(new TextComponentString("Data load: " + worldData.saveData.getInteger(args[2])));
                break;
                case "boolean":
                    playerMP.sendMessage(new TextComponentString("Data load: " + worldData.saveData.getBoolean(args[2])));
                break;
                case "String":
                    playerMP.sendMessage(new TextComponentString("Data load: " + worldData.saveData.getString(args[2])));
                break;
            }
        } else if (args[0].equals("help") && args.length == 1){
            playerMP.sendMessage(new TextComponentString("/data set dataType [key] [value]"));
            playerMP.sendMessage(new TextComponentString("/data get dataType [key]"));
            playerMP.sendMessage(new TextComponentString("/data setnodropprimalblade"));
        } else {
            playerMP.sendMessage(new TextComponentString("/data set dataType [key] [value]"));
            playerMP.sendMessage(new TextComponentString("/data get dataType [key]"));
            playerMP.sendMessage(new TextComponentString("/data setnodropprimalblade"));
        }
    }
}
