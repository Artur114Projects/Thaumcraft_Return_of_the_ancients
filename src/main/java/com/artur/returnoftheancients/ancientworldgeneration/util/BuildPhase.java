package com.artur.returnoftheancients.ancientworldgeneration.util;


import net.minecraft.util.math.BlockPos;

public class BuildPhase {
    public BuildPhase() {
        pos = new BlockPos.MutableBlockPos();
        bossGen = 0;
        reloadLight = false;
        please = false;
        clear = false;
        xtp = 0;
        ytp = 0;
        xtc = 0;
        ytc = 0;
        xtl = -128;
        ytl = -128;
    }

    public BlockPos.MutableBlockPos pos;
    public byte xtp;
    public byte ytp;
    public byte xtc;
    public byte ytc;
    public int xtl;
    public int ytl;
    public int bossGen;

    public byte t = 0;
    public boolean reloadLight;

    public boolean please;
    public boolean clear;


    public void clearArea() {
        clear = true;
    }
    public void genStructuresInWorld() {
        please = true;
    }
    public void reloadLight() {
        reloadLight = true;
    }
    public void stop() {
        bossGen = 0;
        reloadLight = false;
        please = false;
        clear = false;
        xtp = 0;
        ytp = 0;
        xtc = 0;
        ytc = 0;
        xtl = -128;
        ytl = -128;
        t = 0;
    }
}
