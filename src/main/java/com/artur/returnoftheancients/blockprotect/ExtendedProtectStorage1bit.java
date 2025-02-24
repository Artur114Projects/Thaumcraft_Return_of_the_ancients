package com.artur.returnoftheancients.blockprotect;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.util.math.BlockPos;

public class ExtendedProtectStorage1bit implements IExtendedProtectStorage {
    private final boolean[] bitsBuf = new boolean[16];
    private final short[] data = new short[16 * 16];
    private int protectPosCount = 0;

    public ExtendedProtectStorage1bit() {}

    public ExtendedProtectStorage1bit(NBTTagCompound nbt) {
        if (!nbt.hasKey("protectedPoss")) {
            return;
        }

        NBTTagList list = nbt.getTagList("protectedPoss", 2);
        int[] posBuf = new int[3];

        for (int i = 0; i != list.tagCount(); i++) {
            int[] pos = this.unpackPos(posBuf, ((NBTTagShort) list.get(i)).getShort());

            this.setProtect(pos[0], pos[1], pos[2], true);
        }
    }

    @Override
    public boolean hasProtect(BlockPos pos) {
        return this.hasProtect(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public boolean hasProtect(int x, int y, int z) {
        return this.unpackBit(y & 15, this.data[this.horizontalIndex(x, z)]);
    }

    @Override
    public boolean setProtect(BlockPos pos, boolean state) {
        return this.setProtect(pos.getX(), pos.getY(), pos.getZ(), state);
    }

    @Override
    public boolean setProtect(int x, int y, int z, boolean state) {
        int index = this.horizontalIndex(x, z);
        short pack = this.data[index];

        boolean[] bits = this.unpackBits(bitsBuf, pack);

        boolean currentState = bits[y & 15];

        boolean flag = currentState != state;

        if (!currentState && state) {
            protectPosCount++;
        } else if (currentState && !state) {
            protectPosCount--;
        }

        bits[y & 15] = state;

        this.data[index] = this.packBits(bits);

        return flag;
    }

    @Override
    public boolean isEmpty() {
        return protectPosCount == 0;
    }

    private int horizontalIndex(int x, int z) {
        return (x & 15) + (z & 15) * 16;
    }

    private boolean unpackBit(int id, short packedBits) {
        return (packedBits & (1 << (15 - (id & 15)))) != 0;
    }

    private boolean[] unpackBits(boolean[] buf, short packedBits) {
        for (int i = 0; i < 16; i++) {
            buf[i] = (packedBits & (1 << (15 - i))) != 0;
        }
        return buf;
    }

    private short packBits(boolean[] bits) {
        int packed = 0;
        for (int i = 0; i < 16; i++) {
            if (bits[i]) packed |= (1 << (15 - i));
        }
        return (short) packed;
    }

    private short packPos(int x, int y, int z) {
        return (short) (((x & 15) << 8) | ((y & 15) << 4) | (z & 15));
    }

    private int[] unpackPos(int[] posBuf, short packedPos) {
        posBuf[0] = (packedPos >> 8) & 15;
        posBuf[1] = (packedPos >> 4) & 15;
        posBuf[2] = packedPos & 15;
        return posBuf;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        if (this.isEmpty()) {
            return nbt;
        }
        NBTTagList list = new NBTTagList();
        for (int x = 0; x != 16; x++) {
            for (int z = 0; z != 16; z++) {
                boolean[] bits = this.unpackBits(bitsBuf, data[this.horizontalIndex(x, z)]);
                for (int y = 0; y != 16; y++) {
                    if (bits[y]) list.appendTag(new NBTTagShort(this.packPos(x, y, z)));
                }
            }
        }
        nbt.setTag("protectedPoss", list);
        return nbt;
    }
}
