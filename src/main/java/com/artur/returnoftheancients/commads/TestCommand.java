package com.artur.returnoftheancients.commads;

import com.artur.returnoftheancients.handlers.FreeTeleporter;
import com.artur.returnoftheancients.init.InitDimensions;
import com.artur.returnoftheancients.sounds.ModSounds;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.concurrent.TimeUnit;

public class TestCommand extends CommandBase {
    String NAME = "testcommand", USAGE = "/testcommand";

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
        FreeTeleporter.teleportToDimension(player, InitDimensions.ancient_world_dim_id, 8, 100, 8);
        System.out.println(player.dimension);
        BlockPos blockPos = new BlockPos(player.posX - 4, player.posY - 1, player.posZ - 4);
        set(player.world, blockPos, Blocks.STONE.getDefaultState(), 8, 8, 1);
    }


    /**
     * Коробка с полом и потолком.
     */
    private void set(World world, BlockPos position, IBlockState stateMaterial, int width, int lenght, int height) {
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                for(int z = 0; z < lenght; z++) {
                    //То же самое, что в предыдущем примере,
                    //но добавляем проверку по оси y
                    if(!(x > 0 && x < width-1) || !(z > 0 && z < lenght-1) || !(y > 0 && y < height-1)) {
                        world.setBlockState(position.add(x, y, z), stateMaterial);
                    }
                }
            }
        }
    }
}