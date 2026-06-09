package com.artur114.returnoftheancients.util.interfaces;

import net.minecraft.tileentity.TileEntity;

public interface IHasTileEntity<T extends TileEntity> {
    Class<T> tileEntityClass();
}
