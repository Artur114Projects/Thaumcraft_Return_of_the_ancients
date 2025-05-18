package com.artur.returnoftheancients.ancientworld.map.utils.structures;

import com.artur.returnoftheancients.ancientworld.map.utils.*;
import com.artur.returnoftheancients.ancientworld.map.utils.maps.AbstractMap;
import com.artur.returnoftheancients.structurebuilder.StructureBuildersManager;
import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class StructureBase implements IStructure {
    protected final Set<EnumFace> ports = new HashSet<>();
    protected AbstractMap map = null;
    protected EnumStructureType type;
    protected EnumRotate rotate;
    protected StrPos pos;
    protected int y = 80;

    protected StructureBase() {}

    public StructureBase(EnumRotate rotate, EnumStructureType type, StrPos pos) {

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
    public boolean canConnect(EnumFace face) {
        return this.ports.contains(face);
    }

    @Override
    public void build(World world, ChunkPos pos, Random rand) {
        UltraMutableBlockPos blockPos = UltraMutableBlockPos.getBlockPosFromPoll();
        blockPos.setPos(pos).setY(this.y);
        StructureBuildersManager.createBuildRequest(world, blockPos, this.type.stringId(this.rotate)).build();
        UltraMutableBlockPos.returnBlockPosToPoll(blockPos);
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

    @Override
    public IStructure setY(int yIn) {
        this.y = yIn;
        return this;
    }

    private void compilePorts() {
        this.ports.clear();
        this.ports.addAll(this.type.ports(this.rotate));
    }
}
