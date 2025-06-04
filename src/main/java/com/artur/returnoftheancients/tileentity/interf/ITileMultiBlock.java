package com.artur.returnoftheancients.tileentity.interf;

import com.artur.returnoftheancients.tileentity.TileEntityDummy;
import net.minecraft.util.math.BlockPos;

public interface ITileMultiBlock {
    void fillDummy();
    void onDummyBroken(TileEntityDummy dummy);
}
