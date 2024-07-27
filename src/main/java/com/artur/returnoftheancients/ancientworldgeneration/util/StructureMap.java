package com.artur.returnoftheancients.ancientworldgeneration.util;

import net.minecraft.nbt.NBTTagCompound;

import java.util.Arrays;

public class StructureMap {
    private final byte[][] structures;
    private final byte[][] rotates;
    public final int SIZE;

    public StructureMap(byte[][] structures, byte[][] rotates) {
        if (!(structures.length == rotates.length && structures[0].length == rotates[0].length && structures.length == structures[0].length)) throw new RuntimeException("StructureMap.class, transferred incorrect arrays");
        this.structures = structures;
        this.rotates = rotates;
        SIZE = structures.length;
    }

    public StructureMap(NBTTagCompound nbt) {
        if (!nbt.hasKey("SIZE")) throw new RuntimeException("StructureMap.class, transferred incorrect NBTTag EC:0");
        SIZE = nbt.getInteger("SIZE");
        rotates = new byte[SIZE][SIZE];
        structures = new byte[SIZE][SIZE];

        if (!nbt.hasKey("structures")) throw new RuntimeException("StructureMap.class, transferred incorrect NBTTag EC:1");
        NBTTagCompound structuresNBT = nbt.getCompoundTag("structures");
        if (!nbt.hasKey("rotates")) throw new RuntimeException("StructureMap.class, transferred incorrect NBTTag EC:2");
        NBTTagCompound rotatesNBT = nbt.getCompoundTag("rotates");

        arrayInNBT(structuresNBT, structures);
        arrayInNBT(rotatesNBT, rotates);
    }

    private void arrayInNBT(NBTTagCompound nbt, byte[][] array) {
        if (!nbt.hasKey("y:0")) throw new RuntimeException("StructureMap.class, transferred incorrect NBTTag EC:3");
        for (int i = 0; i != SIZE; i++) {
            NBTTagCompound compound = nbt.getCompoundTag("y:" + i);
            if (!compound.hasKey("x:0")) throw new RuntimeException("StructureMap.class, transferred incorrect NBTTag EC:4");
            for (int j = 0; j != SIZE; j++) {
                array[i][j] = compound.getByte("x:" + j);
            }
        }
    }

    private NBTTagCompound NBTInArray(byte[][] array) {
        NBTTagCompound nbt = new NBTTagCompound();
        for (int i = 0; i != array.length; i++) {
            NBTTagCompound compound = new NBTTagCompound();
            for (int j = 0; j != array[i].length; j++) {
                compound.setByte("x:" + j, array[i][j]);
            }
            nbt.setTag("y:" + i, compound);
        }
        return nbt;
    }

    public byte getStructure(int x, int y) {
        return structures[y][x];
    }

    public byte getRotate(int x, int y) {
        return rotates[y][x];
    }

    public NBTTagCompound toNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("SIZE", SIZE);
        nbt.setTag("structures", NBTInArray(structures));
        nbt.setTag("rotates", NBTInArray(rotates));
        return nbt;
    }

    @Override
    public String toString() {
        StringBuilder finalS = new StringBuilder("\n");
        for (int y = 0; y != SIZE; y++) {
            StringBuilder s = new StringBuilder();
            for (int x = 0; x != SIZE; x++) {
                s.append(" (").append(structures[y][x]).append(",").append(rotates[y][x]).append("),");
            }
            finalS.append(s).append("\n");
        }
        return String.valueOf(finalS);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StructureMap) {
            StructureMap map = (StructureMap) obj;
            return Arrays.deepEquals(map.rotates, rotates) && Arrays.deepEquals(map.structures, structures);
        } else {
            return false;
        }
    }
}
