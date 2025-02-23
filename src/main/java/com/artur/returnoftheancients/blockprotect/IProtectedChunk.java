package com.artur.returnoftheancients.blockprotect;

import net.minecraft.util.math.BlockPos;

public interface IProtectedChunk {
    boolean isRemote();
    boolean hasProtect(BlockPos pos);
}
