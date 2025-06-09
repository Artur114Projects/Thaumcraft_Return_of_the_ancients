package com.artur.returnoftheancients.ancientworld.map.utils.structures;

import com.artur.returnoftheancients.ancientworld.map.utils.EnumFace;
import com.artur.returnoftheancients.ancientworld.map.utils.EnumRotate;
import com.artur.returnoftheancients.ancientworld.map.utils.IStructureType;
import com.artur.returnoftheancients.ancientworld.map.utils.StrPos;
import com.artur.returnoftheancients.ancientworld.map.utils.maps.AbstractMap;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class StructureMapBorder implements IStructure {
    public static final IStructureType MAP_BORDER_TYPE = new StructureMapBorderType();

    private final StrPos pos;
    public StructureMapBorder(StrPos pos) {
        this.pos = pos.toImmutable();
    }

    @Override
    public @NotNull IStructure copy() {
        return new StructureMapBorder(this.pos);
    }

    @Override
    public @NotNull StrPos pos() {
        return pos;
    }

    @Override
    public @NotNull IStructureType type() {
        return MAP_BORDER_TYPE;
    }

    @Override
    public @NotNull EnumFace[] ports() {
        return new EnumFace[0];
    }

    @Override
    public @NotNull EnumRotate rotate() {
        return EnumRotate.NON;
    }

    @Override
    public @NotNull IStructure up(int n) {
        return this;
    }

    @Override
    public @NotNull IStructure down(int n) {
        return this;
    }

    @Override
    public boolean canConnect(EnumFace face) {
        return false;
    }

    @Override
    public void build(World world, ChunkPos pos, Random rand) {}

    @Override
    public void setRotate(EnumRotate rotate) {}

    @Override
    public void bindMap(AbstractMap map) {}

    public static class StructureMapBorderType implements IStructureType {

        @Override
        public IStructure create(EnumRotate rotate, StrPos pos) {
            return null;
        }

        @Override
        public String stringId(EnumRotate rotate) {
            return "map_border";
        }
    }
}
