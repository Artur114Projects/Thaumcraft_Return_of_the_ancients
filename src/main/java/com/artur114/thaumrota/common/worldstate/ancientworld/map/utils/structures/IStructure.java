package com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.structures;

import com.artur114.thaumrota.client.light.ILightSource;
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.EnumFace;
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.EnumRotate;
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.IStructureType;
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.StrPos;
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.maps.AbstractMap;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public interface IStructure {
    int yPos();
    void setYPos(int y);
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

    @SideOnly(Side.CLIENT)
    List<ILightSource> light(ChunkPos pos);
}
