package com.artur.returnoftheancients.util.interfaces;

import net.minecraft.tileentity.TileEntity;

public interface ITileKeep {
    TileEntity getTile();

    default boolean isInvalid() {
        return this.getTile().isInvalid();
    }
}
