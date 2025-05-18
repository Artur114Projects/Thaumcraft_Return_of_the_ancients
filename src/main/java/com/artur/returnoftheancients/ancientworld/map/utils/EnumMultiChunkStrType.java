package com.artur.returnoftheancients.ancientworld.map.utils;

import com.artur.returnoftheancients.ancientworld.map.utils.structures.IStructure;
import com.artur.returnoftheancients.ancientworld.map.utils.structures.IStructureMultiChunk;
import com.artur.returnoftheancients.ancientworld.map.utils.structures.StructureBoss;
import com.artur.returnoftheancients.ancientworld.map.utils.structures.StructureEntry;
import com.artur.returnoftheancients.util.interfaces.Function3;

import java.util.*;

public enum EnumMultiChunkStrType implements IStructureType {
    ENTRY("ancient_entry", ((rotate, strPos, iStructureType) -> new StructureEntry(rotate, strPos))),
    BOSS("ancient_boss", ((rotate, strPos, iStructureType) -> new StructureBoss(rotate, strPos)));

    private final Function3<EnumRotate, StrPos, IStructureType, IStructureMultiChunk> creator;
    private final String id;
    EnumMultiChunkStrType(String id, Function3<EnumRotate, StrPos, IStructureType, IStructureMultiChunk> creator) {
        this.creator = creator;
        this.id = id;
    }

    @Override
    public IStructure create(EnumRotate rotate, StrPos pos) {
        return this.creator.apply(rotate, pos, this);
    }

    @Override
    public String stringId(EnumRotate rotate) {
        return this.id.endsWith("-") ? this.id + rotate.id : this.id;
    }
}
