package com.artur.returnoftheancients.generation.portal;

import com.artur.returnoftheancients.generation.portal.base.AncientPortal;
import com.artur.returnoftheancients.generation.portal.base.AncientPortalsProcessor;
import com.artur.returnoftheancients.tileentity.TileEntityAncientTeleport;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.jetbrains.annotations.Nullable;

public class AncientPortalOpening extends AncientPortal {

    @Nullable
    private TileEntityAncientTeleport tile = null;
    private final BlockPos tilePos;

    private byte updateT = 0;


    public AncientPortalOpening(TileEntityAncientTeleport tile) {
        super(tile.getWorld().getMinecraftServer(), tile.getWorld().provider.getDimension(), tile.getPos().getX() >> 4, tile.getPos().getZ() >> 4, tile.getPos().getY() - 1, AncientPortalsProcessor.getFreeId());
        this.tilePos = tile.getPos();
        this.tile = tile;
        this.build();
    }

    public AncientPortalOpening(MinecraftServer server, NBTTagCompound compound) {
        super(server, compound);
        this.tilePos = BlockPos.fromLong(compound.getLong("TilePos"));
        TileEntity tileRaw = world.getTileEntity(tilePos);
        if (tileRaw instanceof TileEntityAncientTeleport) {
            tile = (TileEntityAncientTeleport) tileRaw;
        }
    }

    @Override
    public void update(TickEvent.ServerTickEvent e) {
        super.update(e);
        if (updateT >= 2) {
            updateT = 0;
            if (tile == null && !isExploded() && !isExplodes()) {
                explosion();
                return;
            }
            TileEntity tileRaw = world.getTileEntity(tilePos);
            if (tileRaw instanceof TileEntityAncientTeleport) {
                tile = (TileEntityAncientTeleport) tileRaw;
            } else {
                tile = null;
            }
        }
        updateT++;
    }

    @Override
    public @Nullable NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        if (nbt == null) {
            return null;
        }
        if (tile != null) {
            nbt.setLong("TilePos", tile.getPos().toLong());
        }
        return nbt;
    }

    @Override
    public void build() {
        this.genAncientPortal();
        this.setGenerated();
    }

    @Override
    protected int getPortalTypeID() {
        return 1;
    }
}
