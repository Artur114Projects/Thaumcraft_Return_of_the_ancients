package com.artur.returnoftheancients.generation.generators.portal.base;

import com.artur.returnoftheancients.ancientworldgeneration.main.AncientWorld;
import com.artur.returnoftheancients.generation.generators.portal.AncientPortalNaturalGeneration;
import com.artur.returnoftheancients.handlers.HandlerR;
import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Mod.EventBusSubscriber(modid = Referense.MODID)
public class AncientPortalsProcessor {

    private static List<Integer> TO_DELETE = new ArrayList<>();
    public static Map<Integer, AncientPortal> PORTALS = new HashMap<>();

    @SubscribeEvent
    public static void eventSave(WorldEvent.Save e) {

    }

    @SubscribeEvent
    public static void WorldEventLoad(WorldEvent.Load e) {

    }

    public static void unload() {
        TO_DELETE.clear();
        PORTALS.clear();
    }

    @SubscribeEvent
    public static void BreakEvent(BlockEvent.BreakEvent e) {
        PORTALS.forEach((key, value) -> {
            if (value.isCollide(e.getPos())) {
                value.onBlockDestroyedInPortalChunk(e);
            }
        });
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
                PORTALS.remove(d);
            }
            TO_DELETE.clear();
        }
    }
    public static AncientPortal getPortal(int ID) {
        return PORTALS.get(ID);
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
                AncientPortal newPortal = new AncientPortalNaturalGeneration(player.getServer(), 0, x, z, HandlerR.calculateGenerationHeight(world, (x << 4) + 8, (z << 4) + 8));
                PORTALS.put(0,newPortal);
                newPortal.build();
            }
        }
    }

    public static void onPlayerCollidePortal(EntityPlayerMP player) {
        AtomicBoolean flagA = new AtomicBoolean(false);
        PORTALS.forEach((key, value) -> {
            if (value.isCollide(player.getPosition())) {
                value.tpToAncientWorld(player);
                flagA.set(true);
            }
        });
        boolean flag = flagA.get();
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
            AncientPortal portal = new AncientPortalNaturalGeneration(player.getServer(), player.dimension, ((int) player.posX) >> 4,((int) player.posZ) >> 4, HandlerR.calculateGenerationHeight(player.world, ((((int) player.posX) >> 4) << 4) + 8, ((((int) player.posZ) >> 4) << 4) + 8));
            portal.build();
        }
    }

}