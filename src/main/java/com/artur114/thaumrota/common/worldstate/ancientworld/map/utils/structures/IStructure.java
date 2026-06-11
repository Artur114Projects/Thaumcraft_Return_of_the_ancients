package com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.structures;

import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.EnumFace;
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.EnumRotate;
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.IStructureType;
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.StrPos;
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.maps.AbstractMap;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public interface IStructure {
    @NotNull IStructure copy();
    @NotNull StrPos pos();
    @NotNull IStructureType type();
    @NotNull EnumFace[] ports();
    @NotNull EnumRotate rotate();
    @NotNull IStructure up(int n);
    @NotNull IStructure down(int n);
    boolean canConnect(EnumFace face);
    void build(World world, ChunkPos pos, Random rand);
    default boolean canReplace() {return true;}
    void setRotate(EnumRotate rotate);
    void bindMap(AbstractMap map);
}
