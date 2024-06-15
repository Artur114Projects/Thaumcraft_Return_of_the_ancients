package com.artur.returnoftheancients.commads;

import com.artur.returnoftheancients.generation.generators.GenStructure;
import com.artur.returnoftheancients.init.InitDimensions;
import com.artur.returnoftheancients.utils.interfaces.IStructure;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Rotation;


public class Command  extends CommandBase implements IStructure {
    String NAME = "gen", USAGE = "/gen";

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
        if (args.length == 1) {
            for (byte x = 0; x != 4; x++) {
                int cx = 16 - 16 * x;
                int cz = 16;
                int dx = 0;
                int dz = 0;
                switch (x) {
                    case 0:
                        settings.setRotation(Rotation.NONE);
                        break;
                    case 1:
                        settings.setRotation(Rotation.CLOCKWISE_90);
                        dx = -15;
                        break;
                    case 2:
                        settings.setRotation(Rotation.COUNTERCLOCKWISE_90);
                        dz = -15;
                        break;
                    case 3:
                        settings.setRotation(Rotation.CLOCKWISE_180);
                        dz = -15;
                        dx = -15;
                        break;
                }
                cx = cx - dx;
                cz = cz - dz;
                GenStructure.generateStructure(server.getWorld(InitDimensions.ancient_world_dim_id), cx, 100, cz, args[0]);
                settings.setRotation(Rotation.NONE);
            }
        }
    }
}
