package com.artur.returnoftheancients.generation.generators;

import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.referense.Referense;
import com.artur.returnoftheancients.utils.interfaces.IStructure;
import net.minecraft.block.state.IBlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.ArrayList;
import java.util.Random;

public class GenStructure implements IStructure {

    public static void generateStructure(World world, int x, int y, int z, String structureName)
    {
        if (TRAConfigs.Any.debugMode) {
            System.out.println("Gen Structure: " + structureName + " XYZ " + x + "," + y + "," + z);
        }
        BlockPos pos = new BlockPos(x, y, z);
        MinecraftServer mcServer = world.getMinecraftServer();
        TemplateManager manager = worldServer.getStructureTemplateManager();
        ResourceLocation location = new ResourceLocation(Referense.MODID, structureName);
        Template template = manager.get(mcServer, location);

        if(template != null)
        {
            IBlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);
            template.addBlocksToWorldChunk(world, pos, settings);
        }
    }
}

