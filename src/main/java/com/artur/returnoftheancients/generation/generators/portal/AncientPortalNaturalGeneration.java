package com.artur.returnoftheancients.generation.generators.portal;

import com.artur.returnoftheancients.generation.generators.portal.base.AncientPortal;
import com.artur.returnoftheancients.generation.generators.portal.base.AncientPortalsProcessor;
import com.artur.returnoftheancients.handlers.HandlerR;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;

public class AncientPortalNaturalGeneration extends AncientPortal {
    public AncientPortalNaturalGeneration(MinecraftServer server, int dimension, int chunkX, int chunkZ, int posY) {
        super(server, dimension, chunkX, chunkZ, posY >= 4 ? posY : server.getWorld(dimension).getSeaLevel(), HandlerR.foundMostSmallUniqueIntInList(new ArrayList<>(AncientPortalsProcessor.PORTALS.keySet())));
        build();
    }

    public AncientPortalNaturalGeneration(MinecraftServer server, NBTTagCompound nbt) {
        super(server, nbt);
    }

    @Override
    public void build() {
        genAncientPortal();
    }

    @Override
    protected int getPortalTypeID() {
        return 0;
    }
}
