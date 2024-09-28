package com.artur.returnoftheancients.ancientworldgeneration.util.interfaces;

import net.minecraft.world.World;

public interface IBuild {
    void build(World world);
    boolean isBuild();
    boolean isRequestToDelete();

    void onGen(int x, int y);
    void onClear(int x, int y);

    void onStart();
    void onGenStart();
    void onClearStart();
    void onFinalizing();
    void onFinal();
}
