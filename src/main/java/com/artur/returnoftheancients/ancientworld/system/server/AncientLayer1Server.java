package com.artur.returnoftheancients.ancientworld.system.server;

import com.artur.returnoftheancients.ancientworld.map.build.AncientLayer1Builder;
import com.artur.returnoftheancients.ancientworld.system.base.AncientLayer1;
import com.artur.returnoftheancients.ancientworld.system.utils.AncientWorldPlayer;
import com.artur.returnoftheancients.events.ServerEventsHandler;
import com.artur.returnoftheancients.generation.portal.base.AncientPortalsProcessor;
import com.artur.returnoftheancients.network.ClientPacketSyncAncientLayer1s;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AncientLayer1Server extends AncientLayer1 {
    protected boolean isBuilding = false;
    protected boolean isBuild = false;

    public AncientLayer1Server() {}

    public AncientLayer1Server(List<EntityPlayerMP> players) {
        for (EntityPlayerMP player : players) {
            this.addPlayer(new AncientWorldPlayer(player));
        }
    }

    public AncientLayer1Server(EntityPlayerMP player) {
        this(Collections.singletonList(player));
    }

    @Override
    protected void onPlayersListChanged() {
        if (this.players.isEmpty()) {
            this.requestToDelete();
        }
    }

    @Override
    public void constructFinish() {
        if (!this.isSleep()) {
            this.createMap();

            for (AncientWorldPlayer player : this.players) {
                if (!player.isSleep()) {
                    ClientPacketSyncAncientLayer1s.sendCreateLayer((EntityPlayerMP) player.player, this.pos, this.size);
                }
            }

            if (!this.isBuild && !this.isBuilding) {
                this.build();
            }
        }
    }

    public boolean isSleep() {
        for (AncientWorldPlayer player : this.players) {
            if (!player.isSleep()) {
                return false;
            }
        }
        return true;
    }

    public boolean onPlayerLoginIn(EntityPlayerMP player) {
        AncientWorldPlayer ancientPlayer = this.foundAncientWorldPlayer(player);

        if (ancientPlayer != null) {
            if (this.isSleep()) {
                this.onWakeUp();
            }

            ancientPlayer.player = player;

            if (!this.isBuild) {
                ClientPacketSyncAncientLayer1s.sendCreateLayerAndStartBuild(player, this.pos, this.size);
            } else {
                ClientPacketSyncAncientLayer1s.sendCreateLayer(player, this.pos, this.size);
            }
        }

        return ancientPlayer != null;
    }

    public boolean onPlayerLoginOut(EntityPlayerMP player) {
        AncientWorldPlayer ancientPlayer = this.foundAncientWorldPlayer(player);

        if (ancientPlayer != null) {
            ancientPlayer.player = null;
        }

        return ancientPlayer != null;
    }

    public boolean onPlayerLost(EntityPlayerMP player) {
        AncientWorldPlayer ancientPlayer = this.foundAncientWorldPlayer(player);

        if (ancientPlayer != null) {
            ancientPlayer.onPlayerLost();
            this.removePlayer(ancientPlayer);
        }

        return ancientPlayer != null;
    }

    public boolean onPlayerElope(EntityPlayerMP player) {
        AncientWorldPlayer ancientPlayer = this.foundAncientWorldPlayer(player);

        if (ancientPlayer != null) {
            ancientPlayer.onPlayerElope();
            this.removePlayer(ancientPlayer);
        }

        return ancientPlayer != null;
    }

    protected void onWakeUp() {
        this.createMap();
        if (!this.isBuild && !this.isBuilding) {
            this.build();
        }
    }

    protected void onBuildFinish() {
        this.isBuilding = false;
        this.isBuild = true;

        for (AncientWorldPlayer player : this.players) {
            if (!player.isSleep()) {
                ClientPacketSyncAncientLayer1s.sendBuildState((EntityPlayerMP) player.player, false);
            }
        }

        for (AncientWorldPlayer player : this.players) {
            player.teleportToChunk(this.pos, 250);
        }

        System.out.println("AncientLayer1 finish build on pos:" + this.pos);
    }

    protected void onBuildStart() {
        this.isBuilding = true;

        for (AncientWorldPlayer player : this.players) {
            if (!player.isSleep()) {
                ClientPacketSyncAncientLayer1s.sendBuildState((EntityPlayerMP) player.player, true);
            }
        }

        System.out.println("AncientLayer1 started build on pos:" + this.pos);
    }

    protected void build() {
        ServerEventsHandler.SLOW_BUILD_MANAGER.newBuilder(new AncientLayer1Builder(this.map, this.world, new Random(this.seed), this.pos).addCallBack(this::onBuildFinish));
        this.onBuildStart();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("isBuild", this.isBuild);
        return super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        this.isBuild = nbt.getBoolean("isBuild");
        super.readFromNBT(nbt);
    }
}
