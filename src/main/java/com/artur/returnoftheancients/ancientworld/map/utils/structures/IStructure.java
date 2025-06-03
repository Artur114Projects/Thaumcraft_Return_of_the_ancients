package com.artur.returnoftheancients.ancientworld.map.utils.structures;

import com.artur.returnoftheancients.ancientworld.map.utils.*;
import com.artur.returnoftheancients.ancientworld.map.utils.maps.AbstractMap;
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
    boolean canConnect(EnumFace face);
    void build(World world, ChunkPos pos, Random rand);
    default boolean canReplace() {return true;}
    void setRotate(EnumRotate rotate);
    void bindMap(AbstractMap map);
    IStructure setY(int yIn);
}
