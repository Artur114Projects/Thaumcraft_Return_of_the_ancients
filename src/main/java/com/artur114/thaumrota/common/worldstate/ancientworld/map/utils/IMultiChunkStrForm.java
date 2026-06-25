package com.artur114.thaumrota.common.worldstate.ancientworld.map.utils;

import com.artur114.bananalib.math.m2d.box.IBox2I;
import com.artur114.bananalib.math.m2d.vec.IVec2I;

public interface IMultiChunkStrForm {
    IBox2I box();
    IVec2I center();
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
