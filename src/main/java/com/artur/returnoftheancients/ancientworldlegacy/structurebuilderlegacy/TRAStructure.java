package com.artur.returnoftheancients.ancientworldlegacy.structurebuilderlegacy;

import com.artur.returnoftheancients.ancientworldlegacy.structurebuilderlegacy.util.ITRAStructure;
import com.artur.returnoftheancients.ancientworldlegacy.structurebuilderlegacy.util.ITRAStructureTask;
import com.artur.returnoftheancients.blockprotect.BlockProtectHandler;
import com.artur.returnoftheancients.referense.Referense;
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

public class TRAStructure implements ITRAStructure {
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
            if (state != air || structureName.equals("air_cube") || structureName.equals("ancient_exit")) {
                rawBlocks.add(new BlockInfo(pos.getIntAt(0), pos.getIntAt(1), pos.getIntAt(2), blocksNames[state]));
            }
        }
        blocks = new ArrayDeque<>();
        blocks.addAll(rawBlocks);
    }

    @Override
    public void gen(World world, int x, int y, int z) {
        for (BlockInfo block : blocks) {
            world.setBlockState(mutablePos.setPos(block.x + x, block.y + y, block.z + z), block.state);
        }
    }

    @Override
    public void protect(World world, int x, int y, int z) {
        for (BlockInfo block : blocks) {
            if (block.state.getBlock() != Blocks.AIR) {
                BlockProtectHandler.protect(world, mutablePos.setPos(block.x + x, block.y + y, block.z + z));
            }
        }
    }

    @Override
    public void addDisposableTask(ITRAStructureTask task) {

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
