package com.artur.returnoftheancients.ancientworld.map.utils;

import com.artur.returnoftheancients.ancientworld.map.utils.structures.*;
import com.artur.returnoftheancients.util.interfaces.Function3;

import java.util.*;

public enum EnumMultiChunkStrType implements IStructureType {
    WATER_ROOM("ancient_water_room", new StructureWaterRoom.Form(), ((rotate, strPos, iStructureType) -> new StructureWaterRoom(strPos))),
    ENTRY("ancient_entry", new StructureEntry.Form(), ((rotate, strPos, iStructureType) -> new StructureEntry(strPos))),
    BOSS("ancient_boss", new StructureBoss.Form(), ((rotate, strPos, iStructureType) -> new StructureBoss(strPos)));

    private final Function3<EnumRotate, StrPos, IStructureType, IStructureMultiChunk> creator;
    private final IMultiChunkStrForm form;
    private final String id;
    EnumMultiChunkStrType(String id, IMultiChunkStrForm form, Function3<EnumRotate, StrPos, IStructureType, IStructureMultiChunk> creator) {
        this.creator = creator;
        this.form = form;
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

    public IMultiChunkStrForm form() {
        return this.form;
    }
}
