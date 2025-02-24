package com.artur.returnoftheancients.blockprotect;

import com.artur.returnoftheancients.blockprotect.server.IServerProtectedChunk;
import com.artur.returnoftheancients.capabilities.TRACapabilities;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import java.util.Arrays;

public class BlockProtectHandler {
    public static boolean hasProtect(World world, BlockPos pos) {
        Chunk chunk = world.getChunkFromBlockCoords(pos);

        if (chunk.isEmpty()) {
            return false;
        }

        IProtectedChunk protectedChunk = chunk.getCapability(TRACapabilities.PROTECTED_CHUNK, null);

        if (protectedChunk != null) {
            return protectedChunk.hasProtect(pos);
        }

        return false;
    }

    public static void setProtectState(World world, BlockPos pos, boolean state) {
        if (state) {
            protect(world, pos);
        } else {
            unProtect(world, pos);
        }
    }

    public static void protect(World world, BlockPos pos) {
        if (world.isRemote) {
            return;
        }

        Chunk chunk = world.getChunkFromBlockCoords(pos);
        IProtectedChunk protectedChunk = chunk.getCapability(TRACapabilities.PROTECTED_CHUNK, null);

        ((IServerProtectedChunk) protectedChunk).protect(pos);
    }

    public static void unProtect(World world, BlockPos pos) {
        if (world.isRemote) {
            return;
        }

        Chunk chunk = world.getChunkFromBlockCoords(pos);
        IProtectedChunk protectedChunk = chunk.getCapability(TRACapabilities.PROTECTED_CHUNK, null);

        ((IServerProtectedChunk) protectedChunk).unProtect(pos);
    }


    public static void protectAllBlocksOnChunk(World world, ChunkPos pos, Block... blocks) {
        Chunk chunk = world.getChunkFromChunkCoords(pos.x, pos.z);
        IProtectedChunk chunkP = chunk.getCapability(TRACapabilities.PROTECTED_CHUNK, null);

        if (!(chunkP instanceof IServerProtectedChunk)) {
            return;
        }

        for (int x = 0; x != 16; x++) {
            for (int z = 0; z != 16; z++) {
                for (int y = 0; y != 256; y++) {
                    ExtendedBlockStorage storage = chunk.getBlockStorageArray()[y >> 4];
                    Block block = storage.get(x & 15, y & 15, z & 15).getBlock();
                    if (Arrays.stream(blocks).anyMatch((b) -> b == block)) {
                        ((IServerProtectedChunk) chunkP).protect(x, y, z);
                    }
                }
            }
        }
    }
}
