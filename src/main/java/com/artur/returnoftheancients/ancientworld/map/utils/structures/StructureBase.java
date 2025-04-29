package com.artur.returnoftheancients.ancientworld.map.utils.structures;

import com.artur.returnoftheancients.ancientworld.map.utils.EnumStructure;
import com.artur.returnoftheancients.ancientworld.map.utils.StructurePos;
import com.artur.returnoftheancients.ancientworld.map.utils.StructuresMap;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class StructureBase implements IStructure {
    protected final Set<StructurePos.Face> ports = new HashSet<>();
    protected EnumStructure.Rotate rotate;
    protected StructuresMap map = null;
    protected final EnumStructure type;
    protected final StructurePos pos;
    protected int y = 80;

    public StructureBase(EnumStructure.Rotate rotate, EnumStructure type, StructurePos pos) {
        if (type.isMultiChunk()) {
            throw new IllegalArgumentException();
        }

        this.rotate = rotate;
        this.type = type;
        this.pos = pos;

        this.compilePorts();
    }

    protected StructureBase(StructureBase parent) {
        this.ports.addAll(parent.ports);
        this.rotate = parent.rotate;
        this.type = parent.type;
        this.pos = parent.pos;
        this.y = parent.y;
        this.map = null;
    }

    @Override
    public @NotNull IStructure copy() {
        return new StructureBase(this);
    }

    @Override
    public @NotNull StructurePos pos() {
        return this.pos;
    }

    @Override
    public @NotNull EnumStructure type() {
        return this.type;
    }

    @Override
    public @NotNull EnumStructure.Rotate rotate() {
        return this.rotate;
    }

    @Override
    public boolean canConnect(StructurePos.Face face) {
        return this.ports.contains(face);
    }

    @Override
    public void build(World world, ChunkPos pos, Random rand) {

    }

    @Override
    public void setRotate(EnumStructure.Rotate rotate) {
        this.rotate = rotate;
        this.compilePorts();
    }

    @Override
    public void bindMap(StructuresMap map) {
        this.map = map;
    }

    private void compilePorts() {
        this.ports.clear();
        Collections.addAll(this.ports, this.type.ports(this.rotate));
    }
}
