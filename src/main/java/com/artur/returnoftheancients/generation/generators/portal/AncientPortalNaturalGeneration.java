package com.artur.returnoftheancients.generation.generators.portal;

import com.artur.returnoftheancients.generation.generators.portal.base.AncientPortal;
import com.artur.returnoftheancients.generation.generators.portal.base.AncientPortalsProcessor;
import com.artur.returnoftheancients.handlers.HandlerR;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;

public class AncientPortalNaturalGeneration extends AncientPortal {
    public AncientPortalNaturalGeneration(MinecraftServer server, int dimension, int chunkX, int chunkZ, int posY) {
        super(server, dimension, chunkX, chunkZ, posY, HandlerR.foundMostSmallUniqueIntInList(new ArrayList<>(AncientPortalsProcessor.PORTALS.keySet())));
    }

    @Override
    public void build() {
        HandlerR.genAncientPortal(world, chunkX, chunkZ, false);
    }
}
