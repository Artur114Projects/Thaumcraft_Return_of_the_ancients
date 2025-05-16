package com.artur.returnoftheancients.ancientworld.map.utils.structures;

import net.minecraft.world.World;

public interface IStructureInteractive extends IStructure {
    void bindWorld(World world);
    void update();
}
