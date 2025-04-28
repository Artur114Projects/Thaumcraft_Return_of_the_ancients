package com.artur.returnoftheancients.ancientworld.map.utils;

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

    public void insetStructure(StructurePos pos, IStructure structure) {
        this.insetStructure(pos.getX(), pos.getY(), structure);
    }

    public void insetStructure(int x, int y, IStructure structure) {
        if (x < 0 || y < 0 || x >= size || y >= size) return;
        this.structures[this.index(x, y)] = structure.copy();
    }

    public List<StructurePos> connectedStructures(int x, int y) {
        return null;
    }

    public List<StructurePos> connectedStructures(StructurePos pos) {
        List<StructurePos> ret = new ArrayList<>(4);
        if (pos.isOutOfBounds(size)) return ret;
        IStructure structure = this.structurePrivate(pos);
        if (structure == null) return ret;

        for (StructurePos.Face face : StructurePos.Face.values()) {
            if (!structure.canConnect(face)) continue;
            StructurePos offsetPos = pos.offset(face);
            IStructure neighbor = this.structurePrivate(pos);
            if (neighbor != null && neighbor.canConnect(face.getOppose())) {
                ret.add(offsetPos);
            }
        }

        return ret;
    }
}
