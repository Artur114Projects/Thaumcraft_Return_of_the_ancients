package com.artur.returnoftheancients.ancientworldgeneration.structurebuilder;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class TRAStructureEBS extends TRAStructure {
    public TRAStructureEBS(String structureName) {
        super(structureName);
    }

    @Override
    public void gen(World world, int x, int y, int z) {
        for (BlockInfo block : blocks) {
            int chunkX = (block.x + x) >> 4;
            int chunkZ = (block.z + z) >> 4;
            int storageIndex = (block.y + y) >> 4;
            Chunk chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);

            // Проверка границ массива
            if (storageIndex < 0 || storageIndex >= chunk.getBlockStorageArray().length) {
                continue;
            }

            ExtendedBlockStorage storage = chunk.getBlockStorageArray()[storageIndex];
            if (storage == null) {
                storage = new ExtendedBlockStorage(storageIndex << 4, false);
                chunk.getBlockStorageArray()[storageIndex] = storage;
            }

            // Проверка границ блока в ExtendedBlockStorage
            int localX = block.x & 15;
            int localY = block.y & 15;
            int localZ = block.z & 15;

            storage.set(localX, localY, localZ, block.state);
        }
    }
}
