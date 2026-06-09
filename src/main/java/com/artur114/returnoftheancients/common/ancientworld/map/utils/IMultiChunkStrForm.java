package com.artur114.returnoftheancients.common.ancientworld.map.utils;

public interface IMultiChunkStrForm {
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
