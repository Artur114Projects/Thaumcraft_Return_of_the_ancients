package com.artur.returnoftheancients.blockprotect.server;

import com.artur.returnoftheancients.blockprotect.IExtendedProtectStorage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class ServerProtectedChunkTest {
    @Test
    public void testProtectUnprotect() {
        ServerProtectedChunk chunk = new ServerProtectedChunk(new ChunkPos(0, 0), 0);
        BlockPos[] randPoss = randomPoss(new Random(), 16 * 256);

        Assertions.assertTrue(chunk.isEmpty());

        this.fillChunk(chunk, randPoss);

        Assertions.assertFalse(chunk.isEmpty());

        this.checkProtect(chunk, randPoss);

        this.checkNotProtect(chunk, randPoss);

        this.unFillChunk(chunk, randPoss);

        Assertions.assertTrue(chunk.isEmpty());
    }

    @Test
    public void testSaveToNBT() {
        ServerProtectedChunk chunk = new ServerProtectedChunk(new ChunkPos(0, 0), 0);
        BlockPos[] randPoss = randomPoss(new Random(), 16 * 256);

        Assertions.assertTrue(chunk.isEmpty());

        this.fillChunk(chunk, randPoss);

        Assertions.assertFalse(chunk.isEmpty());

        NBTTagCompound list = chunk.serializeNBT();

        ServerProtectedChunk chunkFromNBT = new ServerProtectedChunk(new ChunkPos(0, 0), 0);
        chunkFromNBT.deserializeNBT(list);

        Assertions.assertFalse(chunk.isEmpty());

        this.checkProtect(chunk, randPoss);

        this.checkNotProtect(chunk, randPoss);
    }

    private BlockPos[] randomPoss(Random rand, int count) {
        BlockPos[] randPos = new BlockPos[count];

        for (int i = 0; i != randPos.length; i++) {
            randPos[i] = new BlockPos(rand.nextInt(16), rand.nextInt(256), rand.nextInt(16));
        }

        return randPos;
    }

    private void fillChunk(IServerProtectedChunk chunk, BlockPos[] poss) {
        for (BlockPos pos : poss) {
            chunk.protect(pos);
        }
    }

    private void unFillChunk(IServerProtectedChunk chunk, BlockPos[] poss) {
        for (BlockPos pos : poss) {
            chunk.unProtect(pos);
        }
    }

    private void checkProtect(IServerProtectedChunk chunk, BlockPos[] poss) {
        for (BlockPos pos : poss) {
            Assertions.assertTrue(chunk.hasProtect(pos));
        }
    }


    private void checkNotProtect(IServerProtectedChunk chunk, BlockPos[] hasProtect) {
        for (int x = 0; x != 16; x++) {
            for (int z = 0; z != 16; z++) {
                for (int y = 0; y != 256; y++) {
                    boolean flag = true;
                    for (BlockPos pos : hasProtect) {
                        if (pos.getX() == x && pos.getY() == y && pos.getZ() == z) {
                            flag = false;
                            break;
                        }
                    }
                    if (!flag) {
                        continue;
                    }
                    Assertions.assertFalse(chunk.hasProtect(x, y, z));
                }
            }
        }
    }
}
