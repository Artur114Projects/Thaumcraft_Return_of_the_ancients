package com.artur.returnoftheancients.generation.generators.portal.base;

import com.artur.returnoftheancients.ancientworldgeneration.main.AncientWorld;
import com.artur.returnoftheancients.generation.generators.portal.AncientPortalNaturalGeneration;
import com.artur.returnoftheancients.generation.generators.portal.AncientPortalOpening;
import com.artur.returnoftheancients.handlers.HandlerR;
import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.misc.WorldData;
import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Mod.EventBusSubscriber(modid = Referense.MODID)
public class AncientPortalsProcessor {

    private static final List<Integer> LOAD_DIMENSIONS = new ArrayList<>();
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
        if (!LOAD_DIMENSIONS.contains(e.getWorld().provider.getDimension()) && !e.getWorld().isRemote) {
            int dimension = e.getWorld().provider.getDimension();
            if (TRAConfigs.Any.debugMode) System.out.println("Load portals dim:" + dimension);
            LOAD_DIMENSIONS.add(dimension);
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
        t++;
        if (t >= 10) {
            t = 0;
            PORTALS.forEach((key, value) -> {
                value.update(e);
                if (value.isExplore()) {
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

    public static AncientPortal getPortal(int ID) {
        return PORTALS.get(ID);
    }

    @NotNull
    public static AncientPortal loadPortal(MinecraftServer server, NBTTagCompound nbt) {
        switch (nbt.getInteger("portalTypeID")) {
            case 0:{
                return new AncientPortalNaturalGeneration(server, nbt);
            }
            case 1:{
                return new AncientPortalOpening(server, nbt);
            }
        }
        return null;
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
}