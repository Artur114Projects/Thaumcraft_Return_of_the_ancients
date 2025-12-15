package com.artur.returnoftheancients.ancientworld.map.utils.structures;

import com.artur.returnoftheancients.ancientworld.map.utils.*;
import com.artur.returnoftheancients.structurebuilder.StructureBuildersManager;
import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public abstract class StructureMultiChunk extends StructureBase implements IStructureMultiChunk {
    protected final List<IStructureSegment> segmentsWithPorts = new ArrayList<>();
    protected final List<IStructureSegment> segments = new ArrayList<>();
    protected final IMultiChunkStrForm form;
    protected EnumMultiChunkStrType type;

    public StructureMultiChunk(EnumRotate rotate, EnumMultiChunkStrType type, StrPos pos) {
        this.pos = pos.toImmutable();
        this.form = type.form();
        this.rotate = rotate;
        this.type = type;

        this.compileSegments();
    }

    protected StructureMultiChunk(StructureMultiChunk parent) {
        super(parent);

        for (IStructureSegment segment : parent.segments) {
            IStructureSegment copy = segment.copy();
            copy.bindParent(this);
            this.segments.add(copy);
            if (copy.ports().length != 0) {
                this.segmentsWithPorts.add(copy);
            }
        }

        this.form = parent.form;
        this.type = parent.type;
    }

    @Override
    public void build(World world, ChunkPos pos, Random rand) {
        UltraMutableBlockPos blockPos = UltraMutableBlockPos.obtain();
        blockPos.setPos(pos).add(8, 0, 8).setY(this.y);
        StructureBuildersManager.createBuildRequest(world, blockPos, this.type.stringId(this.rotate)).setIgnoreAir().setPosAsXZCenter().build();
        UltraMutableBlockPos.release(blockPos);
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
    public @NotNull IStructureType type() {
        return this.type;
    }

    @Override
    public boolean canReplace() {
        return false;
    }

    @Override
    public void setRotate(EnumRotate rotate) {}

    @Override
    public abstract @NotNull IStructure copy();

    protected void compileSegments() {
        IMultiChunkStrForm.IOffset[] offsets = this.form.offsets(this.pos(), this.rotate());

        for (IMultiChunkStrForm.IOffset offset : offsets) {
            IStructureSegment segment = new StructureSegment(this, offset.ports(), offset.globalPos());
            if (offset.ports().length != 0) this.segmentsWithPorts.add(segment);
            this.segments.add(segment);
        }
    }

    public static class StructureSegment extends StructureBase implements IStructureMultiChunk.IStructureSegment {
        protected IStructureMultiChunk parent;
        protected EnumMultiChunkStrType type;
        public StructureSegment(StructureMultiChunk parent, EnumFace[] ports, StrPos pos) {
            this.pos = pos.toImmutable();
            this.rotate = parent.rotate;
            this.type = parent.type;

            this.parent = parent;
            this.ports.addAll(Arrays.asList(ports));
            this.y = parent.y;
        }

        public StructureSegment(StructureSegment parent) {
            super(parent);

            this.type = parent.type;
            this.parent = null;
        }

        @Override
        public @NotNull IStructureMultiChunk.IStructureSegment copy() {
            return new StructureSegment(this);
        }

        @Override
        public void bindParent(IStructureMultiChunk parent) {
            this.parent = parent;
        }

        @Override
        public @NotNull IStructureMultiChunk parent() {
            return this.parent;
        }

        @Override
        public void build(World world, ChunkPos pos, Random rand) {}

        @Override
        public @NotNull IStructureType type() {
            return this.type;
        }

        @Override
        public void setRotate(EnumRotate rotate) {
            this.rotate = parent.rotate();
        }

        @Override
        public boolean canReplace() {
            return false;
        }
    }
}
