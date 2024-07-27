package com.artur.returnoftheancients.ancientworldgeneration.main;

import com.artur.returnoftheancients.ancientworldgeneration.main.entry.AncientEntry;
import com.artur.returnoftheancients.ancientworldgeneration.main.entry.AncientEntrySolo;
import com.artur.returnoftheancients.ancientworldgeneration.util.interfaces.IBuild;
import com.artur.returnoftheancients.handlers.FreeTeleporter;
import com.artur.returnoftheancients.init.InitDimensions;
import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.misc.WorldData;
import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.LinkedList;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Referense.MODID)
public class AncientWorld {
    private static boolean isLoad = false;
    private static final int buildCount = 4;
    private static final LinkedList<IBuild> build = new LinkedList<>();
    private static final LinkedList<AncientEntry> ancientEntries = new LinkedList<>();

    public static void tpToAncientWorld(EntityPlayerMP player) {
        FreeTeleporter.teleportToDimension(player, InitDimensions.ancient_world_dim_id, 0, 244, 0);
        player.setHealth(20);
        newAncientEntrySolo(player);
    }

    public static void load() {
        if (isLoad) {
            if (WorldData.get().saveData.hasKey("AncientWorldPak")) {
                NBTTagCompound nbt = WorldData.get().saveData.getCompoundTag("AncientWorldPak");
                int ancientEntriesCount = nbt.getInteger("AncientEntriesCount");
                for (int i = 0; i != ancientEntriesCount; i++) {
                    NBTTagCompound compound = nbt.getCompoundTag("Entry:" + i);

                    if (!compound.hasKey("IsTeam"))
                        throw new RuntimeException("AncientWorld.class, transferred incorrect NBTTag EC:0");

                    if (!compound.getBoolean("IsTeam")) {
                        ancientEntries.add(new AncientEntrySolo(compound));
                    } else {

                    }
                }
            }
            isLoad = true;
        }
    }

    private static void newAncientEntrySolo(EntityPlayerMP player) {
        ancientEntries.add(new AncientEntrySolo(player, foundFreePos()));
        save();
    }

    @SubscribeEvent
    public static void eventSave(WorldEvent.Save e) {
        if (!e.getWorld().isRemote) {
            save();
        }
    }

    public static void playerLostBus(UUID id) {
        for (AncientEntry entry : ancientEntries) {
            entry.dead(id);
        }
    }

    private static int foundFreePos() {
        if (ancientEntries.isEmpty()) return 1;
        boolean isFound;
        int pos = 1;
        while (true) {
            isFound = true;
            for (AncientEntry entry : ancientEntries) {
                if (entry.getPos() == pos) {
                    isFound = false;
                    break;
                }
            }
            if (isFound) {
                return pos;
            }
            pos++;
        }
    }

    private static void save() {
        NBTTagCompound nbt = new NBTTagCompound();
        saveAll(nbt);
        WorldData.get().saveData.setTag("AncientWorldPak", nbt);
        WorldData.get().markDirty();
        if (TRAConfigs.Any.debugMode) System.out.println("Save finish " + nbt);
    }

    private static void saveAll(NBTTagCompound nbt) {
        nbt.setInteger("AncientEntriesCount", ancientEntries.size());
        for (int i = 0; i != ancientEntries.size(); i++) {
            nbt.setTag("Entry:" + i, ancientEntries.get(i).toNBT());
        }
    }
    private static byte t = 0;
    @SubscribeEvent
    public static void Tick(TickEvent.WorldTickEvent e) {
        int bc = buildCount;
        for(IBuild entry : build) {
            if (!entry.isBuild()) {
                if (bc > 0) {
                    entry.build(e.world);
                    bc--;
                }
            } else {
                build.remove(entry);
            }
        }
        if (t >= 20) {
            t = 0;
            boolean isSave = false;
            for (int i = 0; i < ancientEntries.size(); i++) {
                AncientEntry entry = ancientEntries.get(i);
                entry.update();
                if (entry.isRequestToSave()) {
                    if (!isSave) {
                        save();
                        isSave = true;
                        entry.saveFinish();
                    } else {
                        entry.saveFinish();
                    }
                }
                if (entry.isRequestToDelete()) {
                    ancientEntries.remove(entry);
                    save();
                }
            }
        }
        t++;
    }

    public static void build(AncientEntry entry) {
        build.add(entry);
    }
}
