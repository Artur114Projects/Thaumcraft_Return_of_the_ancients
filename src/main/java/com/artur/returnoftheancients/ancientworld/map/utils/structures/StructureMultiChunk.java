package com.artur.returnoftheancients.ancientworld.map.utils.structures;

import com.artur.returnoftheancients.ancientworld.map.utils.EnumStructure;
import com.artur.returnoftheancients.ancientworld.map.utils.StructurePos;
import com.artur.returnoftheancients.structurebuilder.StructureBuildersManager;
import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

public abstract class StructureMultiChunk extends StructureBase implements IStructureMultiChunk {
    protected final List<IStructureSegment> segmentsWithPorts = new ArrayList<>();
    protected final List<IStructureSegment> segments = new ArrayList<>();

    public StructureMultiChunk(EnumStructure.Rotate rotate, EnumStructure type, StructurePos pos) {
        if (!type.isMultiChunk()) {
            throw new IllegalArgumentException();
        }

        this.rotate = rotate;
        this.type = type;
        this.pos = pos;

        this.compileSegments();
    }

    protected StructureMultiChunk(StructureMultiChunk parent) {
        super(parent);

        for (IStructureSegment segment : parent.segments) {
            IStructureSegment copy = segment.copy();
            this.segments.add(copy);
            if (copy.ports().length != 0) {
                this.segmentsWithPorts.add(copy);
            }
        }
    }

    @Override
    public void build(World world, ChunkPos pos, Random rand) {
        UltraMutableBlockPos blockPos = UltraMutableBlockPos.getBlockPosFromPoll();
        blockPos.setPos(pos).add(8, 0, 8).setY(this.y);
        StructureBuildersManager.createBuildRequest(world, blockPos, this.type.getStringId(this.rotate)).setPosAsXZCenter().build();
        UltraMutableBlockPos.returnBlockPosToPoll(blockPos);
    }

    @Override
    public void insertSegments(Consumer<IStructureSegment> inserter) {
        for (IStructureSegment segment : this.segments) {
            inserter.accept(segment);
        }
    }

    @Override
    public @NotNull List<IStructureSegment> segments() {
        return new ArrayList<>(this.segments);
    }

    @Override
    public @NotNull List<IStructureSegment> segmentsWithPorts() {
        return new ArrayList<>(this.segmentsWithPorts);
    }

    @Override
    public void setRotate(EnumStructure.Rotate rotate) {}

    @Override
    public abstract @NotNull IStructure copy();
    protected abstract char[][] structureForm();

    protected void compileSegments() {
        char[][] form = this.structureForm();

        for (int i = 0; i != form.length; i++) {
            if (form[0].length != form[i].length) {
                throw new IllegalArgumentException();
            }
        }

        int centerX = -1;
        int centerY = -1;
        for (int y = 0; y != form.length; y++) {
            for (int x = 0; x != form[y].length; x++) {
                if (form[y][x] == 'c') {
                    if (centerY == -1) {
                        centerX = x;
                        centerY = y;
                    } else {
                        throw new IllegalArgumentException();
                    }
                }
            }
        }

        for (int y = 0; y != form.length; y++) {
            for (int x = 0; x != form[y].length; x++) {
                if (form[y][x] == 's') {
                    List<StructurePos.Face> ports = this.getPortsFromForm(x, y, form);
                    StructurePos pos = new StructurePos(this.pos.getX() + (x - centerX), this.pos.getY() + (y - centerY));
                    IStructureSegment segment = new StructureSegment(this, ports, pos);
                    if (!ports.isEmpty()) {
                        this.segmentsWithPorts.add(segment);
                    }
                    this.segments.add(segment);
                }
            }
        }
    }

    protected List<StructurePos.Face> getPortsFromForm(int x, int y, char[][] form) {
        List<StructurePos.Face> faces = new ArrayList<>(4);
        if (x - 1 >= 0 && form[y][x - 1] == 'p') {
            faces.add(StructurePos.Face.LEFT);
        }
        if (x + 1 < form[y].length && form[y][x + 1] == 'p') {
            faces.add(StructurePos.Face.RIGHT);
        }
        if (y - 1 >= 0 && form[y - 1][x] == 'p') {
            faces.add(StructurePos.Face.UP);
        }
        if (y + 1 < form.length && form[y + 1][x] == 'p') {
            faces.add(StructurePos.Face.DOWN);
        }
        return faces;
    }

    public static class StructureSegment extends StructureBase implements IStructureMultiChunk.IStructureSegment {
        protected IStructureMultiChunk parent;
        public StructureSegment(StructureMultiChunk parent, List<StructurePos.Face> ports, StructurePos pos) {
            this.rotate = parent.rotate;
            this.type = parent.type;
            this.pos = pos;

            this.parent = parent;
            this.ports.addAll(ports);
            this.y = parent.y;
        }

        public StructureSegment(StructureSegment parent) {
            super(parent);

            this.parent = parent.parent;
        }

        @Override
        public @NotNull IStructureMultiChunk.IStructureSegment copy() {
            return new StructureSegment(this);
        }

        @Override
        public @NotNull IStructureMultiChunk parent() {
            return this.parent;
        }

        @Override
        public void build(World world, ChunkPos pos, Random rand) {}

        @Override
        public void setRotate(EnumStructure.Rotate rotate) {
            this.rotate = parent.rotate();
        }
    }
}
