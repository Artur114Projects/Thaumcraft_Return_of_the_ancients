package com.artur.returnoftheancients.ancientworldgeneration.util.interfaces;

import net.minecraft.world.World;

public interface IBuild {
    void build(World world);
    boolean isBuild();

    void onPlease(int x, int y);
    void onClear(int x, int y);

    void onStart();
    void onPleaseStart();
    void onClearStart();
    void onReloadLightStart();
    void onFinish();
}
