package com.artur.returnoftheancients.blockprotect;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class ExtendedProtectStorageTest {
    @Test
    public void testHasSetProtect1Bit() {
        ExtendedProtectStorage1bit storage = new ExtendedProtectStorage1bit();
        Random rand = new Random();
        BlockPos[] randPos = this.randomPoss(rand, 16 * 16);

        Assertions.assertTrue(storage.isEmpty());

        this.fillStorage(storage, randPos);

        Assertions.assertFalse(storage.isEmpty());

        this.checkProtect(storage, randPos);

        this.checkNotProtect(storage, randPos);

        this.unFillStorage(storage, randPos);

        Assertions.assertTrue(storage.isEmpty());
    }

    @Test
    public void testSaveToNBT1Bit() {
        ExtendedProtectStorage1bit storage = new ExtendedProtectStorage1bit();
        Random rand = new Random();
        BlockPos[] randPos = this.randomPoss(rand, 16 * 16);

        this.fillStorage(storage, randPos);

        NBTTagCompound save = storage.writeToNBT(new NBTTagCompound());

        ExtendedProtectStorage1bit storageInNBT = new ExtendedProtectStorage1bit(save);

        Assertions.assertFalse(storageInNBT.isEmpty());

        this.checkProtect(storageInNBT, randPos);

        this.checkNotProtect(storageInNBT, randPos);
    }

    private BlockPos[] randomPoss(Random rand, int count) {
        BlockPos[] randPos = new BlockPos[count];

        for (int i = 0; i != randPos.length; i++) {
            randPos[i] = new BlockPos(rand.nextInt(16), rand.nextInt(16), rand.nextInt(16));
        }

        return randPos;
    }

    private void unFillStorage(IExtendedProtectStorage storage, BlockPos[] poss) {
        for (BlockPos pos : poss) {
            storage.setProtect(pos, false);
        }
    }

    private void fillStorage(IExtendedProtectStorage storage, BlockPos[] poss) {
        for (BlockPos pos : poss) {
            storage.setProtect(pos, true);
        }
    }

    private void checkProtect(IExtendedProtectStorage storage, BlockPos[] poss) {
        for (BlockPos pos : poss) {
            Assertions.assertTrue(storage.hasProtect(pos));
        }
    }

    private void checkNotProtect(IExtendedProtectStorage storage, BlockPos[] hasProtect) {
        for (int x = 0; x != 16; x++) {
            for (int z = 0; z != 16; z++) {
                for (int y = 0; y != 16; y++) {
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
                    Assertions.assertFalse(storage.hasProtect(x, y, z));
                }
            }
        }
    }
}
