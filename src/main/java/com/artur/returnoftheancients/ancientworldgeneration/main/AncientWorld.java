package com.artur.returnoftheancients.ancientworldgeneration.main;

import com.artur.returnoftheancients.ancientworldgeneration.main.entry.AncientEntry;
import com.artur.returnoftheancients.ancientworldgeneration.main.entry.AncientEntrySolo;
import com.artur.returnoftheancients.ancientworldgeneration.main.entry.AncientEntryTeam;
import com.artur.returnoftheancients.ancientworldgeneration.util.Team;
import com.artur.returnoftheancients.ancientworldgeneration.util.interfaces.IBuild;
import com.artur.returnoftheancients.handlers.FreeTeleporter;
import com.artur.returnoftheancients.handlers.HandlerR;
import com.artur.returnoftheancients.handlers.ServerEventsHandler;
import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.misc.WorldData;
import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.jetbrains.annotations.TestOnly;

import java.util.*;

import static com.artur.returnoftheancients.init.InitDimensions.ancient_world_dim_id;

@Mod.EventBusSubscriber(modid = Referense.MODID)
public class AncientWorld {
    private static boolean isLoad = false;
    private static final int buildCount = 2;
    private static Team.RawTeam rawTeam = null;
    private static final LinkedList<IBuild> build = new LinkedList<>();
    private static final LinkedList<AncientEntry> ANCIENT_ENTRIES = new LinkedList<>();
    @TestOnly
    public static byte getBuildCount() {
        return buildCount;
    }
    @TestOnly
    public static boolean isIsLoad() {
        return isLoad;
    }
    @TestOnly
    public static LinkedList<AncientEntry> getAncientEntries() {
        return ANCIENT_ENTRIES;
    }
    @TestOnly
    public static LinkedList<IBuild> getBuild() {
        return build;
    }

    public static void tpToAncientWorld(EntityPlayerMP player) {
        if (TRAConfigs.AncientWorldSettings.minPlayersCount > 1) {
            tpToAncientWorldWithQueue(player);
        } else {
            tpToAncientWorldNotQueue(player);
        }
    }

    private static void tpToAncientWorldNotQueue(EntityPlayerMP player) {
        player.setHealth(20);
        ItemStack stack = HandlerR.getSoulBinder(player);

        if (stack != null) {
            if (HandlerR.isSoulBinderFull(stack)) {
                Team team = new Team(stack, player);
                if (team.size() > 1) {
                    team.setToAll(AncientWorld::telepotToAncientArea);
                    team.injectNamesToPlayers();
                    newAncientEntryTeam(team);
                    return;
                } else {
                    team.delete();
                }
            }
        }

        telepotToAncientArea(player);
        newAncientEntrySolo(player);
    }

    private static void tpToAncientWorldWithQueue(EntityPlayerMP player) {
        player.setHealth(20);
        ItemStack stack = HandlerR.getSoulBinder(player);

        if (HandlerR.isSoulBinderFull(stack)) {
            Team team = new Team(stack, player);
            rawTeam.add(team);
            team.delete();
        } else {
            rawTeam.add(player);
        }

        telepotToAncientArea(player);
        HandlerR.setLoadingGuiState(player, true, true);
        rawTeam.injectNamesToPlayers();

        Team team = rawTeam.toTeam();
        if (team != null) {
            rawTeam.clear();
            newAncientEntryTeam(team);
        }
    }


    public static void telepotToAncientArea(EntityPlayerMP player) {
        FreeTeleporter.teleportToDimension(player, ancient_world_dim_id, 0, 244, 0);
    }

    public static void load() {
        if (!isLoad) {
            rawTeam = new Team.RawTeam(TRAConfigs.AncientWorldSettings.minPlayersCount);
            if (TRAConfigs.Any.debugMode) System.out.println("Load from NBT start!");
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
                        ANCIENT_ENTRIES.add(new AncientEntrySolo(compound));
                    } else {
                        ANCIENT_ENTRIES.add(new AncientEntryTeam(compound));
                    }
                }
            }
            isLoad = true;
        }
    }

    @TestOnly
    public static void reload() {
        ANCIENT_ENTRIES.clear();
        isLoad = false;
        load();
    }

    public static void unload() {
        isLoad = false;
        ANCIENT_ENTRIES.clear();
        build.clear();
        Team.clear();
    }

    private static void newAncientEntrySolo(EntityPlayerMP player) {
        ANCIENT_ENTRIES.add(new AncientEntrySolo(player, foundFreePos()));
        save();
    }

    private static void newAncientEntryTeam(Team team) {
        ANCIENT_ENTRIES.add(new AncientEntryTeam(foundFreePos(), team));
        save();
    }

    @SubscribeEvent
    public static void eventSave(WorldEvent.Save e) {
        if (!e.getWorld().isRemote && isLoad) {
            save();
        }
    }

    public static long getSeed(EntityPlayerMP player) {
        for (AncientEntry entry : ANCIENT_ENTRIES) {
            if (entry.isBelong(player)) return entry.getMapSeed();
        }
        return 0;
    }

    public static void playerJoinBuss(EntityPlayerMP player) {
        for (AncientEntry entry : ANCIENT_ENTRIES) {
            if (entry.wakeUp(player)) return;
        }
        if (!player.isCreative()) ServerEventsHandler.tpToHome(player);
    }

    public static void playerLoggedOutBus(UUID id) {
        for (AncientEntry entry : ANCIENT_ENTRIES) {
            if (entry.sleepPlayer(id)) break;
        }
    }

    public static void playerLostBus(UUID id) {
        for (AncientEntry entry : ANCIENT_ENTRIES) {
            if (entry.dead(id)) break;
        }
    }

    public static void bossDeadBus(UUID id) {
        for (AncientEntry entry : ANCIENT_ENTRIES) {
            if (entry.deadBoss(id)) break;
        }
    }

    public static void bossJoinBus(EntityJoinWorldEvent event) {
        for (AncientEntry entry : ANCIENT_ENTRIES) {
            if (entry.bossJoin(event)) return;
        }
        event.getWorld().removeEntity(event.getEntity());
        event.setCanceled(true);
    }

    public static void interrupt(EntityPlayerMP player) {
        for (AncientEntry entry : ANCIENT_ENTRIES) {
            if (entry.interrupt(player.getUniqueID())) return;
        }
        rawTeam.remove(player);
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
            nbt.setTag("Entry:" + i, ANCIENT_ENTRIES.get(i).writeToNBT());
        }
    }

    private static byte t = 0;

    @SubscribeEvent
    public static void Tick(TickEvent.WorldTickEvent e) {
        if (!e.world.isRemote) {
            if (e.world.provider.getDimension() == ancient_world_dim_id) {
                int bc = buildCount;
                if (!build.isEmpty()) {
                    ArrayList<IBuild> toDelete = new ArrayList<>();
                    for (IBuild entry : build) {
                        if (!entry.isBuild() && !entry.isRequestToDelete()) {
                            if (bc > 0) {
                                entry.build(e.world);
                                bc--;
                            }
                        } else {
                            toDelete.add(entry);
                        }
                    }
                    if (!toDelete.isEmpty()) {
                        build.removeAll(toDelete);
                    }
                }
                if (t >= 10) {
                    t = 0;
                    Team.updateS();

                    boolean isSave = false;

                    ArrayList<AncientEntry> toDelete = new ArrayList<>();
                    for (AncientEntry entry : ANCIENT_ENTRIES) {
                        entry.update(e.world);
                        if (entry.isRequestToSave()) {
                            if (!isSave) {
                                save();
                                isSave = true;
                                entry.saveFinis();
                            } else {
                                entry.saveFinis();
                            }
                        }
                        if (entry.isRequestToDelete()) {
                            toDelete.add(entry);
                        }
                    }
                    if (!toDelete.isEmpty()) {
                        ANCIENT_ENTRIES.removeAll(toDelete);
                        save();
                    }
                }
                t++;
                if (ANCIENT_ENTRIES.isEmpty()) build.clear();
            }
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
