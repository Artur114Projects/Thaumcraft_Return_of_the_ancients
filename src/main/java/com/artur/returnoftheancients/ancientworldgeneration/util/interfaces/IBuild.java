package com.artur.returnoftheancients.ancientworldgeneration.util.interfaces;

import net.minecraft.world.World;

public interface IBuild {
    void build(World world);
    boolean isBuild();
    void onPlease();
    void onClear();
    void onPleaseStart();
    void onClearStart();
    void onReloadLightStart();
    void onFinish();
}
