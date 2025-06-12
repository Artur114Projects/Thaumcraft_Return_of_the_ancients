package com.artur.returnoftheancients.ancientworld.system.server;

import com.artur.returnoftheancients.ancientworld.system.base.AncientLayer1;
import com.artur.returnoftheancients.handlers.TeleportHandler;
import com.artur.returnoftheancients.init.InitDimensions;
import com.artur.returnoftheancients.network.ClientPacketSyncAncientLayer1s;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ServerAncientLayer1Manager implements IServerAncientLayer1Manager {
    private final List<AncientLayer1Server> ancientLayer1s = new ArrayList<>();
    private final World world;

    public ServerAncientLayer1Manager(World world) {
        this.world = world;
    }

    @Override
    public void worldTick() {
        Iterator<AncientLayer1Server> iterator = ancientLayer1s.iterator();

        while (iterator.hasNext()) {
            AncientLayer1Server layer1Server = iterator.next();

            layer1Server.update();

            if (layer1Server.isRequestToDelete()) {
                iterator.remove();
            }
        }
    }

    @Override
    public void onPlayerLoginIn(EntityPlayerMP player) {
        for (AncientLayer1Server layer1Server : this.ancientLayer1s) {
            if (layer1Server.onPlayerLoginIn(player)) {
                return;
            }
        }
    }

    @Override
    public void onPlayerLoginOut(EntityPlayerMP player) {
        for (AncientLayer1Server layer1Server : this.ancientLayer1s) {
            if (layer1Server.onPlayerLoginOut(player)) {
                return;
            }
        }

    }

    @Override
    public void onPlayerLost(EntityPlayerMP player) {
        for (AncientLayer1Server layer1Server : this.ancientLayer1s) {
            if (layer1Server.onPlayerLost(player)) {
                return;
            }
        }
    }

    @Override
    public void onPlayerElope(EntityPlayerMP player) {
        for (AncientLayer1Server layer1Server : this.ancientLayer1s) {
            if (layer1Server.onPlayerElope(player)) {
                return;
            }
        }
    }

    @Override
    public void intoAncientWorld(EntityPlayerMP player) {
        if (this.hasPlayer(player)) {
            return;
        }

        this.teleportToPlatform(player);

        AncientLayer1Server layer1Server = new AncientLayer1Server(player);
        layer1Server.setWorld(this.world);
        layer1Server.setPos(new ChunkPos(512 * (this.ancientLayer1s.size() + 1), 0));
        layer1Server.setSize(17);
        layer1Server.constructFinish();

        this.ancientLayer1s.add(layer1Server);
    }

    private boolean hasPlayer(EntityPlayerMP player) {
        for (AncientLayer1 layer1 : this.ancientLayer1s) {
            if (layer1.hasPlayer(player)) {
                return true;
            }
        }
        return false;
    }

    private void teleportToPlatform(EntityPlayerMP player) {
        TeleportHandler.teleportToDimension(player, InitDimensions.ancient_world_dim_id, 0, 244, 0);
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return new NBTTagCompound();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {

    }
}
