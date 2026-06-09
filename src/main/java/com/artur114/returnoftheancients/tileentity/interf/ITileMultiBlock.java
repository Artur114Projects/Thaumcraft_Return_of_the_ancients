package com.artur114.returnoftheancients.tileentity.interf;

import com.artur114.returnoftheancients.tileentity.TileEntityDummy;

public interface ITileMultiBlock {
    void fillDummy();
    void onDummyBroken(TileEntityDummy dummy);
}
