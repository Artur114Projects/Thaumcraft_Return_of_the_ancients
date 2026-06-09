package com.artur114.returnoftheancients.ancientworld.map.utils;


import com.artur114.returnoftheancients.ancientworld.map.utils.structures.IStructure;

public interface IStructureType {
    IStructure create(EnumRotate rotate, StrPos pos);
    String stringId(EnumRotate rotate);
}
