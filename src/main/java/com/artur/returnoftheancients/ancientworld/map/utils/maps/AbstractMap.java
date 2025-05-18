package com.artur.returnoftheancients.ancientworld.map.utils.maps;

import com.artur.returnoftheancients.ancientworld.map.utils.*;
import com.artur.returnoftheancients.ancientworld.map.utils.structures.IStructure;
import com.artur.returnoftheancients.ancientworld.map.utils.structures.IStructureMultiChunk;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMap {
    protected final IStructure[] structures;
    protected final int size;

    public AbstractMap(int size) {
        this.structures = new IStructure[size * size];
        this.size = size;
    }

    protected int index(int x, int y) {
        return x + y * this.size;
    }

    protected int index(StrPos pos) {
        return index(pos.getX(), pos.getY());
    }

    public abstract @Nullable IStructureType structureType(StrPos pos);
    public abstract @Nullable IStructureType structureType(int x, int y);
    public abstract @Nullable EnumRotate structureRotate(StrPos pos);
    public abstract @Nullable EnumRotate structureRotate(int x, int y);
    public abstract @Nullable IStructure structure(StrPos pos);
    public abstract @Nullable IStructure structure(int x, int y);
    public abstract void insetRotate(StrPos pos, EnumRotate rotate);
    public abstract void insetRotate(int x, int y, EnumRotate rotate);
    public abstract void insetStructure(IStructure structure);

    public List<StrPos> connectedStructures(int x, int y) {
        return this.connectedStructures(new StrPos(x, y));
    }

    public List<StrPos> connectedStructures(StrPos pos) {
        List<StrPos> ret = new ArrayList<>(4);
        if (pos.isOutOfBounds(this.size)) return ret;
        IStructure structure = this.structure(pos);
        if (structure == null) return ret;

        if (structure instanceof IStructureMultiChunk) {
            for (IStructure segment : ((IStructureMultiChunk) structure).segmentsWithPorts()) {
                ret.addAll(connectedStructures(segment.pos()));
            }
            return ret;
        }

        for (EnumFace face : EnumFace.values()) {
            if (!structure.canConnect(face)) continue;
            StrPos offsetPos = pos.offset(face);
            IStructure neighbor = this.structure(offsetPos);
            if (neighbor != null && neighbor.canConnect(face.getOppose())) {
                ret.add(offsetPos);
            }
        }

        return ret;
    }

    public int size() {
        return this.size;
    }

    public int area() {
        return this.size * this.size;
    }
}
