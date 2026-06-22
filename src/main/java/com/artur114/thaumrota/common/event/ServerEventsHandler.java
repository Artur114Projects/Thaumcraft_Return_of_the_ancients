package com.artur114.thaumrota.common.event;

import com.artur114.bananalib.mc.BananaMC;
import com.artur114.thaumrota.common.config.server.RotAServerConfig;
import com.artur114.thaumrota.common.util.CapUtils;
import com.artur114.thaumrota.common.worldstate.ancientworld.system.base.AncientLayer1EventsHandler;
import com.artur114.thaumrota.common.event.eventmanagers.PlayerInBiomeManager;
import com.artur114.thaumrota.common.event.eventmanagers.ShortChunkLoadManager;
import com.artur114.thaumrota.common.event.eventmanagers.SlowBuildManager;
import com.artur114.thaumrota.common.event.eventmanagers.TimerTasksManager;
import com.artur114.thaumrota.common.handlers.MiscHandler;
import com.artur114.thaumrota.server.structurebuilder.StructuresBuildManager;
import com.artur114.thaumrota.common.worldstate.playertimer.IPlayerTimer;
import com.artur114.thaumrota.common.init.InitCapabilities;
import com.artur114.thaumrota.common.biomes.BiomeTaint;
import com.artur114.thaumrota.common.generation.portal.base.AncientPortalsProcessor;
import com.artur114.thaumrota.common.init.InitBiomes;
import com.artur114.thaumrota.common.config.RotAConfig;
import com.artur114.thaumrota.common.blocks.BlockAncientWorldPortal;
import com.artur114.thaumrota.main.ThaumRotA;
import com.artur114.thaumrota.server.event.PublicSStoppingEvent;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thaumcraft.api.blocks.BlocksTC;

import static com.artur114.thaumrota.common.init.InitDimensions.ANCIENT_WORLD_ID;

@Mod.EventBusSubscriber(modid = ThaumRotA.MODID)
public class ServerEventsHandler { // TODO: 10.05.2025 Переписать!
    public static final ShortChunkLoadManager SHORT_CHUNK_LOAD_MANAGER = new ShortChunkLoadManager();
    public static final PlayerInBiomeManager PLAYER_IN_BIOME_MANAGER = new PlayerInBiomeManager();
    public static final TimerTasksManager TIMER_TASKS_MANAGER = new TimerTasksManager();
    public static final SlowBuildManager SLOW_BUILD_MANAGER = new SlowBuildManager();
    private static boolean isAncientAreaSet = false;

    @SubscribeEvent
    public static void worldEventLoad(WorldEvent.Load e) {
        if (!e.getWorld().isRemote) {
            if (e.getWorld().provider.getDimension() == ANCIENT_WORLD_ID) {
                if (!isAncientAreaSet) {
                    StructuresBuildManager.createBuildRequest(e.getWorld(), new BlockPos(-16, 240, -16), "ancient_area").build();
                    isAncientAreaSet = true;
                }
            }
        }
    }

    @SubscribeEvent
    public static void livingHurtEvent(LivingHurtEvent e) {
        if (!RotAConfig.server.canDeadInAncientWorld) {
            if (e.getEntity() instanceof EntityPlayerMP) {
                if (e.getEntity().dimension == ANCIENT_WORLD_ID) {
                    EntityPlayerMP player = (EntityPlayerMP) e.getEntity();
                    if (player.getHealth() - e.getAmount() <= 0) {
                        e.setCanceled(true);
                        onPlayerLost(player);
                        return;
                    }

                    double additionalOffset = RotAConfig.server.difficulty.additionalOffset;
                    int ignoringOffset = RotAConfig.server.difficulty.ignoringOffset;
                    int baseChange = RotAConfig.server.difficulty.baseChange;

                    double hurt = player.getEntityData().getDouble("hurt");
                    if (MiscHandler.getIgnoringChance((int) (baseChange + (ignoringOffset * hurt)), player.world.rand)) {
                        player.getEntityData().setDouble("hurt", hurt + 1.0D + additionalOffset);
                        player.setHealth(player.getHealth() - e.getAmount());
                        e.setCanceled(true);
                    } else {
                        player.getEntityData().setDouble("hurt", hurt - 1.0D);
                    }
                }
            }
        }
    }

    protected static void onPlayerLost(EntityPlayerMP player) {
        player.setFire(0);
        player.setHealth(2);
        player.clearActivePotions();
        if (!AncientLayer1EventsHandler.SERVER_MANAGER.playerLost(player)) AncientPortalsProcessor.teleportToOverworld(player);
    }

    @SubscribeEvent
    public static void livingDropsEvent(LivingDropsEvent e) {
        if (e.getEntity().dimension == ANCIENT_WORLD_ID && !e.getEntity().isNonBoss()) {
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void BreakEvent(BlockEvent.BreakEvent e) {
        if (e.getPlayer().dimension == ANCIENT_WORLD_ID) {
            if (!e.getPlayer().isCreative() && e.getState().getBlock() != BlocksTC.stoneArcane) {
                e.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void PlaceEvent(BlockEvent.PlaceEvent e) {
        if (e.getPlayer().dimension == ANCIENT_WORLD_ID) {
            if (!e.getPlayer().isCreative()) {
                e.getPlayer().addItemStackToInventory(e.getItemInHand().splitStack(1));
                e.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void playerTickEvent(TickEvent.PlayerTickEvent e) {
        if (e.phase != TickEvent.Phase.START || e.player.world.isRemote) {
            return;
        }

        PLAYER_IN_BIOME_MANAGER.tickEventPlayerTickEvent(e);

        int playerDimension = e.player.dimension;
        if (e.player.ticksExisted % 4 == 0) {
            if (playerDimension == ANCIENT_WORLD_ID) {
                e.player.fallDistance = 0;
            }
        }
        if (e.player.ticksExisted % 20 == 0) {
            if (playerDimension != ANCIENT_WORLD_ID) {
                if (e.player.getEntityData().getBoolean(BlockAncientWorldPortal.noCollisionNBT)) {
                    CapUtils.capability(e.player, InitCapabilities.TIMER).ifPresent(timer -> {
                        if (timer.getTime("notCollisionTime") >= 8 * 20) {
                            e.player.getEntityData().setBoolean(BlockAncientWorldPortal.noCollisionNBT, false);
                            timer.delete("notCollisionTime");
                        }
                        timer.addTime(20, "notCollisionTime");
                    });
                }
            }
        }
        if (e.player.ticksExisted % 40 == 0) {
            tickTimer(e.player);
        }
    }

    private static void tickTimer(EntityPlayer player) {
        IPlayerTimer timer = player.getCapability(InitCapabilities.TIMER, null);
        if (timer != null && timer.hasTimer("poisoning")) {
            timer.addTime(40, "poisoning");
        }
    }

//    @SubscribeEvent
//    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
//        if (event.getEntity().dimension == ANCIENT_WORLD_ID && !event.getWorld().isRemote) {
//            if (RotAConfig.server.difficulty.speedAmplifier > 0 && event.getEntity() instanceof EntityLiving && !(event.getEntity() instanceof EntityPlayer)) {
//                EntityLiving living = (EntityLiving) event.getEntity();
//                if (living.isNonBoss() || RotAConfig.server.difficulty.iaAddSpeedEffectToBoss) {
//                    living.addPotionEffect(new PotionEffect(MobEffects.SPEED, Integer.MAX_VALUE, RotAConfig.server.difficulty.speedAmplifier - 1, false, false));
//                }
//            }
//        }
//    }

    @SubscribeEvent
    public void canDeSpawn(LivingSpawnEvent.AllowDespawn event) {
        if (event.getEntity().dimension == ANCIENT_WORLD_ID) {
            event.setResult(Event.Result.DENY);
        }
    }

    @SubscribeEvent
    public static void canSpawn(LivingSpawnEvent.CheckSpawn e) {
        Biome biome = e.getWorld().getBiome(e.getEntity().getPosition());
        if (biome instanceof BiomeTaint) {
            if (!BiomeTaint.canSpawn(e.getEntity(), (BiomeTaint) biome)) {
                e.setResult(Event.Result.DENY);
            }
        }
    }

    @SubscribeEvent
    public static void tick(TickEvent.WorldTickEvent e) {
        if (!e.world.isRemote) {
            if (e.phase != TickEvent.Phase.START) {
                return;
            }

            if (e.world.provider.getDimension() == 0) {
                MinecraftServer server = e.world.getMinecraftServer();
                if (server != null && server.getTickCounter() >= 40) {
                    int taintChunks = 0;
                    for (Chunk chunk : ((WorldServer) e.world).getChunkProvider().getLoadedChunks()) {
                        if (chunk == null) {
                            continue;
                        }

                        if (BananaMC.chunkContainsBiomeTypeOnCorners(chunk, InitBiomes.TAINT_TYPE_L)) {
                            BiomeTaint.chunkHasBiomeUpdate(chunk);
                            taintChunks++;
                        }
                    }
                    BiomeTaint.taintChunks = taintChunks;
                }
            }
        }
    }

    @SubscribeEvent
    public static void serverTick(TickEvent.ServerTickEvent e) {
        SHORT_CHUNK_LOAD_MANAGER.tickEventServerTickEvent(e);
        TIMER_TASKS_MANAGER.tickEventServerTickEvent(e);
        SLOW_BUILD_MANAGER.tickEventServerTickEvent(e);
    }

    @SubscribeEvent
    public static void saveEvent(WorldEvent.Save e) {
        SHORT_CHUNK_LOAD_MANAGER.worldEventSave(e);
    }

    @SubscribeEvent
    public static void unload(PublicSStoppingEvent e) {
        isAncientAreaSet = false;
        SHORT_CHUNK_LOAD_MANAGER.unload();
        TIMER_TASKS_MANAGER.unload();
        SLOW_BUILD_MANAGER.unload();
    }
}