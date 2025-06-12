package com.artur.returnoftheancients.ancientworld.system.base;

import com.artur.returnoftheancients.ancientworld.map.gen.GenPhase;
import com.artur.returnoftheancients.ancientworld.map.utils.StrPos;
import com.artur.returnoftheancients.ancientworld.map.utils.maps.InteractiveMap;
import com.artur.returnoftheancients.ancientworld.map.utils.structures.IStructure;
import com.artur.returnoftheancients.ancientworld.map.utils.structures.IStructureInteractive;
import com.artur.returnoftheancients.ancientworld.map.utils.structures.IStructureMultiChunk;
import com.artur.returnoftheancients.ancientworld.system.utils.AncientWorldPlayer;
import com.artur.returnoftheancients.handlers.MiscHandler;
import com.artur.returnoftheancients.init.InitDimensions;
import com.artur.returnoftheancients.util.interfaces.IReadFromNBT;
import com.artur.returnoftheancients.util.interfaces.IWriteToNBT;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import java.util.*;

public abstract class AncientLayer1 implements IWriteToNBT, IReadFromNBT, ITickable {
    protected final List<AncientWorldPlayer> players = new ArrayList<>();
    protected long seed = System.nanoTime();
    protected InteractiveMap map = null;
    protected int loadCount;
    protected int size = 17;
    protected ChunkPos pos;
    protected World world;
    private boolean requestToDelete = false;

    public void constructFinish() {}

    public boolean hasPlayer(EntityPlayer player) {
        return this.foundAncientWorldPlayer(player) != null;
    }

    public void requestToDelete() {
        this.requestToDelete = true;
    }

    public boolean isRequestToDelete() {
        return requestToDelete || this.loadCount >= 4;
    }

    public AncientLayer1 setSize(int size) {
        this.size = size;
        return this;
    }

    public AncientLayer1 setWorld(World world) {
        this.world = world;
        return this;
    }

    public AncientLayer1 setPos(ChunkPos pos) {
        this.pos = pos;
        return this;
    }

    public ChunkPos pos() {
        return this.pos;
    }

    protected void addPlayer(AncientWorldPlayer player) {
        this.players.add(player);
        this.onPlayersListChanged();
    }

    protected void removePlayer(AncientWorldPlayer player) {
        this.players.remove(player);
        this.onPlayersListChanged();
    }

    protected void removePlayer(UUID id) {
        this.players.removeIf(player -> player.playerID.equals(id));
        this.onPlayersListChanged();
    }

    protected void removePlayer(EntityPlayer player) {
        this.players.removeIf(playerA -> playerA.player == player);
        this.onPlayersListChanged();
    }

    protected AncientWorldPlayer foundAncientWorldPlayer(EntityPlayer player) {
        for (AncientWorldPlayer ancientPlayer : this.players) {
            if (ancientPlayer.playerID.equals(player.getUniqueID())) {
                ancientPlayer.player = player;
                return ancientPlayer;
            }
        }
        return null;
    }

    protected AncientWorldPlayer foundAncientWorldPlayer(UUID id) {
        for (AncientWorldPlayer ancientPlayer : this.players) {
            if (ancientPlayer.playerID.equals(id)) {
                return ancientPlayer;
            }
        }
        return null;
    }

    protected void createMap() {
        this.map = GenPhase.InstanceAllGenPhases().getMap(this.seed, this.size).toInteractive(this.world);
    }

    protected void onPlayersListChanged() {}

    @Override
    public void update() {
        Map<IStructure, List<AncientWorldPlayer>> listMap = null;

        this.players.removeIf((v) -> v.player.dimension != InitDimensions.ancient_world_dim_id);

        for (AncientWorldPlayer player : this.players) {

            if (!player.isSleep()) {
                StrPos pos = player.calculatePosOnMap(this.pos, this.size);
                if (!pos.isOutOfBounds(this.size)) {
                    IStructure str = this.map.structure(pos);
                    player.setCurrentRoom(str instanceof IStructureMultiChunk.IStructureSegment ? ((IStructureMultiChunk.IStructureSegment) str).parent() : str);
                }

                if (player.currentRoom() == null) {
                    continue;
                }

                if (listMap == null) {
                    listMap = new HashMap<>();
                }

                List<AncientWorldPlayer> playerList = listMap.computeIfAbsent(player.currentRoom(), k -> new LinkedList<>());

                playerList.add(player);
            }
        }

        if (listMap != null) {
            listMap.forEach((k, v) -> {
                if (k instanceof IStructureInteractive) {
                    ((IStructureInteractive) k).update(v);
                }
            });
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagList list = nbt.getTagList("players", 10);
        for (int i = 0; i != list.tagCount(); i++) this.players.add(new AncientWorldPlayer(list.getCompoundTagAt(i).getUniqueId("playerID")));
        this.pos = MiscHandler.chunkPosFromLong(nbt.getLong("pos"));
        this.loadCount = nbt.getInteger("loadCount") + 1;
        this.size = nbt.getInteger("size");
        this.seed = nbt.getLong("seed");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setTag("players", IWriteToNBT.objectsToNBT(this.players));
        nbt.setLong("pos", MiscHandler.chunkPosAsLong(this.pos));
        nbt.setInteger("loadCount", loadCount);
        nbt.setInteger("size", this.size);
        nbt.setLong("seed", this.seed);
        return nbt;
    }
}
