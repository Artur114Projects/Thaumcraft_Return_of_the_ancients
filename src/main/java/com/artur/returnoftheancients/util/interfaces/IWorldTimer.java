package com.artur.returnoftheancients.util.interfaces;

import net.minecraft.world.World;

public interface IWorldTimer {
    void start(long time, World world);
    boolean is(long time, World world);
    boolean isCycle(long time, World world);
}
