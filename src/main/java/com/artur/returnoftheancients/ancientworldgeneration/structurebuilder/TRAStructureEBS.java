package com.artur.returnoftheancients.ancientworldgeneration.structurebuilder;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class TRAStructureEBS extends TRAStructure{
    private final String name;
    public TRAStructureEBS(String structureName) {
        super(structureName);
        name = structureName;
    }

    @Override
    public void please(World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        ExtendedBlockStorage ebs = world.getChunkFromBlockCoords(pos).getBlockStorageArray()[pos.getY() >> 4];
        if (!(ebs == Chunk.NULL_BLOCK_STORAGE && name.equals("air_cube"))) {
            for (BlockInfo block : blocks) {
                ebs.set((pos.getX() + block.x) & 15, (pos.getY() + block.y) & 15, (pos.getZ() + block.z) & 15, block.state);
            }
        }
    }
}
