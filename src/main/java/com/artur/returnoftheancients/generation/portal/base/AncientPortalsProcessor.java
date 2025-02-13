package com.artur.returnoftheancients.generation.portal.base;

import com.artur.returnoftheancients.generation.portal.AncientPortalNaturalGeneration;
import com.artur.returnoftheancients.generation.portal.AncientPortalOpening;
import com.artur.returnoftheancients.generation.terraingen.GenLayersHandler;
import com.artur.returnoftheancients.handlers.HandlerR;
import com.artur.returnoftheancients.main.MainR;
import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.misc.WorldData;
import com.artur.returnoftheancients.network.ClientPacketSyncAncientPortals;
import com.artur.returnoftheancients.referense.Referense;
import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
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
            save();
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
        if (!LOADED_DIMENSIONS.contains(e.getWorld().provider.getDimension())) {
            int dimension = e.getWorld().provider.getDimension();
            if (Arrays.stream(TRAConfigs.PortalSettings.dimensionsGenerate).anyMatch((i) -> i == dimension)) {
                if (dimension == 0) {
                    portalsGenerationPosOverWorld = new ChunkPos[portalsCount];
                    GenLayersHandler.initPortalsPosOnWorld(portalsGenerationPosOverWorld, e.getWorld().getWorldInfo().getSeed());
                } else {
                    ChunkPos[] poss = new ChunkPos[portalsCount];
                    GenLayersHandler.initPortalsPosOnWorld(poss, e.getWorld().getSeed());
                    PORTALS_GENERATION_POS.put(dimension, poss);
                }
            }
            if (!e.getWorld().isRemote) {
                if (TRAConfigs.Any.debugMode) System.out.println("Load portals dim:" + dimension);
                WorldData worldData = WorldData.get();
                NBTTagCompound portalsPack = worldData.saveData.getCompoundTag("PortalsPack");
                if (portalsPack.hasKey(dimension + "")) {
                    NBTTagList list = portalsPack.getTagList(dimension + "", 10);
                    for (int i = 0; i != list.tagCount(); i++) {
                        NBTTagCompound nbt = list.getCompoundTagAt(i);
                        addNewPortal(loadPortal(e.getWorld().getMinecraftServer(), nbt));
                    }
                }
            }
            LOADED_DIMENSIONS.add(dimension);
        }
    }

    @SubscribeEvent
    public static void BreakEvent(BlockEvent.BreakEvent e) {
        for (AncientPortal portal : LOADED_PORTALS.values()) {
            if (portal.isLoaded() && portal.isGenerated() && portal.isOnPortalRange(e.getPos())) {
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
            updateLoadedPortalsMap();

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
                save();
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
            if (e.player.getEntityData().hasKey(AncientPortal.PortalID)) {
                NBTTagCompound data = e.player.getEntityData();
                int i = data.getInteger(AncientPortal.PortalID);
                if (i != -1) {
                    AncientPortal portal = getPortal(i);
                    if (portal != null) {
                        portal.playerHasPortalIdUpdate((EntityPlayerMP) e.player);
                    }
                }
            }
        }
    }


    @SubscribeEvent
    public static void populatePre(PopulateChunkEvent.Pre e) {
        if (e.getWorld().provider.getDimension() == 0) {
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

    public static AncientPortal getPortal(int ID) {
        return PORTALS.get(ID);
    }

    public static int getFreeId() {
        return HandlerR.foundMostSmallUniqueIntInList(new ArrayList<>(AncientPortalsProcessor.PORTALS.keySet()));
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
    public static void save() {
        long time = System.nanoTime();
        WorldData worldData = WorldData.get();
        NBTTagCompound nbt = worldData.saveData.hasKey("PortalsPack") ? worldData.saveData.getCompoundTag("PortalsPack") : new NBTTagCompound();

        if (!LOADED_PORTALS.isEmpty()) {
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

            PORTAL_DIMENSIONS.forEach((dimension, portals) -> {
                if (!LOADED_DIMENSIONS.contains(dimension)) {
                    return;
                }

                NBTTagList list = nbt.getTagList(dimension + "", 10);

                for (int i = 0; i != portals.size(); i++) {
                    AncientPortal portal = portals.get(i);
                    NBTTagCompound portalData = portal.writeToNBT();
                    if (portalData == null) {
                        continue;
                    }
                    if (LOADED_PORTALS.containsKey(portal.id)) {
                        list.set(i, portalData);
                    }
                }

                nbt.setTag(dimension + "", list);
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
        AncientPortal portal = PORTALS.get(player.getEntityData().getInteger(AncientPortal.PortalID));
        if (portal != null) {
            portal.tpToHome(player);
        } else {
            portal = PORTALS.get(0);
            if (portal != null) {
                portal.tpToHome(player);
            } else {
//                World world = Objects.requireNonNull(player.getServer()).getWorld(0);
//                Random rand = new Random(world.getSeed());
//                int x = rand.nextInt(TRAConfigs.PortalSettings.generationRange) - TRAConfigs.PortalSettings.generationRange / 2;
//                int z = rand.nextInt(TRAConfigs.PortalSettings.generationRange) - TRAConfigs.PortalSettings.generationRange / 2;
//                new AncientPortalNaturalGeneration(player.getServer(), 0, x, z, HandlerR.calculateGenerationHeight(world, (x << 4) + 8, (z << 4) + 8));
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
                return portal;
            }
        }
        return null;
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
        if (!nearestPosOptional.isPresent()) {
            return new ChunkPos(0, 0);
        }
        ChunkPos nearestPos = nearestPosOptional.get();
        return new ChunkPos(nearestPos.x, nearestPos.z);
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