package com.artur.returnoftheancients.ancientworld.map.utils;

public interface IMultiChunkStrForm {
    char[][] rawForm();
    IOffset[] offsets();
    IOffset[] offsets(StrPos center);
    IOffset[] offsets(EnumRotate rotate);
    IOffset[] offsets(StrPos center, EnumRotate rotate);

    interface IOffset {
        EnumFace[] ports();
        StrPos centerPos();
        StrPos globalPos();
        StrPos localPos();
    }
}
