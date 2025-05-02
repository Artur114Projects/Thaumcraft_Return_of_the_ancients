package com.artur.returnoftheancients.structurebuilder.interf;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public interface IStructureBuilder {
    @NotNull String name();
    void build(World world, BlockPos pos, IBuildProperties properties);
}
