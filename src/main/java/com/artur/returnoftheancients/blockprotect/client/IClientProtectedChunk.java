package com.artur.returnoftheancients.blockprotect.client;

import com.artur.returnoftheancients.blockprotect.IProtectedChunk;

public interface IClientProtectedChunk extends IProtectedChunk {
    default boolean isRemote() {return true;}
}
