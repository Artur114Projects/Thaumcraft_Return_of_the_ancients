package com.artur.returnoftheancients.ancientworld.system.server;

import com.artur.returnoftheancients.ancientworld.system.base.AncientLayer1;
import com.artur.returnoftheancients.ancientworldlegacy.main.entry.AncientEntry;
import com.artur.returnoftheancients.handlers.TeleportHandler;
import com.artur.returnoftheancients.init.InitDimensions;
import com.artur.returnoftheancients.items.ItemSoulBinder;
import com.artur.returnoftheancients.network.ClientPacketSyncAncientLayer1s;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.*;


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
    public boolean onPlayerLost(EntityPlayerMP player) {
        for (AncientLayer1Server layer1Server : this.ancientLayer1s) {
            if (layer1Server.onPlayerLost(player)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onPlayerElope(EntityPlayerMP player) {
        for (AncientLayer1Server layer1Server : this.ancientLayer1s) {
            if (layer1Server.onPlayerElope(player)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onPlayerInterruptBuild(EntityPlayerMP player) {
        for (AncientLayer1Server layer1Server : this.ancientLayer1s) {
            if (layer1Server.onPlayerInterruptBuild(player)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void intoAncientWorld(EntityPlayerMP player) {
        if (this.hasPlayer(player)) {
            return;
        }

        Tuple<List<EntityPlayerMP>, Map<UUID, String>> compiled = ItemSoulBinder.compileTeam(player);

        List<EntityPlayerMP> players = compiled.getFirst();

        for (EntityPlayerMP playerMP : players) this.teleportToPlatform(playerMP);

        AncientLayer1Server layer1Server = new AncientLayer1Server(players);
        layer1Server.setPlayersState(compiled.getSecond());
        layer1Server.setWorld(this.world);
        this.setFreePos(layer1Server);
        layer1Server.setSize(33);
        layer1Server.constructFinish();

        this.ancientLayer1s.add(layer1Server);
    }

    @Override
    public boolean loadEntity(EntityLiving entity) {
        long sessionId = entity.getEntityData().getCompoundTag("AncientSystemData").getLong("sessionId");
        for (AncientLayer1Server layer1Server : this.ancientLayer1s) {
            if (layer1Server.sessionId == sessionId) {
                return layer1Server.loadEntity(entity);
            }
        }
        return false;
    }

    @Override
    public void onEntityDead(EntityLiving entity) {
        long sessionId = entity.getEntityData().getCompoundTag("AncientSystemData").getLong("sessionId");
        for (AncientLayer1Server layer1Server : this.ancientLayer1s) {
            if (layer1Server.sessionId == sessionId) {
                layer1Server.onEntityDead(entity);
            }
        }
    }

    @Override
    public void unloadEntity(EntityLiving entity) {
        long sessionId = entity.getEntityData().getCompoundTag("AncientSystemData").getLong("sessionId");
        for (AncientLayer1Server layer1Server : this.ancientLayer1s) {
            if (layer1Server.sessionId == sessionId) {
                layer1Server.unloadEntity(entity);
            }
        }
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

    private void setFreePos(AncientLayer1Server layer1Server) {
        int index = 1;
        boolean isFound;
        while (true) {
            isFound = true;
            for (AncientLayer1Server entry : this.ancientLayer1s) {
                if (entry.posIndex() == index) {
                    isFound = false;
                    break;
                }
            }
            if (isFound) {
                break;
            }
            index++;
        }

        layer1Server.setPos(new ChunkPos(512 * (index), 0), index);
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        NBTTagList list = new NBTTagList();
        for (AncientLayer1Server layer1Server : this.ancientLayer1s) {
            if (!layer1Server.isRequestToDelete()) {
                list.appendTag(layer1Server.writeToNBT(new NBTTagCompound()));
            }
        }
        nbt.setTag("ancientLayer1s", list);
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        NBTTagList list = nbt.getTagList("ancientLayer1s", 10);

        for (int i = 0; i != list.tagCount(); i++) {
            AncientLayer1Server layer1Server = new AncientLayer1Server();
            layer1Server.readFromNBT(list.getCompoundTagAt(i));
            layer1Server.setWorld(this.world);
            layer1Server.constructFinish();

            if (layer1Server.isRequestToDelete()) {
                continue;
            }

            this.ancientLayer1s.add(layer1Server);
        }
    }
}
