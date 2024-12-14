package com.artur.returnoftheancients.ancientworldgeneration.structurebuilder;

import com.artur.returnoftheancients.ancientworldgeneration.structurebuilder.util.ITRAStructureIsUseEBS;
import com.artur.returnoftheancients.ancientworldgeneration.structurebuilder.util.ITRAStructureTask;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import java.util.HashSet;
import java.util.Set;

public class TRAStructureEBS extends TRAStructure {
    public TRAStructureEBS(String structureName, ITRAStructureIsUseEBS isUseEBS) {
        super(structureName);
        this.isUseEBS = isUseEBS == null ? isUseEBSDefault : isUseEBS;
    }
    protected ITRAStructureTask task = null;
    protected ITRAStructureIsUseEBS isUseEBS;
    protected ITRAStructureIsUseEBS isUseEBSDefault = (x, y, z, state) -> true;

    @Override
    public void gen(World world, int x, int y, int z) {
        Set<Chunk> chunkSet = new HashSet<>();
        for (BlockInfo block : blocks) {
            if (isUseEBS.isUseEBS(block.x, block.y, block.z, block.state)) {
                int chunkX = (block.x + x) >> 4;
                int chunkZ = (block.z + z) >> 4;
                int storageIndex = (block.y + y) >> 4;
                Chunk chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);

                IBlockState state = block.state;
                if (task != null) {
                    state = task.run(state);
                }

                // Проверка границ массива
                if (storageIndex < 0 || storageIndex >= chunk.getBlockStorageArray().length) {
                    continue;
                }

                ExtendedBlockStorage storage = chunk.getBlockStorageArray()[storageIndex];
                if (storage == null) {
                    storage = new ExtendedBlockStorage((block.y + y), false);
                    chunk.getBlockStorageArray()[storageIndex] = storage;
                }

                int localX = block.x >= 16 ? (block.x >> 4) : block.x;
                int localY = block.y >= 16 ? (block.y >> 4) : block.y;
                int localZ = block.z >= 16 ? (block.z >> 4) : block.z;

                storage.set(localX, localY, localZ, state);
                chunkSet.add(chunk);
            } else {
                world.setBlockState(mutablePos.setPos((block.x + x), (block.y + y), (block.z + z)), block.state);
            }
        }
        for (Chunk chunk : chunkSet) {
            chunk.markDirty();
        }
        task = null;
    }

    @Override
    public void addDisposableTask(ITRAStructureTask task) {
        this.task = task;
    }
}
