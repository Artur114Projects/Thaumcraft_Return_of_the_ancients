package com.artur.returnoftheancients.blockprotect.client;

import net.minecraft.util.math.BlockPos;

public class ClientProtectedChunk implements IClientProtectedChunk {
    @Override
    public boolean hasProtect(BlockPos pos) {
        return false;
    }
}
