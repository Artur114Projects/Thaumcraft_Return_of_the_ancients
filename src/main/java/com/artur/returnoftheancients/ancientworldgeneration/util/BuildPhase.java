package com.artur.returnoftheancients.ancientworldgeneration.util;


import net.minecraft.util.math.BlockPos;

public class BuildPhase {
    public BuildPhase() {
        isClearStart = false;
        pos = new BlockPos.MutableBlockPos();
        bossGen = 0;
        finalizing = false;
        gen = false;
        clear = false;
        xtg = 0;
        ytg = 0;
        xtc = 0;
        ytc = 0;
        xtf = -128;
        ytf = -128;
    }

    public BlockPos.MutableBlockPos pos;
    public byte xtg;
    public byte ytg;
    public byte xtc;
    public byte ytc;
    public int xtf;
    public int ytf;
    public int bossGen;

    public byte t = 0;
    public boolean finalizing;
    public boolean isClearStart;
    public boolean gen;
    public boolean clear;


    public void clearArea() {
        clear = true;
    }
    public void genStructuresInWorld() {
        gen = true;
    }
    public void finalizing() {
        finalizing = true;
    }
    public void stop() {
        bossGen = 0;
        finalizing = false;
        gen = false;
        clear = false;
        xtg = 0;
        ytg = 0;
        xtc = 0;
        ytc = 0;
        xtf = -128;
        ytf = -128;
        t = 0;
    }
}
