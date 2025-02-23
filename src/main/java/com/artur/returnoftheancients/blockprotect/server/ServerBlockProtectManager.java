package com.artur.returnoftheancients.blockprotect.server;

import com.artur.returnoftheancients.blockprotect.client.ClientProtectedChunk;
import com.artur.returnoftheancients.capabilities.GenericCapabilityProvider;
import com.artur.returnoftheancients.capabilities.TRACapabilities;
import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class ServerBlockProtectManager {
    public void playerInteractEventLeftClickBlock(PlayerInteractEvent.LeftClickBlock e) {
        if (e.getEntityPlayer().isCreative()) {
            return;
        }

        e.setCanceled(true);
    }

    public void attachCapabilitiesEventChunk(AttachCapabilitiesEvent<Chunk> e) {
        e.addCapability(new ResourceLocation(Referense.MODID, "protected_chunk"), new GenericCapabilityProvider<>(new ServerProtectedChunk(), TRACapabilities.PROTECTED_CHUNK));
    }
}
