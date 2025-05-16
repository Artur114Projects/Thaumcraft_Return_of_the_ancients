package com.artur.returnoftheancients.ancientworld.map.utils.maps;

import com.artur.returnoftheancients.ancientworld.map.utils.EnumStructure;
import com.artur.returnoftheancients.ancientworld.map.utils.StructurePos;
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

    protected int index(StructurePos pos) {
        return index(pos.getX(), pos.getY());
    }

    public abstract @Nullable EnumStructure structureType(StructurePos pos);
    public abstract @Nullable EnumStructure structureType(int x, int y);
    public abstract @Nullable EnumStructure.Rotate structureRotate(StructurePos pos);
    public abstract @Nullable EnumStructure.Rotate structureRotate(int x, int y);
    public abstract @Nullable IStructure structure(StructurePos pos);
    public abstract @Nullable IStructure structure(int x, int y);
    public abstract void insetRotate(StructurePos pos, EnumStructure.Rotate rotate);
    public abstract void insetRotate(int x, int y, EnumStructure.Rotate rotate);
    public abstract void insetStructure(IStructure structure);

    public List<StructurePos> connectedStructures(int x, int y) {
        return this.connectedStructures(new StructurePos(x, y));
    }

    public List<StructurePos> connectedStructures(StructurePos pos) {
        List<StructurePos> ret = new ArrayList<>(4);
        if (pos.isOutOfBounds(this.size)) return ret;
        IStructure structure = this.structure(pos);
        if (structure == null) return ret;

        if (structure instanceof IStructureMultiChunk) {
            for (IStructure segment : ((IStructureMultiChunk) structure).segmentsWithPorts()) {
                ret.addAll(connectedStructures(segment.pos()));
            }
            return ret;
        }

        for (StructurePos.Face face : StructurePos.Face.values()) {
            if (!structure.canConnect(face)) continue;
            StructurePos offsetPos = pos.offset(face);
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
