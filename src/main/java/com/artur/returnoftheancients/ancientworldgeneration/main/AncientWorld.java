package com.artur.returnoftheancients.ancientworldgeneration.main;

import com.artur.returnoftheancients.ancientworldgeneration.main.entry.AncientEntry;
import com.artur.returnoftheancients.ancientworldgeneration.main.entry.AncientEntrySolo;
import com.artur.returnoftheancients.ancientworldgeneration.util.interfaces.IBuild;
import com.artur.returnoftheancients.handlers.FreeTeleporter;
import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.misc.WorldData;
import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.LinkedList;
import java.util.UUID;

import static com.artur.returnoftheancients.init.InitDimensions.ancient_world_dim_id;

@Mod.EventBusSubscriber(modid = Referense.MODID)
public class AncientWorld {
    private static boolean isLoad = false;
    private static final int buildCount = 4;
    private static final LinkedList<IBuild> build = new LinkedList<>();
    private static final LinkedList<AncientEntry> ANCIENT_ENTRIES = new LinkedList<>();

    public static void tpToAncientWorld(EntityPlayerMP player) {
        FreeTeleporter.teleportToDimension(player, ancient_world_dim_id, 0, 244, 0);
        player.setHealth(20);
        newAncientEntrySolo(player);
    }

    public static void load(World world) {
        if (!isLoad) {
            if (WorldData.get().saveData.hasKey("AncientWorldPak")) {
                NBTTagCompound nbt = WorldData.get().saveData.getCompoundTag("AncientWorldPak");
                int ancientEntriesCount = nbt.getInteger("AncientEntriesCount");
                for (int i = 0; i != ancientEntriesCount; i++) {
                    if (!nbt.hasKey("Entry:" + i)) {
                        System.out.println("AncientWorld.class, transferred incorrect NBTTag EC:1");
                        break;
                    }
                    NBTTagCompound compound = nbt.getCompoundTag("Entry:" + i);

                    if (!compound.hasKey("IsTeam")) {
                        System.out.println("AncientWorld.class, transferred incorrect NBTTag EC:0");
                        break;
                    }

                    if (!compound.getBoolean("IsTeam")) {
                        ANCIENT_ENTRIES.add(new AncientEntrySolo(compound, world));
                    } else {

                    }
                }
            }
            isLoad = true;
        }
    }

    private static void newAncientEntrySolo(EntityPlayerMP player) {
        ANCIENT_ENTRIES.add(new AncientEntrySolo(player, foundFreePos()));
        save();
    }

    @SubscribeEvent
    public static void eventSave(WorldEvent.Save e) {
        if (!e.getWorld().isRemote && isLoad) {
            save();
        }
    }

    public static void playerLostBuss(UUID id) {
        for (AncientEntry entry : ANCIENT_ENTRIES) {
            if (entry.dead(id)) break;
        }
    }

    public static void bossDeadBuss(UUID id) {
        for (AncientEntry entry : ANCIENT_ENTRIES) {
            if (entry.deadBoss(id)) break;
        }
    }


    private static int foundFreePos() {
        if (ANCIENT_ENTRIES.isEmpty()) return 1;
        boolean isFound;
        int pos = 1;
        while (true) {
            isFound = true;
            for (AncientEntry entry : ANCIENT_ENTRIES) {
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
        WorldData worldData = WorldData.get();
        worldData.saveData.setTag("AncientWorldPak", nbt);
        worldData.markDirty();
        if (TRAConfigs.Any.debugMode) System.out.println("Save finish " + nbt);
    }

    private static void saveAll(NBTTagCompound nbt) {
        nbt.setInteger("AncientEntriesCount", ANCIENT_ENTRIES.size());
        for (int i = 0; i != ANCIENT_ENTRIES.size(); i++) {
            nbt.setTag("Entry:" + i, ANCIENT_ENTRIES.get(i).toNBT());
        }
    }
    private static byte t = 0;
    @SubscribeEvent
    public static void Tick(TickEvent.WorldTickEvent e) {
        if (e.world.provider.getDimension() == ancient_world_dim_id) {
            int bc = buildCount;
            for (IBuild entry : build) {
                if (!entry.isBuild()) {
                    if (bc > 0) {
                        entry.build(e.world);
                        bc--;
                    }
                } else {
                    build.remove(entry);
                }
            }
            if (t >= 10) {
                t = 0;
                boolean isSave = false;
                for (int i = 0; i < ANCIENT_ENTRIES.size(); i++) {
                    AncientEntry entry = ANCIENT_ENTRIES.get(i);
                    entry.update(e.world);
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
                        ANCIENT_ENTRIES.remove(entry);
                        save();
                    }
                }
            }
            t++;
        }
    }

    public static void onBossTriggerBlockAdd(int pos, BlockPos bossPos) {
        if (ANCIENT_ENTRIES.isEmpty()) {
            System.out.println("It's strange, this block was installed outside the ancient labyrinth, pos:" + bossPos);
        }
        boolean flag = false;
        for (AncientEntry entry : ANCIENT_ENTRIES) {
            if (entry.onBossTriggerBlockAdd(pos, bossPos)) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            System.out.println("It's strange, this block was installed outside the ancient labyrinth, pos:" + bossPos);
        } else {
            save();
        }
    }

    public static void build(AncientEntry entry) {
        build.add(entry);
    }
}
