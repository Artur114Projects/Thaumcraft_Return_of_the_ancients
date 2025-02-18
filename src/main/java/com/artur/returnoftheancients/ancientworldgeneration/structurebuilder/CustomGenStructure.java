package com.artur.returnoftheancients.ancientworldgeneration.structurebuilder;

import com.artur.returnoftheancients.ancientworldgeneration.structurebuilder.util.ITRAStructure;
import com.artur.returnoftheancients.ancientworldgeneration.structurebuilder.util.ITRAStructureIsUseEBS;
import com.artur.returnoftheancients.ancientworldgeneration.structurebuilder.util.ITRAStructureTask;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.*;

public class CustomGenStructure {
    private static final List<String> rawStructures = new ArrayList<>();
    private static final Map<String, ITRAStructure> structures = new HashMap<>();
    private static final List<String> useAirList = new ArrayList<>();
    private static boolean isUseEBS = false;
    private static boolean isUseBinary = false;
    private static boolean isUseAir = false;
    private static ITRAStructureTask task = null;
    private static ITRAStructureIsUseEBS isUseEBSTask = null;

    public static void put(String name) {
        if (isUseAir) useAirList.add(name);
        rawStructures.add(name);
    }

    public static void addTaskToFirstBuild(ITRAStructureTask task) {
        CustomGenStructure.task = task;
    }

    public static void setUseEBS(ITRAStructureIsUseEBS isUseEBSTask) {
        CustomGenStructure.isUseEBSTask = isUseEBSTask;
        isUseEBS = true;
    }

    public static void setUseBinary() {
        isUseBinary = true;
    }

    public static void setUseAir() {
        isUseAir = true;
    }

    public static void register() {
        if (isUseBinary) {
            for (String s : rawStructures) {
                boolean isAir = false;
                for (String a : useAirList) {
                    if (a.equals(s)) {
                        structures.put(s, new TRAStructureBinary(s, true));
                        isAir = true;
                        break;
                    }
                }
                if (isAir) continue;
                structures.put(s, new TRAStructureBinary(s, false));
            }
            postProsesRegister();
            return;
        }
        if (isUseEBS) {
            for (String s : rawStructures) {
                structures.put(s, new TRAStructureEBS(s, isUseEBSTask));
            }
            postProsesRegister();
            return;
        }

        for (String s : rawStructures) {
            structures.put(s, new TRAStructure(s));
        }
        postProsesRegister();
    }

    private static void postProsesRegister() {
        isUseAir = false;
        isUseEBS = false;
        isUseBinary = false;
        rawStructures.clear();
    }

    public static void gen(World world, int x, int y, int z, String name) {
        if (structures.containsKey(name)) {
            ITRAStructure structure = structures.get(name);
            structure.addDisposableTask(task);
            structure.gen(world, x, y, z);
            task = null;
        } else {
            throw new RuntimeException("invalid structure name: " + name);
        }
    }

    public static void gen(World world, BlockPos pos, String name) {
        if (structures.containsKey(name)) {
            structures.get(name).gen(world, pos.getX(), pos.getY(), pos.getZ());
        } else {
            throw new RuntimeException("invalid structure name: " + name);
        }
    }


    public static void registerOrGen(World world, int x, int y, int z, String name) {
        if (structures.containsKey(name)) {
            structures.get(name).gen(world, x, y, z);
        } else {
            TRAStructure structure = new TRAStructure(name);
            structures.put(name, structure);
            structure.gen(world, x, y, z);
        }
    }

    public static void clearChunk(World world, int x, int z) {
        Chunk chunk = world.getChunkFromChunkCoords(x, z);

        List<BlockPos> tileEntityPoss = new ArrayList<>(chunk.getTileEntityMap().keySet());
        for (BlockPos tileEntityPos : tileEntityPoss) {
            chunk.removeTileEntity(tileEntityPos);
        }

        for (byte y = 0; y != chunk.getBlockStorageArray().length; y++) {
            chunk.getBlockStorageArray()[y] = Chunk.NULL_BLOCK_STORAGE;
        }

        chunk.markDirty();
        world.markBlockRangeForRenderUpdate(chunk.getPos().getBlock(0, 0, 0), chunk.getPos().getBlock(15, 255, 15));
    }


    public static void delete(String name) {
        structures.remove(name);
    }
}
