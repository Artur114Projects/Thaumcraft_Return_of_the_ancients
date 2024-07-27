package com.artur.returnoftheancients.ancientworldgeneration.structurebuilder;

import com.artur.returnoftheancients.referense.Referense;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class TRAStructure {
    protected final BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

    protected final ArrayDeque<BlockInfo> blocks;

    public TRAStructure(String structureName) {
        NBTTagCompound structureNBT = readStructureAsName(structureName);
        NBTTagList palette = structureNBT.getTagList("palette", 10);
        NBTTagList blocksNBT = structureNBT.getTagList("blocks", 10);

        int air = -1;
        IBlockState[] blocksNames = new IBlockState[palette.tagCount()];
        for (int i = 0; i != palette.tagCount(); i++) {
            blocksNames[i] = getBlockByString(palette.getCompoundTagAt(i).getString("Name")).getDefaultState();
            if (getBlockByString(palette.getCompoundTagAt(i).getString("Name")) == Blocks.AIR) {
                air = i;
            }
        }

        ArrayList<BlockInfo> rawBlocks = new ArrayList<>();
        for (int i = 0; i != blocksNBT.tagCount(); i++) {
            NBTTagCompound compound = blocksNBT.getCompoundTagAt(i);
            NBTTagList pos = compound.getTagList("pos", 3);
            int state = compound.getInteger("state");
            if (state != air || structureName.equals("air_cube")) {
                rawBlocks.add(new BlockInfo(pos.getIntAt(0), pos.getIntAt(1), pos.getIntAt(2), blocksNames[state]));
            }
        }
        blocks = new ArrayDeque<>();
        blocks.addAll(rawBlocks);
    }

    private static NBTTagCompound readStructureAsName(String structureName) {
        InputStream inputstream = MinecraftServer.class.getResourceAsStream("/assets/" + Referense.MODID + "/structures/" + structureName + ".nbt");
        try {
            assert inputstream != null;
            return CompressedStreamTools.readCompressed(inputstream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(inputstream);
        }
    }

    public void please(World world, int x, int y, int z) {
        world.getChunkFromBlockCoords(mutablePos.setPos(x, y, z)).onLoad();
        for (BlockInfo block : blocks) {
            world.setBlockState(mutablePos.setPos(block.x + x, block.y + y, block.z + z), block.state);
        }
    }

    protected Block getBlockByString(String block) {
        ResourceLocation resourcelocation = new ResourceLocation(block);
        if (!Block.REGISTRY.containsKey(resourcelocation)) {
            throw new RuntimeException(block);
        } else {
            return Block.REGISTRY.getObject(resourcelocation);
        }
    }

    protected static class BlockInfo {
        public final int x;
        public final int y;
        public final int z;
        public final IBlockState state;

        public BlockInfo(int x, int y, int z, IBlockState state) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.state = state;
        }
    }
}
