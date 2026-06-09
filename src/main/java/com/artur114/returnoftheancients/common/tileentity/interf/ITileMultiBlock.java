package com.artur114.returnoftheancients.common.tileentity.interf;

import com.artur114.returnoftheancients.common.tileentity.TileEntityDummy;

public interface ITileMultiBlock {
    void fillDummy();
    void onDummyBroken(TileEntityDummy dummy);
}
