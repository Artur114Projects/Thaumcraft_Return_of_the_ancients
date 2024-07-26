package com.artur.returnoftheancients.ancientworldgeneration.util;

public class StructureMap {
    private final byte[][] structures;
    private final byte[][] rotates;
    public final int SIZE;

    public StructureMap(byte[][] structures, byte[][] rotates) {
        if (!(structures.length == rotates.length && structures[0].length == rotates[0].length)) throw new RuntimeException("StructureMap.class, transferred incorrect arrays");
        this.structures = structures;
        this.rotates = rotates;
        SIZE = structures.length;
    }

    public byte getStructure(int x, int y) {
        return structures[y][x];
    }

    public byte getRotate(int x, int y) {
        return rotates[y][x];
    }


}
