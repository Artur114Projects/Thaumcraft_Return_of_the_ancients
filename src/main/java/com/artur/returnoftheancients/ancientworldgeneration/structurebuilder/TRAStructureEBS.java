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
        BlockPos pos = new BlockPos(x, y, z);
        Chunk chunk = world.getChunkFromBlockCoords(pos);

        int selectionIndex = pos.getY() >> 4;
        int chunkX = chunk.getPos().getXStart();
        int chunkZ = chunk.getPos().getZStart();
        int selectionY = selectionIndex >> 4;

        ExtendedBlockStorage ebs = chunk.getBlockStorageArray()[selectionIndex];

        if (ebs == Chunk.NULL_BLOCK_STORAGE) {
            ebs = new ExtendedBlockStorage(pos.getY() >> 4, true);
        }
        for (BlockInfo block : blocks) {
            ebs.set((pos.getX() + block.x) & 15, (pos.getY() + block.y) & 15, (pos.getZ() + block.z) & 15, block.state);
        }

        chunk.markDirty();
        world.markBlockRangeForRenderUpdate(chunkX, selectionY, chunkZ, chunkX + 15, selectionY + 15, chunkZ + 15);
    }
}
