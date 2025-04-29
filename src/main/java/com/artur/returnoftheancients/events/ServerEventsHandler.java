package com.artur.returnoftheancients.events;

import com.artur.returnoftheancients.ancientworldlegacy.main.AncientWorld;
import com.artur.returnoftheancients.handlers.MiscHandler;
import com.artur.returnoftheancients.structurebuilderlegacy.CustomGenStructure;
import com.artur.returnoftheancients.capabilities.IPlayerTimerCapability;
import com.artur.returnoftheancients.capabilities.PlayerTimer;
import com.artur.returnoftheancients.capabilities.TRACapabilities;
import com.artur.returnoftheancients.generation.biomes.BiomeTaint;
import com.artur.returnoftheancients.generation.portal.base.AncientPortalsProcessor;
import com.artur.returnoftheancients.events.eventmanagers.PlayerInBiomeManager;
import com.artur.returnoftheancients.events.eventmanagers.ShortChunkLoadManager;
import com.artur.returnoftheancients.events.eventmanagers.TimerTasksManager;
import com.artur.returnoftheancients.init.InitBiome;
import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.misc.WorldData;
import com.artur.returnoftheancients.blocks.BlockTpToAncientWorld;
import com.artur.returnoftheancients.misc.WorldDataFields;
import com.artur.returnoftheancients.referense.Referense;
import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.DifficultyChangeEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
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

import static com.artur.returnoftheancients.init.InitDimensions.ancient_world_dim_id;

@Mod.EventBusSubscriber(modid = Referense.MODID)
public class ServerEventsHandler {
    public static final ShortChunkLoadManager SHORT_CHUNK_LOAD_MANAGER = new ShortChunkLoadManager();
    public static final PlayerInBiomeManager PLAYER_IN_BIOME_MANAGER = new PlayerInBiomeManager();
    public static final TimerTasksManager TIMER_TASKS_MANAGER = new TimerTasksManager();

    private static boolean isAncientAreaSet = false;
    private static byte difficultyId = -1;
    private static boolean newVersion = false;


    public static byte getDifficultyId() {return difficultyId;}


    @SubscribeEvent
    public static void DifficultyEvent(DifficultyChangeEvent e) {
        EnumDifficulty d = e.getDifficulty();
        difficultyId = d == EnumDifficulty.PEACEFUL ? 0 : d == EnumDifficulty.EASY ? 1 : d == EnumDifficulty.NORMAL ? 2 : d == EnumDifficulty.HARD ? 3 : (byte) -1;
    }

    @SubscribeEvent
    public static void playerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent e) {
        if (!e.player.world.isRemote) {
            EntityPlayerMP player = (EntityPlayerMP) e.player;

            WorldDataFields.sync(player);
            if (player.dimension == ancient_world_dim_id) AncientWorld.playerJoinBuss(player);

            if (newVersion) {
                MiscHandler.sendMessageTranslate(player, Referense.MODID + ".message.new-version");
                newVersion = false;
            }
        }
    }

    @SubscribeEvent
    public static void playerLoggedOutEvent(PlayerEvent.PlayerLoggedOutEvent e) {
        if (e.player instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) e.player;
            if (player.dimension == ancient_world_dim_id) AncientWorld.playerLoggedOutBus(player.getUniqueID());
        }
    }


    @SubscribeEvent
    public static void WorldEventLoad(WorldEvent.Load e) {
        if (!e.getWorld().isRemote) {
            WorldData worldData = WorldData.get();
            if (!worldData.saveData.hasKey("version")) {
                worldData.saveData.setString("version", Referense.VERSION);
            }
            if (e.getWorld().provider.getDimension() == ancient_world_dim_id) {
                if (!isAncientAreaSet) {
                    CustomGenStructure.gen(e.getWorld(), -16, 240, -16, "ancient_area");
                    isAncientAreaSet = true;
                }
                checkVersion();
            }
            WorldDataFields.reload();
        }
    }

    public static void checkVersion() {
        WorldData worldData = WorldData.get();
        if (!worldData.saveData.getString("version").equals(Referense.VERSION)) {
            newVersion(worldData);
        }
    }

    private static void newVersion(WorldData worldData) {
        newVersion = true;
        System.out.println("new version!");
        worldData.saveData.setString("version", Referense.VERSION);
        worldData.markDirty();
    }

    @SubscribeEvent
    public static void LivingDeathEvent(LivingDeathEvent e) {
        World world = e.getEntity().world;
        if (!e.getEntity().isNonBoss() && world.provider.getDimension() == ancient_world_dim_id && !world.isRemote) {
            AncientWorld.bossDeadBus(e.getEntity().getUniqueID());
        }
    }

    @SubscribeEvent
    public static void LivingHurtEvent(LivingHurtEvent e) {
        if (!TRAConfigs.AncientWorldSettings.isDeadToAncientWorld) {
            if (e.getEntity() instanceof EntityPlayerMP) {
                if (e.getEntity().dimension == ancient_world_dim_id) {
                    EntityPlayerMP player = (EntityPlayerMP) e.getEntity();
                    if (player.getHealth() - e.getAmount() <= 0) {
                        e.setCanceled(true);
                        onPlayerLost(player);
                        return;
                    }

                    double additionalOffset = TRAConfigs.DifficultySettings.additionalOffset;
                    int ignoringOffset = TRAConfigs.DifficultySettings.ignoringOffset;
                    int baseChange = TRAConfigs.DifficultySettings.baseChange;

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
        player.setHealth(20);
        if (!AncientWorld.playerLostBus(player.getUniqueID())) AncientPortalsProcessor.tpToHome(player);
    }

    @SubscribeEvent
    public static void LivingDropsEvent(LivingDropsEvent e) {
        if (e.getEntity().dimension == ancient_world_dim_id && !e.getEntity().isNonBoss()) {
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void BreakEvent(BlockEvent.BreakEvent e) {
        if (e.getPlayer().dimension == ancient_world_dim_id) {
            if (!e.getPlayer().isCreative() && e.getState().getBlock() != BlocksTC.stoneArcane) {
                e.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void PlaceEvent(BlockEvent.PlaceEvent e) {
        if (e.getPlayer().dimension == ancient_world_dim_id) {
            if (!e.getPlayer().isCreative()) {
                e.getPlayer().addItemStackToInventory(e.getItemInHand().splitStack(1));
                e.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void PlayerTickEvent(TickEvent.PlayerTickEvent e) {

        if (e.phase != TickEvent.Phase.START || e.player.world.isRemote) {
            return;
        }

        PLAYER_IN_BIOME_MANAGER.tickEventPlayerTickEvent(e);

        int playerDimension = e.player.dimension;
        if (e.player.ticksExisted % 4 == 0) {
            if (playerDimension == ancient_world_dim_id) {
                if (TRAConfigs.AncientWorldSettings.noNightVision) {
                    if (e.player.getActivePotionEffect(MobEffects.NIGHT_VISION) != null && !e.player.isCreative()) {
                        e.player.removePotionEffect(MobEffects.NIGHT_VISION);
                    }
                }
                if (TRAConfigs.AncientWorldSettings.noPeaceful) {
                    if (difficultyId == 0) {
                        if (!e.player.world.isRemote) {
                            AncientPortalsProcessor.tpToHome((EntityPlayerMP) e.player);
                        }
                    }
                }

                e.player.fallDistance = 0;
            }
        }
        if (e.player.ticksExisted % 20 == 0) {
            if (playerDimension != ancient_world_dim_id) {
                if (e.player.getEntityData().getBoolean(BlockTpToAncientWorld.noCollisionNBT)) {
                    IPlayerTimerCapability timer = TRACapabilities.getTimer(e.player);
                    if (timer.getTime("notCollisionTime") >= 8 * 20) {
                        e.player.getEntityData().setBoolean(BlockTpToAncientWorld.noCollisionNBT, false);
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
        UltraMutableBlockPos blockPos = UltraMutableBlockPos.getBlockPosFromPoll();
        if (AncientPortalsProcessor.hasPortalOnWorld(player.world)) {
            blockPos.setPos(AncientPortalsProcessor.getNearestPortalPos(player.world, blockPos.setPos(player))).add(8, 0, 8).setY(MathHelper.floor(player.posY));
            if (blockPos.distance(player) < 32) {
                MiscHandler.researchAndSendMessage((EntityPlayerMP) player, "m_FOUND_ANCIENT", Referense.MODID + ".text.found_portal");
            }
        }
        UltraMutableBlockPos.returnBlockPosToPoll(blockPos);
    }

    private static void tickTimer(EntityPlayer player) {
        IPlayerTimerCapability timer = TRACapabilities.getTimer(player);
        if (timer.hasTimer("recovery")) {
            timer.addTime(40, "recovery");
            if (timer.getTime("recovery") >= 10000) {
                timer.delete("recovery");
                MiscHandler.researchAndSendMessage((EntityPlayerMP) player, "RECOVERY", Referense.MODID + ".text.recovery");
            }
        }
        if (timer.hasTimer("poisoning")) {
            timer.addTime(40, "poisoning");
        }
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity().dimension == ancient_world_dim_id && !event.getWorld().isRemote) {
            if (!event.getEntity().isNonBoss()) {
                AncientWorld.bossJoinBus(event);
            }
            if (TRAConfigs.DifficultySettings.speedAmplifier > 0 && event.getEntity() instanceof EntityLiving && !(event.getEntity() instanceof EntityPlayer)) {
                EntityLiving living = (EntityLiving) event.getEntity();
                if (living.isNonBoss() || TRAConfigs.DifficultySettings.iaAddSpeedEffectToBoss) {
                    living.addPotionEffect(new PotionEffect(MobEffects.SPEED, Integer.MAX_VALUE, TRAConfigs.DifficultySettings.speedAmplifier - 1, false, false));
                }
            }
        }
    }

    @SubscribeEvent
    public void canDeSpawn(LivingSpawnEvent.AllowDespawn event) {
//        if (event.getEntity().dimension == ancient_world_dim_id) {
//            event.setResult(Event.Result.DENY);
//        }
    }

    @SubscribeEvent
    public static void canSpawn(LivingSpawnEvent.CheckSpawn e) {
        if (e.getEntity().dimension == ancient_world_dim_id && e.getEntity().world.canSeeSky(e.getEntity().getPosition())) {
            e.setResult(Event.Result.DENY);
        }
    }

    @SubscribeEvent
    public static void attachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityPlayer) {
            event.addCapability(PlayerTimer.Provider.NAME, new PlayerTimer.Provider());
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

                        if (MiscHandler.fastCheckChunkContainsAnyOnBiomeArray(chunk, InitBiome.TAINT_BIOMES_L_ID)) {
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
    }

    @SubscribeEvent
    public static void saveEvent(WorldEvent.Save e) {
        SHORT_CHUNK_LOAD_MANAGER.worldEventSave(e);
    }

    public static void unload() {
        isAncientAreaSet = false;
        newVersion = false;

        SHORT_CHUNK_LOAD_MANAGER.unload();
        TIMER_TASKS_MANAGER.unload();
    }
}