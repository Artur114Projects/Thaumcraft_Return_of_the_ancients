package com.artur.returnoftheancients.ancientworldgeneration.structurebuilder;

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
import java.util.ArrayDeque;
import java.util.ArrayList;

public class TRAStructureBinary implements ITRAStructure {
    protected final BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
    protected final int[] blocks;
    protected final IBlockState[] palette;
    protected ITRAStructureTask task = null;

    public TRAStructureBinary(String structureName, boolean isUseAir) {
        NBTTagCompound structureNBT = readStructureAsName(structureName);
        NBTTagList palette = structureNBT.getTagList("palette", 10);
        NBTTagList blocksNBT = structureNBT.getTagList("blocks", 10);

        int air = -1;
        this.palette = new IBlockState[palette.tagCount()];
        for (int i = 0; i != palette.tagCount(); i++) {
            this.palette[i] = getBlockByString(palette.getCompoundTagAt(i).getString("Name")).getDefaultState();
            if (getBlockByString(palette.getCompoundTagAt(i).getString("Name")) == Blocks.AIR && air == -1) {
                air = i;
            }
        }

        ArrayList<Integer> rawBlocks = new ArrayList<>();

        for (int i = 0; i != blocksNBT.tagCount(); i++) {
            NBTTagCompound compound = blocksNBT.getCompoundTagAt(i);
            NBTTagList pos = compound.getTagList("pos", 3);
            int state = compound.getInteger("state");
            if (state != air || isUseAir) {
                rawBlocks.add(packBytes((byte) pos.getIntAt(0), (byte) pos.getIntAt(1), (byte) pos.getIntAt(2), (byte) state));
            }
        }

        blocks = rawBlocks.stream().mapToInt(Integer::intValue).toArray();
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

    @Override
    public void gen(World world, int x, int y, int z) {
        for (int block : blocks) {
            byte xC = (byte) (block >> 24);
            byte yC = (byte) (block >> 16);
            byte zC = (byte) (block >> 8);
            byte structure = (byte) block;
            IBlockState state = palette[structure];
            if (task != null) {state = task.run(state);}
            world.setBlockState(mutablePos.setPos(xC + x, yC + y, zC + z), state);
        }
        task = null;
    }

    @Override
    public void addDisposableTask(ITRAStructureTask task) {
        this.task = task;
    }


    protected static int packBytes(byte b1, byte b2, byte b3, byte b4) {
        return (b1 << 24) | ((b2 & 0xFF) << 16) | ((b3 & 0xFF) << 8) | (b4 & 0xFF);
    }
}
