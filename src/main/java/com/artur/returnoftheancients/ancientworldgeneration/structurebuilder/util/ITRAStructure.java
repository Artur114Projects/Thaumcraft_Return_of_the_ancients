package com.artur.returnoftheancients.ancientworldgeneration.structurebuilder.util;

import net.minecraft.world.World;

public interface ITRAStructure {
    void gen(World world, int x, int y, int z);
    void addDisposableTask(ITRAStructureTask task);
}
