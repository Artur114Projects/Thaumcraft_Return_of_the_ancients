package com.artur.returnoftheancients.ancientworld.map.utils.maps;

import com.artur.returnoftheancients.ancientworld.map.utils.*;
import com.artur.returnoftheancients.ancientworld.map.utils.structures.IStructure;
import com.artur.returnoftheancients.ancientworld.map.utils.structures.IStructureMultiChunk;
import com.artur.returnoftheancients.ancientworld.map.utils.structures.StructureBase;
import com.artur.returnoftheancients.ancientworld.map.utils.structures.StructureMapBorder;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ImmutableMap extends AbstractMap {
    private final List<Runnable> frozenChanges = new ArrayList<>();
    private boolean isChangesFrozen = false;

    public ImmutableMap(int size) {
        super(size);
    }

    @Override
    public @Nullable IStructureType structureType(StrPos pos) {
        return this.structureType(pos.getX(), pos.getY());
    }

    @Override
    public @Nullable IStructureType structureType(int x, int y) {
        IStructure structure = this.structurePrivate(x, y);
        if (structure == null) return StructureMapBorder.MAP_BORDER_TYPE;
        return structure.type();
    }

    @Override
    public @Nullable EnumRotate structureRotate(StrPos pos) {
        return this.structureRotate(pos.getX(), pos.getY());
    }

    @Override
    public @Nullable EnumRotate structureRotate(int x, int y) {
        IStructure structure = this.structurePrivate(x, y);
        if (structure == null) return EnumRotate.NON;
        return structure.rotate();
    }

    @Override
    public @Nullable IStructure structure(StrPos pos) {
        return this.structure(pos.getX(), pos.getY());
    }

    @Override
    public @Nullable IStructure structure(int x, int y) {
        IStructure structure = this.structurePrivate(x, y);
        if (x < 0 || y < 0 || x >= size || y >= size) return new StructureMapBorder(new StrPos(x, y));
        if (structure == null) return null;
        return structure;
    }

    @Override
    public void insetRotate(StrPos pos, EnumRotate rotate) {
        this.insetRotate(pos.getX(), pos.getY(), rotate);
    }

    @Override
    public void insetRotate(int x, int y, EnumRotate rotate) {
        if (this.isChangesFrozen) {
            this.frozenChanges.add(() -> this.insetRotate(x, y, rotate));
            return;
        }
        IStructure structure = this.structurePrivate(x, y);
        if (structure != null) {
            structure.setRotate(rotate);
        }
    }

    @Override
    public void insetStructure(IStructure structure) {
        if (this.isChangesFrozen) {
            this.frozenChanges.add(() -> this.insetStructure(structure));
            return;
        }
        if (structure instanceof IStructureMultiChunk) {
            this.privateInsetMultiChunkStructure((IStructureMultiChunk) structure);
        } else {
            this.privateInsetStructure(structure);
        }
    }

    @Override
    public List<StrPos> connectedStructures(StrPos pos) {
        List<StrPos> ret = new ArrayList<>(4);
        if (pos.isOutOfBounds(this.size)) return ret;
        IStructure structure = this.structurePrivate(pos);
        if (structure == null) return ret;

        if (structure instanceof IStructureMultiChunk) {
            for (IStructureMultiChunk.IStructureSegment segment : ((IStructureMultiChunk) structure).segmentsWithPorts()) {
                ret.addAll(connectedStructures(segment.pos()));
            }
            return ret;
        }

        for (EnumFace face : EnumFace.values()) {
            if (!structure.canConnect(face)) continue;
            StrPos offsetPos = pos.offset(face);
            IStructure neighbor = this.structurePrivate(offsetPos);
            if (neighbor != null && neighbor.canConnect(face.getOppose())) {
                ret.add(offsetPos);
            }
        }

        return ret;
    }

    public void createBaseStructure(StrPos pos, EnumStructureType type, EnumRotate rotate) {
        if (pos.isOutOfBounds(this.size)) return;
        IStructure structure = type.create(rotate, pos);
        structure.bindMap(this);
        this.structures[this.index(pos)] = structure;
    }

    public void createBaseStructure(int x, int y, EnumStructureType type, EnumRotate rotate) {
        this.createBaseStructure(new StrPos(x, y), type, rotate);
    }

    public InteractiveMap toInteractive(World world, ChunkPos center) {
        return new InteractiveMap(this, world, center);
    }

    public void freezeChanges() {
        this.isChangesFrozen = true;
    }

    public void acceptChanges() {
        this.isChangesFrozen = false;

        for (Runnable run : this.frozenChanges) run.run();

        this.frozenChanges.clear();
    }

    private void privateInsetStructure(IStructure structure) {
        StrPos pos = structure.pos();
        if (pos.isOutOfBounds(this.size)) return;
        structure.bindMap(this);
        this.structures[this.index(pos)] = structure;
    }

    private void privateInsetMultiChunkStructure(IStructureMultiChunk structure) {
        StrPos pos = structure.pos();
        if (pos.isOutOfBounds(this.size)) return;
        structure.bindMap(this);
        this.structures[this.index(pos)] = structure;
        structure.insertSegments(this::privateInsetStructure);
    }

    private @Nullable IStructure structurePrivate(StrPos pos) {
        return this.structure(pos.getX(), pos.getY());
    }

    private @Nullable IStructure structurePrivate(int x, int y) {
        if (x < 0 || y < 0 || x >= size || y >= size) return null;
        return this.structures[this.index(x, y)];
    }

    private @Nullable IStructure structurePrivate(int index) {
        if (index >= this.area() || index < 0) return null;
        return this.structures[index];
    }
}
