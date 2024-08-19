package com.artur.returnoftheancients.ancientworldgeneration.util;

import com.artur.returnoftheancients.ancientworldgeneration.util.interfaces.ITeamSet;
import com.artur.returnoftheancients.handlers.HandlerR;
import com.artur.returnoftheancients.init.InitDimensions;
import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import java.util.*;

public class Team {
    public static void updateS() {
        uniqueIds.clear();
        for (Team team : teams) {
            for (UUID id : team.uuids) {
                if (!uniqueIds.add(id)) {
                    team.requestToDelete();
                }
            }
        }
        uniqueIds.clear();
    }

    public static void clear() {
        teams.clear();
    }
    private static final Set<UUID> uniqueIds = new HashSet<>();
    private static final LinkedList<Team> teams = new LinkedList<>();

    private boolean delete = false;
    private final LinkedList<UUID> toRemove = new LinkedList<>();
    private final LinkedList<UUID> uuids = new LinkedList<>();
    private final HashMap<UUID, EntityPlayerMP> players = new HashMap<>();
    public Team(ItemStack stack, World world) {
        NBTTagCompound nbt = stack.getOrCreateSubCompound(Referense.MODID);
        NBTTagCompound list = nbt.getCompoundTag("players");
        List<String> keys = HandlerR.uuidKeySetToList(list.getKeySet());
        MinecraftServer server = world.getMinecraftServer();
        for (String key : keys) {
            if (server != null) {
                UUID id = Objects.requireNonNull(list.getUniqueId(key));
                Entity entity = server.getEntityFromUuid(id);
                if (entity instanceof EntityPlayerMP && entity.dimension != InitDimensions.ancient_world_dim_id) {
                    uuids.add(id);
                    players.put(id, (EntityPlayerMP) entity);
                }
            }
        }
        System.out.println(this);
        teams.add(this);
    }

    public Team(List<EntityPlayerMP> players) {
        for (EntityPlayerMP playerMP : players) {
            this.uuids.add(playerMP.getUniqueID());
            this.players.put(playerMP.getUniqueID(), playerMP);
        }
        teams.add(this);
    }

    public Team(EntityPlayerMP... players) {
        for (EntityPlayerMP playerMP : players) {
            this.uuids.add(playerMP.getUniqueID());
            this.players.put(playerMP.getUniqueID(), playerMP);
        }
        teams.add(this);
    }

    public Team(NBTTagCompound nbt) {
        boolean error = false;
        if (!nbt.hasKey("p0Most") || !nbt.hasKey("p0Least")) {
            System.out.println("Team.class, transferred incorrect NBTTag EC:0");
            error = true;
        }
        for (int i = 0; nbt.hasKey("p" + i); i++) {
            uuids.add(nbt.getUniqueId("p" + i));
        }
        if (!error) teams.add(this);
        if (error) requestToDelete();
    }

    public boolean initialise(EntityPlayerMP player) {
        boolean flag = false;
        UUID playerId = player.getUniqueID();
        for (UUID id : uuids) {
            if (id.equals(playerId)) {
                flag = true;
                break;
            }
        }
        if (flag) {
            players.put(player.getUniqueID(), player);
        }
        return flag;
    }

    public boolean kill(UUID uuid) {
        players.remove(uuid);
        boolean flag = uuids.remove(uuid);
        if (uuids.isEmpty()) requestToDelete();
        return flag;
    }

    public EntityPlayerMP get(UUID uuid) {
        return players.get(uuid);
    }

    public EntityPlayerMP[] getAll() {
        return players.values().toArray(new EntityPlayerMP[0]);
    }

    public boolean contains(UUID id) {
        return uuids.contains(id);
    }

    public boolean isEmpty() {
        return uuids.isEmpty();
    }

    public boolean isActive() {
        return !players.isEmpty();
    }

    public void setToAll(ITeamSet set) {
        players.forEach((key, value) -> {
            set.set(value);
        });
    }

    public void delete() {
        teams.remove(this);
    }

    public void update() {
        toRemove.clear();

        players.forEach((key, value) -> {
            if (value == null || value.world.provider.getDimension() != InitDimensions.ancient_world_dim_id) {
                toRemove.add(key);
            }
        });

        if (!toRemove.isEmpty()) {
            for (UUID id : toRemove) {
                players.remove(id);
            }
        }

        toRemove.clear();
    }

    public NBTTagCompound toNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        for (int i = 0; i != uuids.size(); i++) {
            nbt.setUniqueId("p" + i, uuids.get(i));
        }
        return nbt;
    }

    public boolean isRequestDelete() {
        return delete;
    }

    public void requestToDelete() {
        this.delete = true;
    }

    @Override
    public String toString() {
        if (players.isEmpty()) {
            return "";
        }
        StringBuilder res = new StringBuilder("{");
        players.forEach((key, value) -> res.append(value.getName()).append(", "));
        res.delete(res.length() - 2, res.length());
        res.append('}');
        return res.toString();
    }
}
