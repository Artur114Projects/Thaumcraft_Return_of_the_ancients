package com.artur.returnoftheancients.ancientworld.map.utils.structures;

import com.artur.returnoftheancients.ancientworld.map.utils.*;
import com.artur.returnoftheancients.ancientworld.map.utils.maps.AbstractMap;
import com.artur.returnoftheancients.structurebuilder.StructuresBuildManager;
import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class StructureBase implements IStructure {
    public static final int baseY = 80;
    protected final Set<EnumFace> ports = new HashSet<>();
    protected AbstractMap map = null;
    protected EnumStructureType type;
    protected EnumRotate rotate;
    protected int y = baseY;
    protected StrPos pos;

    protected StructureBase() {}

    public StructureBase(EnumRotate rotate, EnumStructureType type, StrPos pos) {

        this.pos = pos.toImmutable();
        this.rotate = rotate;
        this.type = type;

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
    public @NotNull StrPos pos() {
        return this.pos;
    }

    @Override
    public @NotNull IStructureType type() {
        return this.type;
    }

    @Override
    public @NotNull EnumFace[] ports() {
        return this.ports.toArray(new EnumFace[0]);
    }

    @Override
    public @NotNull EnumRotate rotate() {
        return this.rotate;
    }

    @Override
    public @NotNull IStructure up(int n) {
        this.y += n;
        return this;
    }

    @Override
    public @NotNull IStructure down(int n) {
        this.y -= n;
        return this;
    }

    @Override
    public boolean canConnect(EnumFace face) {
        return this.ports.contains(face);
    }

    @Override
    public void build(World world, ChunkPos pos, Random rand) {
        UltraMutableBlockPos blockPos = UltraMutableBlockPos.obtain();
        blockPos.setPos(pos).setY(this.y);
        StructuresBuildManager.createBuildRequest(world, blockPos, this.type.stringId(this.rotate)).setIgnoreAir().build();
        UltraMutableBlockPos.release(blockPos);
    }

    @Override
    public void setRotate(EnumRotate rotate) {
        this.rotate = rotate;
        this.compilePorts();
    }

    @Override
    public void bindMap(AbstractMap map) {
        this.map = map;
    }

    private void compilePorts() {
        this.ports.clear();
        this.ports.addAll(this.type.ports(this.rotate));
    }
}
