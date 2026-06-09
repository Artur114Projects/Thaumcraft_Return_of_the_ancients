package com.artur114.returnoftheancients.common.ancientworld.map.utils;


import com.artur114.returnoftheancients.common.ancientworld.map.utils.structures.IStructure;

public interface IStructureType {
    IStructure create(EnumRotate rotate, StrPos pos);
    String stringId(EnumRotate rotate);
}
