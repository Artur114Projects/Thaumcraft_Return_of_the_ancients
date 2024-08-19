package com.artur.returnoftheancients.ancientworldgeneration.util;


import net.minecraft.util.math.BlockPos;

public class BuildPhase {
    public BuildPhase() {
        isClearStart = false;
        pos = new BlockPos.MutableBlockPos();
        bossGen = 0;
        finalizing = false;
        please = false;
        clear = false;
        xtp = 0;
        ytp = 0;
        xtc = 0;
        ytc = 0;
        xtf = -128;
        ytf = -128;
    }

    public BlockPos.MutableBlockPos pos;
    public byte xtp;
    public byte ytp;
    public byte xtc;
    public byte ytc;
    public int xtf;
    public int ytf;
    public int bossGen;

    public byte t = 0;
    public boolean finalizing;
    public boolean isClearStart;
    public boolean please;
    public boolean clear;


    public void clearArea() {
        clear = true;
    }
    public void genStructuresInWorld() {
        please = true;
    }
    public void finalizing() {
        finalizing = true;
    }
    public void stop() {
        bossGen = 0;
        finalizing = false;
        please = false;
        clear = false;
        xtp = 0;
        ytp = 0;
        xtc = 0;
        ytc = 0;
        xtf = -128;
        ytf = -128;
        t = 0;
    }
}
