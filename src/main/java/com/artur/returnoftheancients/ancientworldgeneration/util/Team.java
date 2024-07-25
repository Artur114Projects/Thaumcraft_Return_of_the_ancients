package com.artur.returnoftheancients.ancientworldgeneration.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.*;

public class Team {
    public final long ID = new Random().nextLong();
    private final HashMap<UUID, EntityPlayerMP> players = new HashMap<>();;
    public Team(List<EntityPlayerMP> players) {
        for (EntityPlayerMP playerMP : players) {
            this.players.put(playerMP.getUniqueID(), playerMP);
            playerMP.getEntityData().setLong("TeamID", ID);
        }
    }

    public Team(EntityPlayerMP... players) {
        for (EntityPlayerMP playerMP : players) {
            this.players.put(playerMP.getUniqueID(), playerMP);
            playerMP.getEntityData().setLong("TeamID", ID);
        }
    }

    public Team(NBTTagCompound nbt) {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (!nbt.hasKey("p0")) throw new RuntimeException("Team.class, transferred incorrect NBTTag value:0");
        for (int i = 0; nbt.hasKey("p" + i); i++) {
            Entity e = server.getEntityFromUuid(Objects.requireNonNull(nbt.getUniqueId("p" + i)));
            if (e instanceof EntityPlayerMP) {
                EntityPlayerMP playerMP = (EntityPlayerMP) e;
                this.players.put(playerMP.getUniqueID(), playerMP);
                playerMP.getEntityData().setLong("TeamID", ID);
            } else {
                throw new RuntimeException("Team.class, transferred incorrect NBTTag value:1");
            }
        }
    }

    public void kill(UUID uuid) {
        players.get(uuid).getEntityData().setLong("TeamID", 0);
        players.remove(uuid);
    }

    public EntityPlayerMP get(UUID uuid) {
        return players.get(uuid);
    }

    public EntityPlayerMP[] getAll() {
        return players.values().toArray(new EntityPlayerMP[0]);
    }
}
