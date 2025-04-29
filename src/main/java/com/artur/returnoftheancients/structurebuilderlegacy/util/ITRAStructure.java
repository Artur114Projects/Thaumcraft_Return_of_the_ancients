package com.artur.returnoftheancients.structurebuilderlegacy.util;

import net.minecraft.world.World;

public interface ITRAStructure {
    void gen(World world, int x, int y, int z);
    void protect(World world, int x, int y, int z);
    void addDisposableTask(ITRAStructureTask task);
}
