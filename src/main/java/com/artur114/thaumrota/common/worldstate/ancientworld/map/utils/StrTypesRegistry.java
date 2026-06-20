package com.artur114.thaumrota.common.worldstate.ancientworld.map.utils;

import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.structures.StructureMapBorder;

import java.util.HashMap;
import java.util.Map;

public class StrTypesRegistry {
    private static final Map<String, IStructureType> nameOfTypeMap = new HashMap<>();
    private static final Map<IStructureType, String> typeOfNameMap = new HashMap<>();

    public static String nameOfType(IStructureType type) {
        return typeOfNameMap.get(type);
    }

    public static IStructureType typeFromName(String type) {
        return nameOfTypeMap.get(type);
    }

    static {
        for (EnumStructureType type : EnumStructureType.values()) {
            nameOfTypeMap.put(type.name(), type);
            typeOfNameMap.put(type, type.name());
        }
        for (EnumMultiChunkStrType type : EnumMultiChunkStrType.values()) {
            nameOfTypeMap.put(type.name(), type);
            typeOfNameMap.put(type, type.name());
        }
        nameOfTypeMap.put("MAP_BORDER", StructureMapBorder.MAP_BORDER_TYPE);
        typeOfNameMap.put(StructureMapBorder.MAP_BORDER_TYPE, "MAP_BORDER");
    }
}
