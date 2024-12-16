package com.artur.returnoftheancients.generation.generators.portal;

import com.artur.returnoftheancients.generation.generators.portal.base.AncientPortal;
import com.artur.returnoftheancients.generation.generators.portal.base.AncientPortalsProcessor;
import com.artur.returnoftheancients.handlers.HandlerR;
import com.artur.returnoftheancients.tileentity.TileEntityAncientTeleport;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;

public class AncientPortalOpening extends AncientPortal {
    public AncientPortalOpening(TileEntityAncientTeleport tile) {
        super(tile.getWorld().getMinecraftServer(), tile.getWorld().provider.getDimension(), tile.getPos().getX() >> 4, tile.getPos().getZ() >> 4, tile.getPos().getY() - 1, HandlerR.foundMostSmallUniqueIntInList(new ArrayList<>(AncientPortalsProcessor.PORTALS.keySet())));
        build();
    }

    public AncientPortalOpening(MinecraftServer server, NBTTagCompound compound) {
        super(server, compound);
    }

    @Override
    public void build() {
        genAncientPortal();
    }

    @Override
    protected int getPortalTypeID() {
        return 1;
    }
}
