package com.artur114.thaumrota.common.worldstate.ancientworld.system.server;

import com.artur114.bananalib.mc.BananaMC;
import com.artur114.thaumrota.common.worldstate.ancientworld.map.build.AncientLayer1Builder;
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.StrPos;
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.structures.IStructure;
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.structures.IStructureEntityManager;
import com.artur114.thaumrota.common.worldstate.ancientworld.system.base.AncientLayer1;
import com.artur114.thaumrota.common.worldstate.ancientworld.system.utils.AncientWorldPlayer;
import com.artur114.thaumrota.common.event.CommonEventsHandler;
import com.artur114.thaumrota.common.generation.portal.base.AncientPortalsProcessor;
import com.artur114.thaumrota.common.network.ClientPacketSyncAncientLayer1s;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.List;

public class AncientLayer1Server extends AncientLayer1 {
    private static final Logger log = LogManager.getLogger("ThaumRotA/AncientWorld");
    protected Map<UUID, String> playersState = new HashMap<>();
    protected List<Runnable> delayedTasks = new ArrayList<>();
    protected long sessionId = System.nanoTime();
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
    protected void onDeleting() {
        if (this.builder != null) {
            CommonEventsHandler.SLOW_BUILD_MANAGER.finishBuilder(this.builder);
        }
    }

    @Override
    public void constructFinish() {
        this.createMap();

        if (!this.isSleep()) {
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

    @Override
    public void update() {
        super.update();

        this.checkNeedSyncMap();
    }

    @Override
    protected void createMap() {
        super.createMap();

        this.map.foundStructures(IStructureEntityManager.class).forEach((s) -> s.bindSessionId(this.sessionId));
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
                this.updatePlayerState(player, "thaumrota.team_state.out_of_game");
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
            this.updatePlayerState(player, "thaumrota.team_state.interrupt");
        }

        return ancientPlayer != null;
    }

    public boolean loadEntity(EntityLiving entity) {
        if (this.isSleep()) {
            this.delayedTasks.add(() -> {if (!this.loadEntity(entity)) entity.setDead();}); return true;
        }

        StrPos pos = new StrPos(entity.getEntityData().getCompoundTag("AncientSystemData").getLong("pos"));
        IStructure structure = this.map.structure(pos);

        if (structure instanceof IStructureEntityManager) {
            return ((IStructureEntityManager) structure).loadEntity(entity);
        }

        return false;
    }

    public void unloadEntity(EntityLiving entity) {
        if (this.isSleep()) {
            this.delayedTasks.add(() -> this.unloadEntity(entity)); return;
        }

        StrPos pos = new StrPos(entity.getEntityData().getCompoundTag("AncientSystemData").getLong("pos"));
        IStructure structure = this.map.structure(pos);

        if (structure instanceof IStructureEntityManager) {
            ((IStructureEntityManager) structure).unloadEntity(entity);
        }
    }

    public void onEntityDead(EntityLiving entity) {
        if (this.isSleep()) {
            this.delayedTasks.add(() -> this.onEntityDead(entity)); return;
        }

        StrPos pos = new StrPos(entity.getEntityData().getCompoundTag("AncientSystemData").getLong("pos"));
        IStructure structure = this.map.structure(pos);

        if (structure instanceof IStructureEntityManager) {
            ((IStructureEntityManager) structure).onEntityDead(entity);
        }
    }

    protected void onWakeUp() {
        this.loadCount = 0;
        if (!this.isBuild && !this.isBuilding) {
            this.build();
        }

        for (Runnable run : this.delayedTasks) {
            run.run();
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

        log.info("Finished sector build on pos {}", this.pos);
    }

    protected void onBuildStart() {
        this.isBuilding = true;

        for (AncientWorldPlayer player : this.players) {
            if (!player.isSleep()) {
                ClientPacketSyncAncientLayer1s.sendBuildState((EntityPlayerMP) player.player, true);
            }
        }

        log.info("Runed sector build on pos {}", this.pos);
    }

    protected void build() {
        this.builder = (AncientLayer1Builder) new AncientLayer1Builder(this.map, this.world, new Random(this.seed), this.pos).addCallBack(this::onBuildFinish);
        CommonEventsHandler.SLOW_BUILD_MANAGER.newBuilder(this.builder);
        this.onBuildStart();
    }

    private void updatePlayerState(EntityPlayerMP player, String newState) {
        this.playersState.compute(player.getUniqueID(), (k, name) -> name + "|" + newState);

        for (AncientWorldPlayer playerA : this.players) {
            if (!playerA.isSleep()) {
                ClientPacketSyncAncientLayer1s.sendUpdatePlayersState((EntityPlayerMP) playerA.player, this.playersState);
            }
        }
    }

    private void checkNeedSyncMap() {
        if (this.map != null && this.map.hasStructuresSyncData()) {
            NBTTagCompound data = this.map.structuresSyncData();

            for (AncientWorldPlayer player : this.players) {
                if (!player.isSleep()) {
                    ClientPacketSyncAncientLayer1s.sendSyncStructures((EntityPlayerMP) player.player, data);
                }
            }
        }
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setLong("sessionId", this.sessionId);
        nbt.setBoolean("isBuild", this.isBuild);
        return super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        this.sessionId = nbt.getLong("sessionId");
        this.isBuild = nbt.getBoolean("isBuild");
        super.readFromNBT(nbt);
    }

    public NBTTagCompound writeClientCreateNBT(NBTTagCompound nbt) {
        nbt.setTag("map", this.map.writeToNBT(new NBTTagCompound()));
        nbt.setLong("pos", BananaMC.chunkPosAsLong(this.pos));
        nbt.setInteger("posIndex", this.posIndex);
        nbt.setInteger("size", this.size);
        nbt.setLong("seed", this.seed);

        NBTTagList list = new NBTTagList();

        for (String n : this.playersState.values()) {
            list.appendTag(new NBTTagString(n));
        }

        nbt.setTag("playersState", list);

        return nbt;
    }
}
