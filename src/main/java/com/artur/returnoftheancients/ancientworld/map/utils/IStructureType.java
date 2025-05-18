package com.artur.returnoftheancients.ancientworld.map.utils;


import com.artur.returnoftheancients.ancientworld.map.utils.structures.IStructure;

import java.util.Collection;
import java.util.Set;

public interface IStructureType {
    IStructure create(EnumRotate rotate, StrPos pos);
    String stringId(EnumRotate rotate);
}
