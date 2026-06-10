package com.artur114.thaumrota.common.util.interfaces;

import net.minecraft.tileentity.TileEntity;

public interface IHasTileEntity<T extends TileEntity> {
    Class<T> tileEntityClass();
}
