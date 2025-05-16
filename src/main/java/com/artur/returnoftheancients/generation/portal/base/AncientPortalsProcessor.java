package com.artur.returnoftheancients.generation.portal.base;

import com.artur.returnoftheancients.generation.portal.naturalgen.AncientPortalNaturalGeneration;
import com.artur.returnoftheancients.generation.portal.AncientPortalOpening;
import com.artur.returnoftheancients.generation.terraingen.GenLayersHandler;
import com.artur.returnoftheancients.handlers.MiscHandler;
import com.artur.returnoftheancients.handlers.TeleportHandler;
import com.artur.returnoftheancients.main.MainR;
import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.misc.WorldData;
import com.artur.returnoftheancients.network.ClientPacketSyncAncientPortals;
import com.artur.returnoftheancients.referense.Referense;
import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.artur.returnoftheancients.init.InitDimensions.ancient_world_dim_id;

@Mod.EventBusSubscriber(modid = Referense.MODID)
public class AncientPortalsProcessor {

    private static final Map<Integer, ChunkPos[]> PORTALS_GENERATION_POS = new HashMap<>();
    private static ChunkPos[] portalsGenerationPosOverWorld = null;
    public static final int portalsCount = 8;

    private static final Map<Integer, AncientPortal> LOADED_PORTALS = new HashMap<>();
    private static final Map<Integer, AncientPortal> PORTALS = new HashMap<>();
    private static final Set<Integer> LOADED_DIMENSIONS = new HashSet<>();
    private static final List<Integer> TO_DELETE = new ArrayList<>();


    @SubscribeEvent
    public static void eventSave(WorldEvent.Save e) {
        if (!e.getWorld().isRemote) {
            save(false);
        }
    }

    @SubscribeEvent
    public static void playerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent e) {
        if (!e.player.world.isRemote) {
            updatePortalDataOnClient((EntityPlayerMP) e.player);
        }
    }

    @SubscribeEvent
    public static void WorldEventLoad(WorldEvent.Load e) {
        if (!e.getWorld().isRemote) {
            if (!LOADED_DIMENSIONS.contains(e.getWorld().provider.getDimension())) {
                int dimension = e.getWorld().provider.getDimension();
                WorldData worldData = WorldData.get();

                if (dimension == 0) {
                    portalsGenerationPosOverWorld = new ChunkPos[portalsCount];
                    GenLayersHandler.initPortalsPosOnWorld(portalsGenerationPosOverWorld, e.getWorld().getWorldInfo().getSeed());
                }

                if (TRAConfigs.Any.debugMode) System.out.println("Load portals dim:" + dimension);
                NBTTagCompound portalsPack = worldData.saveData.getCompoundTag("PortalsPack");
                if (portalsPack.hasKey(dimension + "")) {
                    NBTTagList list = portalsPack.getTagList(dimension + "", 10);
                    for (int i = 0; i != list.tagCount(); i++) {
                        NBTTagCompound nbt = list.getCompoundTagAt(i);
                        addNewPortal(loadPortal(e.getWorld().getMinecraftServer(), nbt));
                    }
                }

                if (dimension == 0) {
                    NBTTagList dimList = worldData.saveData.getTagList("generatedPortals", 3);
                    if (!MiscHandler.intTagListContains(dimList, dimension)) {
                        ChunkPos[] portalsPos = getAllPortalsPosOnDim(dimension);

                        for (ChunkPos pos : portalsPos) {
                            AncientPortal portal = new AncientPortalNaturalGeneration(e.getWorld().getMinecraftServer(), dimension, pos.x, pos.z);
                            PORTALS.put(portal.id, portal);
                        }

                        dimList.appendTag(new NBTTagInt(dimension));

                        worldData.saveData.setTag("generatedPortals", dimList);
                        worldData.markDirty();
                    }
                }

                LOADED_DIMENSIONS.add(dimension);
            }
        }
    }

    @SubscribeEvent
    public static void BreakEvent(BlockEvent.BreakEvent e) {
        for (AncientPortal portal : LOADED_PORTALS.values()) {
            if (portal.isGenerated() && portal.isOnPortalRange(e.getPos()) && portal.isLoaded()) {
                portal.onBlockDestroyedInPortalArea(e);
            }
        }
    }

    @SubscribeEvent
    public static void Tick(TickEvent.ServerTickEvent e) {
        if (e.phase == TickEvent.Phase.START) {
            return;
        }

        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();

        if (server.getTickCounter() % 10 == 0) {
            if (server.getTickCounter() % 20 == 0) {
                updateLoadedPortalsMap();
            }

            if (LOADED_PORTALS.isEmpty()) return;
            AtomicBoolean flag = new AtomicBoolean(false);

            LOADED_PORTALS.forEach((key, value) -> {
                if (!value.isGenerated()) {
                    return;
                }
                value.update(e);
                if (value.isExploded()) {
                    TO_DELETE.add(key);
                } else if (value.isNeedUpdateOnClient()) {
                    flag.set(true);
                }
            });

            for (int d : TO_DELETE) {
                PORTALS.remove(d);
            }

            if (!TO_DELETE.isEmpty()) {
                updateLoadedPortalsMap();
                flag.set(true);
                TO_DELETE.clear();
                save(true);
            }

            if (flag.get()) {
                updatePortalDataOnClient();
            }
        }
    }

    @SubscribeEvent
    public static void PlayerTick(TickEvent.PlayerTickEvent e) {
        if (e.player.world.isRemote) {
            return;
        }
        if (e.player.dimension == ancient_world_dim_id) {
            return;
        }
        if (e.phase != TickEvent.Phase.START) {
            return;
        }

        if (e.player.ticksExisted % 4 == 0) {
            if (e.player.getEntityData().hasKey(AncientPortal.portalID)) {
                NBTTagCompound data = e.player.getEntityData();
                int i = data.getInteger(AncientPortal.portalID);
                if (i != -1) {
                    AncientPortal portal = providePortal(i);
                    if (portal != null) {
                        portal.playerHasPortalIdUpdate((EntityPlayerMP) e.player);
                    }
                }
            }
        }
    }


    @SubscribeEvent
    public static void populatePre(PopulateChunkEvent.Pre e) {
        if (e.getWorld().provider.getDimension() == 0 && !e.getWorld().isRemote) {
            for (AncientPortal portal : PORTALS.values()) {
                if (portal.dimension == 0 && !portal.isGenerated()) {
                    portal.onChunkPopulatePre(e.getChunkX(), e.getChunkZ());
                }
            }
        }
    }

    public static void updateLoadedPortalsMap() {
        LOADED_PORTALS.clear();
        PORTALS.forEach((key, value) -> {
            if (value.isLoaded()) {
                LOADED_PORTALS.put(key, value);
            }
        });
    }

    public static void addNewPortal(AncientPortal portal) {
        if (!PORTALS.containsKey(portal.id)) {
            if (!hasPortal(portal.portalPos, portal.dimension)) {
                PORTALS.put(portal.id, portal);
                updateLoadedPortalsMap();
                updatePortalDataOnClient();
            } else {
                System.out.println("An attempt to add a portal to the position of an existing portal!");
            }
        } else {
            portal.id = getFreeId();
            addNewPortal(portal);
        }
    }

    public static int getFreeId() {
        return MiscHandler.foundMostSmallUniqueIntInList(new ArrayList<>(AncientPortalsProcessor.PORTALS.keySet()));
    }

    public static AncientPortal loadPortal(MinecraftServer server, NBTTagCompound nbt) {
        switch (nbt.getInteger("portalTypeID")) {
            case 0:{
                return new AncientPortalNaturalGeneration(server, nbt);
            }
            case 1:{
                return new AncientPortalOpening(server, nbt);
            }
        }
        throw new IllegalArgumentException();
    }

    public static boolean hasPortal(int chunkX, int chunkZ, int dimension) {
        for (AncientPortal portal : PORTALS.values()) {
            if (portal.isOnPortalRange(chunkX, chunkZ)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasPortal(ChunkPos pos, int dimension) {
        return hasPortal(pos.x, pos.z, dimension);
    }


    public static boolean hasPortal(BlockPos pos, int dimension) {
        return hasPortal(pos.getX() >> 4, pos.getZ() >> 4, dimension);
    }

    // TODO: Оптимизировать, возможно убрать систему с сортировкой по измерениям
    // TODO: 26.03.2025 Сделать по адекватному, чтобы сохранялся только тот мир который сохраняется.
    public static void save(boolean needSaveForcibly) {
        long time = System.nanoTime();
        WorldData worldData = WorldData.get();
        NBTTagCompound nbt = worldData.saveData.hasKey("PortalsPack") ? worldData.saveData.getCompoundTag("PortalsPack") : null;
        boolean needSaveAll;

        if (!LOADED_PORTALS.isEmpty() || nbt == null) {
            if (nbt == null || needSaveForcibly) {
                nbt = new NBTTagCompound();
                needSaveAll = true;
            } else {
                needSaveAll = false;
            }

            Map<Integer, List<AncientPortal>> PORTAL_DIMENSIONS = new HashMap<>();

            for (AncientPortal portal : PORTALS.values()) {
                if (PORTAL_DIMENSIONS.containsKey(portal.dimension)) {
                    PORTAL_DIMENSIONS.get(portal.dimension).add(portal);
                } else {
                    List<AncientPortal> portalList = new LinkedList<>();
                    portalList.add(portal);
                    PORTAL_DIMENSIONS.put(portal.dimension, portalList);
                }
            }

            NBTTagCompound finalNbt = nbt;
            PORTAL_DIMENSIONS.forEach((dimension, portals) -> {
                if (!LOADED_DIMENSIONS.contains(dimension)) {
                    return;
                }

                NBTTagList list = finalNbt.getTagList(dimension + "", 10);


                for (int i = 0; i != portals.size(); i++) {
                    AncientPortal portal = portals.get(i);
                    NBTTagCompound portalData = portal.writeToNBT(new NBTTagCompound());
                    if (portalData == null) {
                        continue;
                    }
                    if (LOADED_PORTALS.containsKey(portal.id) || needSaveAll) {
                        if (list.hasNoTags() || needSaveAll) {
                            list.appendTag(portalData);
                        } else {
                            list.set(i, portalData);
                        }
                    }
                }

                finalNbt.setTag(dimension + "", list);
            });
        }

        worldData.saveData.setTag("PortalsPack", nbt);
        worldData.markDirty();
        if (TRAConfigs.Any.debugMode) System.out.println("Is took:" + ((System.nanoTime() - time) / 1000000.0D) + "ms");
        if (TRAConfigs.Any.debugMode) System.out.println("Save portals finish " + nbt);
    }

    public static void unload() {
        PORTALS_GENERATION_POS.clear();
        LOADED_DIMENSIONS.clear();
        LOADED_PORTALS.clear();
        TO_DELETE.clear();
        PORTALS.clear();
    }

    public static void tpToHome(EntityPlayerMP player) {
        AncientPortal portal = providePortal(player.getEntityData().getInteger(AncientPortal.portalID));
        if (portal != null) {
            portal.tpToHome(player);
        } else {
            portal = PORTALS.get(0);
            if (portal != null) {
                portal.tpToHome(player);
            } else {
                TeleportHandler.teleportToDimension(player, 0, player.getBedLocation(0));
            }
        }
    }

    public static void onPlayerCollidePortal(EntityPlayerMP player) {
        boolean flag = false;
        for (AncientPortal portal : PORTALS.values()) {
            if (portal.isCollide(player.getPosition())) {
                portal.onCollide(player);
                flag = true;
                break;
            }
        }
        if (!flag) {
            for (int i = -1; i != 2; i++) {
                player.world.setBlockToAir(player.getPosition().add(0, i, 0));

                player.world.setBlockToAir(player.getPosition().add(0, i, -1));
                player.world.setBlockToAir(player.getPosition().add(0, i, 1));
                player.world.setBlockToAir(player.getPosition().add(-1, i, 0));
                player.world.setBlockToAir(player.getPosition().add(1, i, 0));

                player.world.setBlockToAir(player.getPosition().add(-1, i, -1));
                player.world.setBlockToAir(player.getPosition().add(1, i, 1));
                player.world.setBlockToAir(player.getPosition().add(1, i, -1));
                player.world.setBlockToAir(player.getPosition().add(-1, i, 1));
            }
        }
    }

    @Nullable
    public static AncientPortal getPortalOnPos(BlockPos pos) {
        for (AncientPortal portal : PORTALS.values()) {
            if (portal.isOnPortalRange(pos)) {
                load(portal);
                return portal;
            }
        }
        return null;
    }

    public static AncientPortal providePortal(int id) {
        AncientPortal portal = PORTALS.get(id);
        load(portal);
        return portal;
    }

    public static void load(AncientPortal portal) {
        if (portal != null && !LOADED_PORTALS.containsKey(portal.id)) {
            LOADED_PORTALS.put(portal.id, portal);
        }
    }

    public static boolean hasPortalOnWorld(World world) {
        return world.provider.getDimension() == 0 || PORTALS_GENERATION_POS.containsKey(world.provider.getDimension());
    }

    public static ChunkPos getNearestPortalPos(World world, UltraMutableBlockPos pos) {
        ChunkPos[] poss = world.provider.getDimension() == 0 ? portalsGenerationPosOverWorld : PORTALS_GENERATION_POS.get(world.provider.getDimension());
        if (poss == null) {
            System.out.println("[Warning] (AncientPortalsProcessor::getNearestPortalPos) Request portal pos array == null");
            return new ChunkPos(0, 0);
        }
        Optional<ChunkPos> nearestPosOptional = Arrays.stream(poss).min(Comparator.comparingInt(pos::distanceSq));
        return nearestPosOptional.orElseGet(() -> new ChunkPos(0, 0));
    }

    private static ChunkPos[] getAllPortalsPosOnDim(int dim) {
        return dim == 0 ? portalsGenerationPosOverWorld : PORTALS_GENERATION_POS.get(dim);
    }

    public static ChunkPos getPortalPos(World world, int id) {
        ChunkPos[] poss = world.provider.getDimension() == 0 ? portalsGenerationPosOverWorld : PORTALS_GENERATION_POS.get(world.provider.getDimension());
        if (id > poss.length) {
            return new ChunkPos(0, 0);
        }
        ChunkPos pos = poss[id];
        return new ChunkPos(pos.x, pos.z);
    }

    public static void updatePortalDataOnClient() {
        MainR.NETWORK.sendToAll(new ClientPacketSyncAncientPortals(crateSyncNBT()));
    }

    public static void updatePortalDataOnClient(EntityPlayerMP player) {
        MainR.NETWORK.sendTo(new ClientPacketSyncAncientPortals(crateSyncNBT()), player);
    }

    public static NBTTagCompound crateSyncNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        int i = 0;
        for (AncientPortal portal : PORTALS.values()) {
            NBTTagCompound data = portal.createClientUpdateNBT();
            if (data == null) {
                continue;
            }
            nbt.setTag("Portal:" + i, data);
            i++;
        }
        return nbt;
    }

}