package com.artur.returnoftheancients.util;

import com.artur.returnoftheancients.util.interfaces.ITileKeep;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TileKeeper<T extends ITileKeep> {
    private final Class<T> tileClass;
    private final BlockPos pos;
    private final World world;
    private T tile;

    public TileKeeper(T tile, Class<T> tileClass) {
        this.world = tile.getTile().getWorld();
        this.pos = tile.getTile().getPos();
        this.tileClass = tileClass;
        this.tile = tile;
    }

    public T getTile() {
        if (!tile.isInvalid()) {
            return tile;
        }

        TileEntity tileRaw = world.getTileEntity(pos);

        if (tileClass.isInstance(tileRaw)) {
            tile = tileClass.cast(tileRaw);
            return tile;
        }

        return null;
    }

    @Override
    public int hashCode() {
        return pos.hashCode() + tileClass.hashCode();
    }

    public boolean isLoaded() {
        return world.isBlockLoaded(pos);
    }

    public static <T extends ITileKeep> Set<TileKeeper<T>> castSet(Set<T> input, Class<T> tClass) {
        Set<TileKeeper<T>> ret = new HashSet<>();
        for (T t : input) {
            ret.add(new TileKeeper<>(t, tClass));
        }
        return ret;
    }
}