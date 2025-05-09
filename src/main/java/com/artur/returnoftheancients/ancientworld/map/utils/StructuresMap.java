package com.artur.returnoftheancients.ancientworld.map.utils;

import com.artur.returnoftheancients.ancientworld.map.utils.structures.IStructure;
import com.artur.returnoftheancients.ancientworld.map.utils.structures.IStructureMultiChunk;
import com.artur.returnoftheancients.ancientworld.map.utils.structures.StructureBase;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class StructuresMap {
    private final IStructure[] structures;
    private final int size;

    public StructuresMap(int size) {
        this.structures = new IStructure[size * size];
        this.size = size;
    }

    private int index(int x, int y) {
        return x + y * this.size;
    }

    private int index(StructurePos pos) {
        return index(pos.getX(), pos.getY());
    }

    public @Nullable EnumStructure structureType(StructurePos pos) {
        return this.structureType(pos.getX(), pos.getY());
    }

    public @Nullable EnumStructure structureType(int x, int y) {
        IStructure structure = this.structurePrivate(x, y);
        if (structure == null) return null;
        return structure.type();
    }

    public @Nullable EnumStructure.Rotate structureRotate(StructurePos pos) {
        return this.structureRotate(pos.getX(), pos.getY());
    }

    public @Nullable EnumStructure.Rotate structureRotate(int x, int y) {
        IStructure structure = this.structurePrivate(x, y);
        if (structure == null) return null;
        return structure.rotate();
    }

    public @Nullable IStructure structure(StructurePos pos) {
        return this.structure(pos.getX(), pos.getY());
    }

    public @Nullable IStructure structure(int x, int y) {
        IStructure structure = this.structurePrivate(x, y);
        if (structure == null) return null;
        return structure.copy();
    }

    private @Nullable IStructure structurePrivate(StructurePos pos) {
        return this.structure(pos.getX(), pos.getY());
    }

    private @Nullable IStructure structurePrivate(int x, int y) {
        if (x < 0 || y < 0 || x >= size || y >= size) return null;
        return this.structures[this.index(x, y)];
    }

    public void insetRotate(StructurePos pos, EnumStructure.Rotate rotate) {
        this.insetRotate(pos.getX(), pos.getY(), rotate);
    }

    public void insetRotate(int x, int y, EnumStructure.Rotate rotate) {
        IStructure structure = this.structurePrivate(x, y);
        if (structure != null) {
            structure.setRotate(rotate);
        }
    }

    public void insetStructure(IStructure structure) {
        if (structure.type().isMultiChunk()) {
            this.privateInsetMultiChunkStructure((IStructureMultiChunk) structure.copy());
        } else {
            this.privateInsetStructure(structure.copy());
        }
    }

    private void privateInsetStructure(IStructure structure) {
        StructurePos pos = structure.pos();
        if (pos.isOutOfBounds(this.size)) return;
        structure.bindMap(this);
        this.structures[this.index(pos)] = structure;
    }

    private void privateInsetMultiChunkStructure(IStructureMultiChunk structure) {
        StructurePos pos = structure.pos();
        if (pos.isOutOfBounds(this.size)) return;
        structure.bindMap(this);
        this.structures[this.index(pos)] = structure;
        structure.insertSegments(this::privateInsetStructure);
    }

    public void createBaseStructure(StructurePos pos, EnumStructure type, EnumStructure.Rotate rotate) {
        if (pos.isOutOfBounds(this.size)) return;
        IStructure structure = new StructureBase(rotate, type, pos);
        structure.bindMap(this);
        this.structures[this.index(pos)] = structure;
    }

    public void createBaseStructure(int x, int y, EnumStructure type, EnumStructure.Rotate rotate) {
        this.createBaseStructure(new StructurePos(x, y), type, rotate);
    }

    public List<StructurePos> connectedStructures(int x, int y) {
        return this.connectedStructures(new StructurePos(x, y));
    }

    public List<StructurePos> connectedStructures(StructurePos pos) {
        List<StructurePos> ret = new ArrayList<>(4);
        if (pos.isOutOfBounds(this.size)) return ret;
        IStructure structure = this.structurePrivate(pos);
        if (structure == null) return ret;

        for (StructurePos.Face face : StructurePos.Face.values()) {
            if (!structure.canConnect(face)) continue;
            StructurePos offsetPos = pos.offset(face);
            IStructure neighbor = this.structurePrivate(offsetPos);
            if (neighbor != null && neighbor.canConnect(face.getOppose())) {
                ret.add(offsetPos);
            }
        }

        return ret;
    }

    public int size() {
        return this.size;
    }
}
