package com.artur.returnoftheancients.ancientworld.system.server;

import com.artur.returnoftheancients.ancientworld.map.build.AncientLayer1Builder;
import com.artur.returnoftheancients.ancientworld.system.base.AncientLayer1;
import com.artur.returnoftheancients.ancientworld.system.utils.AncientWorldPlayer;
import com.artur.returnoftheancients.events.ServerEventsHandler;
import com.artur.returnoftheancients.generation.portal.base.AncientPortalsProcessor;
import com.artur.returnoftheancients.handlers.MiscHandler;
import com.artur.returnoftheancients.network.ClientPacketSyncAncientLayer1s;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;

import java.util.*;

public class AncientLayer1Server extends AncientLayer1 {
    protected Map<UUID, String> playersState = new HashMap<>();
    protected AncientLayer1Builder builder;
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
    protected void onRequestToDelete() {
        if (this.builder != null) {
            ServerEventsHandler.SLOW_BUILD_MANAGER.finishBuilder(this.builder);
        }
    }

    @Override
    public void constructFinish() {
        if (!this.isSleep()) {
            this.createMap();

            for (AncientWorldPlayer player : this.players) {
                if (!player.isSleep()) {
                    ClientPacketSyncAncientLayer1s.sendCreateLayer((EntityPlayerMP) player.player, this);
                }
            }

            if (!this.isBuild && !this.isBuilding) {
                this.build();
            }
        }
    }

    public void setPlayersState(Map<UUID, String> playersState) {
        this.playersState = playersState;
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
        boolean isSleep = this.isSleep();

        AncientWorldPlayer ancientPlayer = this.foundAncientWorldPlayer(player);

        if (ancientPlayer != null) {
            if (isSleep) {
                this.onWakeUp();
            }

            if (!this.isBuild) {
                ClientPacketSyncAncientLayer1s.sendCreateLayerAndStartBuild(player, this);
            } else {
                ClientPacketSyncAncientLayer1s.sendCreateLayer(player, this);
            }
        }

        return ancientPlayer != null;
    }

    public boolean onPlayerLoginOut(EntityPlayerMP player) {
        AncientWorldPlayer ancientPlayer = this.foundAncientWorldPlayer(player);

        if (ancientPlayer != null) {
            ancientPlayer.player = null;

            if (!this.isBuild) {
                this.updatePlayerState(player, "returnoftheancients.team_state.out_of_game");
            }
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

    public boolean onPlayerInterruptBuild(EntityPlayerMP player) {
        AncientWorldPlayer ancientPlayer = this.foundAncientWorldPlayer(player);

        if (ancientPlayer != null) {
            this.removePlayer(ancientPlayer);
            AncientPortalsProcessor.teleportToOverworld(player);
            this.updatePlayerState(player, "returnoftheancients.team_state.interrupt");
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
        this.builder = null;

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
        this.builder = (AncientLayer1Builder) new AncientLayer1Builder(this.map, this.world, new Random(this.seed), this.pos).addCallBack(this::onBuildFinish);
        ServerEventsHandler.SLOW_BUILD_MANAGER.newBuilder(this.builder);
        this.onBuildStart();
    }

    private void updatePlayerState(EntityPlayerMP player, String newState) {
        String name = this.playersState.get(player.getUniqueID());
        this.playersState.put(player.getUniqueID(), name + "|" + newState);

        for (AncientWorldPlayer playerA : this.players) {
            if (!playerA.isSleep()) {
                ClientPacketSyncAncientLayer1s.sendUpdatePlayersState((EntityPlayerMP) playerA.player, this.playersState);
            }
        }
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

    public NBTTagCompound writeClientCreateNBT(NBTTagCompound nbt) {
        nbt.setLong("pos", MiscHandler.chunkPosAsLong(this.pos));
        nbt.setInteger("posIndex", this.posIndex);
        nbt.setInteger("size", this.size);

        NBTTagList list = new NBTTagList();

        for (String n : this.playersState.values()) {
            list.appendTag(new NBTTagString(n));
        }

        nbt.setTag("playersState", list);

        return nbt;
    }
}
