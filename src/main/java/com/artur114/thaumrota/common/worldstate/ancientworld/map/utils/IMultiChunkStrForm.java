package com.artur114.thaumrota.common.worldstate.ancientworld.map.utils;

import com.artur114.bananalib.math.m2d.box.IBox2I;
import com.artur114.bananalib.math.m2d.vec.IVec2I;

public interface IMultiChunkStrForm {
    IVec2I center();
    IBox2I box(EnumRotate rot);
    IOffset[] offsets();
    IOffset[] offsets(StrPos center);
    IOffset[] offsets(EnumRotate rotate);
    IOffset[] offsets(StrPos center, EnumRotate rotate);

    interface IOffset {
        EnumFace[] voidCollides();
        EnumFace[] ports();
        StrPos centerPos();
        StrPos globalPos();
        StrPos localPos();
    }
}
