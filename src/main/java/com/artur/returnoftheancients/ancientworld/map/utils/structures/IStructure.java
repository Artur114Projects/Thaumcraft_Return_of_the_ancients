package com.artur.returnoftheancients.ancientworld.map.utils.structures;

import com.artur.returnoftheancients.ancientworld.map.utils.EnumStructure;
import com.artur.returnoftheancients.ancientworld.map.utils.StructurePos;
import com.artur.returnoftheancients.ancientworld.map.utils.maps.AbstractMap;
import com.artur.returnoftheancients.ancientworld.map.utils.maps.ImmutableMap;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public interface IStructure {
    @NotNull IStructure copy();
    @NotNull StructurePos pos();
    @NotNull EnumStructure type();
    @NotNull StructurePos.Face[] ports();
    @NotNull EnumStructure.Rotate rotate();
    boolean canConnect(StructurePos.Face face);
    void build(World world, ChunkPos pos, Random rand);
    void setRotate(EnumStructure.Rotate rotate);
    void bindMap(AbstractMap map);
}
