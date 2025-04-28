package com.artur.returnoftheancients.ancientworld.map.utils;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public interface IStructure {
    @NotNull IStructure copy();
    @NotNull StructurePos pos();
    @NotNull EnumStructure type();
    @NotNull EnumStructure.Rotate rotate();
    boolean canConnect(StructurePos.Face face);
    void build(World world, ChunkPos pos, Random rand);
    void setRotate(EnumStructure.Rotate rotate);
    void bindMap(StructuresMap map);
}
