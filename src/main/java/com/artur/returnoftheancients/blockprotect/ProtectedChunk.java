package com.artur.returnoftheancients.blockprotect;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

public abstract class ProtectedChunk implements IProtectedChunk {
    protected final IExtendedProtectStorage[] storages = new IExtendedProtectStorage[16];
    protected int initStoragesCount = 0;
    protected final int dimension;
    protected final ChunkPos pos;

    public ProtectedChunk(ChunkPos pos, int dimension) {
        this.dimension = dimension;
        this.pos = pos;
    }

    @Override
    public boolean hasProtect(int x, int y, int z) {
        if (this.isEmpty()) return false;
        IExtendedProtectStorage storage = this.storages[y >> 4];
        if (storage == null) return false;

        return storage.hasProtect(x, y, z);
    }

    @Override
    public boolean hasProtect(BlockPos pos) {
        return this.hasProtect(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public boolean isEmpty() {
        return this.initStoragesCount == 0;
    }
}
