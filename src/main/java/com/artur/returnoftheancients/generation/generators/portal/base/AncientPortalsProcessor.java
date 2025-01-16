package com.artur.returnoftheancients.generation.generators.portal.base;

import com.artur.returnoftheancients.generation.generators.portal.AncientPortalNaturalGeneration;
import com.artur.returnoftheancients.generation.generators.portal.AncientPortalOpening;
import com.artur.returnoftheancients.handlers.HandlerR;
import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.misc.WorldData;
import com.artur.returnoftheancients.referense.Referense;
import com.artur.returnoftheancients.utils.math.UltraMutableBlockPos;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.artur.returnoftheancients.init.InitDimensions.ancient_world_dim_id;

@Mod.EventBusSubscriber(modid = Referense.MODID)
public class AncientPortalsProcessor {

    private static final Map<Integer, ChunkPos[]> PORTALS_GENERATION_POS = new HashMap<>();
    private static ChunkPos[] portalsGenerationPosOverWorld = null;
    public static final int portalsCount = 8;

    private static final Set<Integer> LOAD_DIMENSIONS = new HashSet<>();
    private static final List<Integer> TO_DELETE = new ArrayList<>();
    public static Map<Integer, AncientPortal> PORTALS = new HashMap<>();


    @SubscribeEvent
    public static void eventSave(WorldEvent.Save e) {
        if (!e.getWorld().isRemote) {
            save();
        }
    }

    @SubscribeEvent
    public static void WorldEventLoad(WorldEvent.Load e) {
        if (!LOAD_DIMENSIONS.contains(e.getWorld().provider.getDimension())) {
            int dimension = e.getWorld().provider.getDimension();
            if (Arrays.stream(TRAConfigs.PortalSettings.dimensionsGenerate).anyMatch((i) -> i == dimension)) {
                if (dimension == 0) {
                    portalsGenerationPosOverWorld = new ChunkPos[portalsCount];
                    initPortalsPosOnWorld(portalsGenerationPosOverWorld, e.getWorld().getWorldInfo().getSeed());
                } else {
                    ChunkPos[] poss = new ChunkPos[portalsCount];
                    initPortalsPosOnWorld(poss, e.getWorld().getSeed());
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
                        PORTALS.put(dimension, loadPortal(e.getWorld().getMinecraftServer(), nbt));
                    }
                }
            }
            LOAD_DIMENSIONS.add(dimension);
        }
    }

    @SubscribeEvent
    public static void BreakEvent(BlockEvent.BreakEvent e) {
        for (AncientPortal portal : PORTALS.values()) {
            if (portal.isCollide(e.getPos())) {
                portal.onBlockDestroyedInPortalChunk(e);
            }
        }
    }

    private static byte t = 0;

    @SubscribeEvent
    public static void Tick(TickEvent.ServerTickEvent e) {
        if (e.phase == TickEvent.Phase.START) {
            return;
        }
        t++;
        if (t >= 10) {
            t = 0;
            if (PORTALS.isEmpty()) return;

            PORTALS.forEach((key, value) -> {
                value.update(e);
                if (value.isExploded()) {
                    TO_DELETE.add(key);
                }
            });

            for (int d : TO_DELETE) {
                Objects.requireNonNull(PORTALS.remove(d));
            }

            if (!TO_DELETE.isEmpty()) {
                TO_DELETE.clear();
                save();
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

    public static AncientPortal getPortal(int ID) {
        return PORTALS.get(ID);
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
            if (portal.chunkX == chunkX && portal.chunkZ == chunkZ && portal.dimension == dimension) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasPortal(BlockPos pos, int dimension) {
        return hasPortal(pos.getX() >> 4, pos.getZ() >> 4, dimension);
    }

    // TODO: Оптимизировать, возможно убрать систему с сортировкой по измерениям
    public static void save() {
        long time = System.nanoTime();
        WorldData worldData = WorldData.get();
        NBTTagCompound nbt = worldData.saveData.hasKey("PortalsPack") ? worldData.saveData.getCompoundTag("PortalsPack") : new NBTTagCompound();

        if (!PORTALS.isEmpty()) {
            Map<Integer, List<AncientPortal>> PORTAL_DIMENSIONS = new HashMap<>();

            for (AncientPortal portal : PORTALS.values()) {
                if (PORTAL_DIMENSIONS.containsKey(portal.dimension)) {
                    PORTAL_DIMENSIONS.get(portal.dimension).add(portal);
                } else {
                    List<AncientPortal> portalList = new ArrayList<>();
                    portalList.add(portal);
                    PORTAL_DIMENSIONS.put(portal.dimension, portalList);
                }
            }

            NBTTagCompound finalNbt = nbt;
            PORTAL_DIMENSIONS.forEach((dimension, portals) -> {
                if (!LOAD_DIMENSIONS.contains(dimension)) {
                    return;
                }

                NBTTagList list = new NBTTagList();

                for (AncientPortal portal : portals) {
                    NBTTagCompound portalData = portal.writeToNBT();
                    if (portalData == null) {
                        continue;
                    }
                    list.appendTag(portalData);
                }
                finalNbt.setTag(dimension + "", list);
            });
        } else {
            nbt = new NBTTagCompound();
        }

        worldData.saveData.setTag("PortalsPack", nbt);
        worldData.markDirty();
        if (TRAConfigs.Any.debugMode) System.out.println("Save portals finish " + nbt);
        if (TRAConfigs.Any.debugMode) System.out.println("Is took:" + ((System.nanoTime() - time) / 1000000.0D) + "ms");

    }

    public static void unload() {
        PORTALS_GENERATION_POS.clear();
        LOAD_DIMENSIONS.clear();
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
                World world = Objects.requireNonNull(player.getServer()).getWorld(0);
                Random rand = new Random(world.getSeed());
                int x = rand.nextInt(TRAConfigs.PortalSettings.generationRange) - TRAConfigs.PortalSettings.generationRange / 2;
                int z = rand.nextInt(TRAConfigs.PortalSettings.generationRange) - TRAConfigs.PortalSettings.generationRange / 2;
                new AncientPortalNaturalGeneration(player.getServer(), 0, x, z, HandlerR.calculateGenerationHeight(world, (x << 4) + 8, (z << 4) + 8));
            }
        }
    }

    public static void onPlayerCollidePortal(EntityPlayerMP player) {
        boolean flag = false;
        for (AncientPortal portal : PORTALS.values()) {
            if (portal.isCollide(player.getPosition())) {
                portal.onCollide(player);
                flag = true;
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
            new AncientPortalNaturalGeneration(player.getServer(), player.dimension, ((int) player.posX) >> 4,((int) player.posZ) >> 4, HandlerR.calculateGenerationHeight(player.world, ((((int) player.posX) >> 4) << 4) + 8, ((((int) player.posZ) >> 4) << 4) + 8));
        }
    }

    @Nullable
    public static AncientPortal getPortalOnPos(BlockPos pos) {
        for (AncientPortal portal : PORTALS.values()) {
            if (portal.isCollide(pos)) {
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
            System.out.println("[Warning] (AncientPortalsProcessor::getNearestPortalPos) request portal pos array == null");
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

    private static int portalGenerateOffset(Random rand) {
        return (((rand.nextInt(8) - 4) + 1) << 8);
    }

    public static void initPortalsPosOnWorld(ChunkPos[] portalPos, long seed) {
        final Random rand = new Random(seed);
        rand.nextInt();

        final double angleOffset = ((Math.PI * 2) / (rand.nextInt(16) + 1));
        final int defaultDistance = 4000;
        final int distance = 8000;

        for (int i = 0; i != portalPos.length; i++) {
            double angle = (((Math.PI * 2) / portalPos.length) * i) + angleOffset;
            int radius = defaultDistance + (distance * i) + portalGenerateOffset(rand);

            int chunkX = (int) ((radius * Math.cos(angle)) + portalGenerateOffset(rand)) >> 8;
            int chunkZ = (int) ((radius * Math.sin(angle)) + portalGenerateOffset(rand)) >> 8;

            portalPos[i] = new ChunkPos(chunkX << 4, chunkZ << 4);
        }

        if (TRAConfigs.Any.debugMode) {
            System.out.println("Seed:" + seed);
            for (ChunkPos pos : portalPos) {
                System.out.println(pos);
            }
        }
    }
}