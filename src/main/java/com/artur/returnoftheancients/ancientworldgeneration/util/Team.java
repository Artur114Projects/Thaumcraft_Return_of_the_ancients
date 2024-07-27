package com.artur.returnoftheancients.ancientworldgeneration.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.*;

public class Team {
    private final HashMap<UUID, EntityPlayerMP> players = new HashMap<>();;
    public Team(List<EntityPlayerMP> players) {
        for (EntityPlayerMP playerMP : players) {
            this.players.put(playerMP.getUniqueID(), playerMP);
        }
    }

    public Team(EntityPlayerMP... players) {
        for (EntityPlayerMP playerMP : players) {
            this.players.put(playerMP.getUniqueID(), playerMP);
        }
    }

    public Team(NBTTagCompound nbt) {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (!nbt.hasKey("p0")) throw new RuntimeException("Team.class, transferred incorrect NBTTag EC:0");
        for (int i = 0; nbt.hasKey("p" + i); i++) {
            Entity e = server.getEntityFromUuid(Objects.requireNonNull(nbt.getUniqueId("p" + i)));
            if (e instanceof EntityPlayerMP) {
                EntityPlayerMP playerMP = (EntityPlayerMP) e;
                this.players.put(playerMP.getUniqueID(), playerMP);
            } else {
                throw new RuntimeException("Team.class, transferred incorrect NBTTag EC:1");
            }
        }
    }

    public void kill(UUID uuid) {
        players.remove(uuid);
    }

    public EntityPlayerMP get(UUID uuid) {
        return players.get(uuid);
    }

    public EntityPlayerMP[] getAll() {
        return players.values().toArray(new EntityPlayerMP[0]);
    }

    public NBTTagCompound toNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        UUID[] arrayP = players.keySet().toArray(new UUID[0]);
        for (int i = 0; i != arrayP.length; i++) {
            nbt.setUniqueId("p" + i, arrayP[i]);
        }
        return nbt;
    }
}
