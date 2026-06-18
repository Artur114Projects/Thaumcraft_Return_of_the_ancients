package com.artur114.thaumrota.common.event;

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
import com.artur114.thaumrota.common.config.RotAConfigs;
import com.artur114.thaumrota.common.blocks.BlockAncientWorldPortal;
import com.artur114.thaumrota.common.util.math.UltraMutableBlockPos;
import com.artur114.thaumrota.main.ThaumRotA;
import com.artur114.thaumrota.server.event.PublicSStoppingEvent;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.DifficultyChangeEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
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
    private static boolean newVersion = false;
    private static byte difficultyId = -1;


    public static byte getDifficultyId() {return difficultyId;}


    @SubscribeEvent
    public static void difficultyEvent(DifficultyChangeEvent e) {
        EnumDifficulty d = e.getDifficulty();
        difficultyId = d == EnumDifficulty.PEACEFUL ? 0 : d == EnumDifficulty.EASY ? 1 : d == EnumDifficulty.NORMAL ? 2 : d == EnumDifficulty.HARD ? 3 : (byte) -1;
    }

    @SubscribeEvent
    public static void playerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent e) {
        if (!e.player.world.isRemote) {
            EntityPlayerMP player = (EntityPlayerMP) e.player;


            if (newVersion) {
                MiscHandler.sendMessageTranslate(player, ThaumRotA.MODID + ".message.new-version");
                newVersion = false;
            }
        }
    }

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
        if (!RotAConfigs.AncientWorldSettings.isDeadToAncientWorld) {
            if (e.getEntity() instanceof EntityPlayerMP) {
                if (e.getEntity().dimension == ANCIENT_WORLD_ID) {
                    EntityPlayerMP player = (EntityPlayerMP) e.getEntity();
                    if (player.getHealth() - e.getAmount() <= 0) {
                        e.setCanceled(true);
                        onPlayerLost(player);
                        return;
                    }

                    double additionalOffset = RotAConfigs.DifficultySettings.additionalOffset;
                    int ignoringOffset = RotAConfigs.DifficultySettings.ignoringOffset;
                    int baseChange = RotAConfigs.DifficultySettings.baseChange;

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
                if (RotAConfigs.AncientWorldSettings.noPeaceful) {
                    if (difficultyId == 0) {
                        if (!e.player.world.isRemote) {
                            AncientPortalsProcessor.teleportToOverworld((EntityPlayerMP) e.player);
                        }
                    }
                }

                e.player.fallDistance = 0;
            }
        }
        if (e.player.ticksExisted % 20 == 0) {
            if (playerDimension != ANCIENT_WORLD_ID) {
                if (e.player.getEntityData().getBoolean(BlockAncientWorldPortal.noCollisionNBT)) {
                    IPlayerTimer timer = InitCapabilities.getTimer(e.player);
                    if (timer.getTime("notCollisionTime") >= 8 * 20) {
                        e.player.getEntityData().setBoolean(BlockAncientWorldPortal.noCollisionNBT, false);
                        timer.delete("notCollisionTime");
                    }
                    timer.addTime(20, "notCollisionTime");
                }
            }
        }
        if (e.player.ticksExisted % 40 == 0) {
            checkResearch(e.player);
            tickTimer(e.player);
        }
    }

    protected static void checkResearch(EntityPlayer player) {
        UltraMutableBlockPos blockPos = UltraMutableBlockPos.obtain();
        if (AncientPortalsProcessor.hasPortalOnWorld(player.world)) {
            blockPos.setPos(AncientPortalsProcessor.getNearestPortalPos(player.world, blockPos.setPos(player))).add(8, 0, 8).setY(MathHelper.floor(player.posY));
            if (blockPos.distance(player) < 32) {
                MiscHandler.researchAndSendMessage((EntityPlayerMP) player, "m_FOUND_ANCIENT", ThaumRotA.MODID + ".text.found_portal");
            }
        }
        UltraMutableBlockPos.release(blockPos);
    }

    private static void tickTimer(EntityPlayer player) {
        IPlayerTimer timer = InitCapabilities.getTimer(player);
        if (timer.hasTimer("recovery")) {
            timer.addTime(40, "recovery");
            if (timer.getTime("recovery") >= 10000) {
                timer.delete("recovery");
                MiscHandler.researchAndSendMessage((EntityPlayerMP) player, "RECOVERY", ThaumRotA.MODID + ".text.recovery");
            }
        }
        if (timer.hasTimer("poisoning")) {
            timer.addTime(40, "poisoning");
        }
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity().dimension == ANCIENT_WORLD_ID && !event.getWorld().isRemote) {
            if (RotAConfigs.DifficultySettings.speedAmplifier > 0 && event.getEntity() instanceof EntityLiving && !(event.getEntity() instanceof EntityPlayer)) {
                EntityLiving living = (EntityLiving) event.getEntity();
                if (living.isNonBoss() || RotAConfigs.DifficultySettings.iaAddSpeedEffectToBoss) {
                    living.addPotionEffect(new PotionEffect(MobEffects.SPEED, Integer.MAX_VALUE, RotAConfigs.DifficultySettings.speedAmplifier - 1, false, false));
                }
            }
        }
    }

//    @SubscribeEvent
//    public void canDeSpawn(LivingSpawnEvent.AllowDespawn event) {
////        if (event.getEntity().dimension == ancient_world_dim_id) {
////            event.setResult(Event.Result.DENY);
////        }
//    }

    @SubscribeEvent
    public static void canSpawn(LivingSpawnEvent.CheckSpawn e) {
        if (e.getEntity().dimension == ANCIENT_WORLD_ID && e.getWorld().canSeeSky(e.getEntity().getPosition())) {
            e.setResult(Event.Result.DENY);
        }
        Biome biome = e.getWorld().getBiome(e.getEntity().getPosition());
        if (biome instanceof BiomeTaint) {
            if (!BiomeTaint.canSpawn(e.getEntity(), (BiomeTaint) biome)) {
                e.setResult(Event.Result.DENY);
            }
        }
    }

    private static byte bTick = 0;

    @SubscribeEvent
    public static void tick(TickEvent.WorldTickEvent e) {
        if (!e.world.isRemote) {
            if (e.phase != TickEvent.Phase.START) {
                return;
            }

            if (e.world.provider.getDimension() == 0) {
                bTick++;
                if (bTick >= 40) {
                    bTick = 0;
                    int taintChunks = 0;
                    for (Chunk chunk : ((WorldServer) e.world).getChunkProvider().getLoadedChunks()) {
                        if (chunk == null) {
                            continue;
                        }

                        if (MiscHandler.fastCheckChunkContainsBiomeType(chunk, InitBiomes.TAINT_TYPE_L)) {
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
        newVersion = false;

        SHORT_CHUNK_LOAD_MANAGER.unload();
        TIMER_TASKS_MANAGER.unload();
        SLOW_BUILD_MANAGER.unload();
    }
}