package com.artur114.thaumrota.common.tileentity.interf;

import com.artur114.thaumrota.common.tileentity.TileEntityDummy;

public interface ITileMultiBlock {
    void fillDummy();
    void onDummyBroken(TileEntityDummy dummy);
}
