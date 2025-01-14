package com.artur.returnoftheancients.handlers;

import com.artur.returnoftheancients.ancientworldgeneration.main.AncientWorld;
import com.artur.returnoftheancients.ancientworldgeneration.structurebuilder.CustomGenStructure;
import com.artur.returnoftheancients.capabilities.IPlayerTimerCapability;
import com.artur.returnoftheancients.capabilities.PlayerTimer;
import com.artur.returnoftheancients.capabilities.TRACapabilities;
import com.artur.returnoftheancients.generation.biomes.BiomeTaint;
import com.artur.returnoftheancients.generation.generators.portal.base.AncientPortalsProcessor;
import com.artur.returnoftheancients.init.InitBiome;
import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.misc.WorldData;
import com.artur.returnoftheancients.blocks.TpToAncientWorldBlock;
import com.artur.returnoftheancients.misc.WorldDataFields;
import com.artur.returnoftheancients.referense.Referense;
import com.artur.returnoftheancients.utils.interfaces.IALGS;
import net.minecraft.command.CommandWeather;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.DifficultyChangeEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.common.entities.monster.EntityEldritchGuardian;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import static com.artur.returnoftheancients.init.InitDimensions.ancient_world_dim_id;

@Mod.EventBusSubscriber(modid = Referense.MODID)
public class ServerEventsHandler {
    public static final String tpToHomeNBT = "tpToHomeNBT";
    protected static final String startUpNBT = "startUpNBT";
    protected static final String notNoCollisionNBTTime = "notNoCollisionNBTTime";
    private static boolean isAncientAreaSet = false;
    private static byte difficultyId = -1;
    private static boolean newVersion = false;


    public  static void unload() {
        isAncientAreaSet = false;
        newVersion = false;
    }

    public static byte getDifficultyId() {return difficultyId;}


    @SubscribeEvent
    public static void DifficultyEvent(DifficultyChangeEvent e) {
        EnumDifficulty d = e.getDifficulty();
        difficultyId = d == EnumDifficulty.PEACEFUL ? 0 : d == EnumDifficulty.EASY ? 1 : d == EnumDifficulty.NORMAL ? 2 : d == EnumDifficulty.HARD ? 3 : (byte) -1;
    }

    @SubscribeEvent
    public static void playerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent e) {
        if (e.player instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) e.player;

            WorldDataFields.sync(player);
            if (player.dimension == ancient_world_dim_id) AncientWorld.playerJoinBuss(player);

            if (newVersion) {
                HandlerR.sendMessageTranslate(player, Referense.MODID + ".message.new-version");
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

            if (e.getWorld().provider.getDimension() == TRAConfigs.PortalSettings.dimensionGenerate) {
                if (TRAConfigs.PortalSettings.isGen) {
                    if (!worldData.saveData.getBoolean(IALGS.isAncientPortalGenerateKey)) {

                        int x;
                        int z;

                        if (TRAConfigs.PortalSettings.isRandomGenerate) {
                            Random rand = new Random(e.getWorld().getSeed());
                            x = rand.nextInt(TRAConfigs.PortalSettings.generationRange) - TRAConfigs.PortalSettings.generationRange / 2;
                            z = rand.nextInt(TRAConfigs.PortalSettings.generationRange) - TRAConfigs.PortalSettings.generationRange / 2;
                        } else {
                            x = TRAConfigs.PortalSettings.chunkX;
                            z = TRAConfigs.PortalSettings.chunkZ;
                        }

                        worldData.saveData.setString("version", Referense.VERSION);
                        worldData.saveData.setInteger(IALGS.ancientPortalXPosKey, x);
                        worldData.saveData.setInteger(IALGS.ancientPortalZPosKey, z);
                        worldData.saveData.setInteger(IALGS.ancientPortalYPosKey, HandlerR.calculateGenerationHeight(e.getWorld(), (16 * x) + 8, (16 * z) + 8));
                        HandlerR.genAncientPortal(e.getWorld(), x, z, TRAConfigs.PortalSettings.dimensionGenerate == 0);
                        worldData.saveData.setInteger(IALGS.portalDimension, TRAConfigs.PortalSettings.dimensionGenerate);
                        worldData.saveData.setBoolean(IALGS.isAncientPortalGenerateKey, true);
                        worldData.markDirty();
                        WorldDataFields.reload();
                    }
                }
                checkVersion();
            }
            if (e.getWorld().provider.getDimension() == ancient_world_dim_id) {
                if (!isAncientAreaSet) {
                    CustomGenStructure.gen(e.getWorld(), -16, 240, -16, "ancient_area");
                    isAncientAreaSet = true;
                }
            }
            WorldDataFields.reload();
        }
        for (EntityPlayer player : e.getWorld().playerEntities) {
            player.getEntityData().setBoolean("isUUI", false);
        }
    }

    public static void checkVersion() {
        WorldData worldData = WorldData.get();
        if (!worldData.saveData.hasKey("version")) {
            if (worldData.saveData.getBoolean(IALGS.isAncientPortalGenerateKey)) {
                worldData.saveData.setInteger(IALGS.ancientPortalXPosKey, 0);
                worldData.saveData.setInteger(IALGS.ancientPortalZPosKey, 0);
                worldData.markDirty();
                WorldDataFields.reload();
                newVersion = true;
                System.out.println("new version!");
            }
            worldData.saveData.setString("version", "1.2.0-betta");
            return;
        }
        if (worldData.saveData.getString("version").contains("1.3") && !worldData.saveData.getString("version").contains("1.3.5")) {
            worldData.saveData.setInteger(IALGS.portalDimension, 0);
            worldData.saveData.setString("version", "1.3.4-betta");
        }
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
                    if (HandlerR.getIgnoringChance((int) (baseChange + (ignoringOffset * hurt)), player.world.rand)) {
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
            if (!e.getPlayer().isCreative() && !e.getState().getBlock().equals(BlocksTC.stoneArcane)) {
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
        int playerDimension = e.player.dimension;
        if (!e.player.world.isRemote) {
            if (playerDimension == ancient_world_dim_id) {
                if (e.player.posY > 84 && !e.player.isCreative()) {
                    e.player.fallDistance = 0;
                    e.player.motionY += -1 - e.player.motionY;
                }
            }
        }
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
            }
        }
        if (playerDimension != ancient_world_dim_id && e.player.ticksExisted % 20 == 0) {
            if (e.player.getEntityData().getBoolean(TpToAncientWorldBlock.noCollisionNBT)) {
                e.player.getEntityData().setInteger(notNoCollisionNBTTime, (e.player.getEntityData().getInteger(notNoCollisionNBTTime) + 1));
                if (e.player.getEntityData().getInteger(notNoCollisionNBTTime) >= 8) {
                    e.player.getEntityData().setBoolean(TpToAncientWorldBlock.noCollisionNBT, false);
                    e.player.getEntityData().setInteger(notNoCollisionNBTTime, 0);
                }
            }
        }
        if (e.player.ticksExisted % 40 == 0) {
            if (!e.player.world.isRemote) {
                checkResearch(e.player);
                tickTimer(e.player);
            }
        }
    }

    protected static void checkResearch(EntityPlayer player) {
        if (HandlerR.isWithinRadius(player.posX, player.posZ, WorldDataFields.portalX, WorldDataFields.portalZ, 20)) {
            HandlerR.researchAndSendMessage((EntityPlayerMP) player, "m_FOUND_ANCIENT", Referense.MODID + ".text.found_portal");
        }
    }

    private static void tickTimer(EntityPlayer player) {
        IPlayerTimerCapability timer = TRACapabilities.getTimer(player);
        if (timer.hasTimer("recovery")) {
            timer.addTime(40, "recovery");
            if (timer.getTime("recovery") >= 10000) {
                timer.delete("recovery");
                HandlerR.researchAndSendMessage((EntityPlayerMP) player, "RECOVERY", Referense.MODID + ".text.recovery");
            }
        }
    }


    static int maxDuration = 999999999;

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity().dimension == ancient_world_dim_id && !event.getWorld().isRemote) {
            if (!event.getEntity().isNonBoss()) {
                AncientWorld.bossJoinBus(event);
            }
            if (event.getEntity() instanceof EntityLiving && !(event.getEntity() instanceof EntityPlayer)) {
                EntityLiving living = (EntityLiving) event.getEntity();
                if (living.isNonBoss() || TRAConfigs.DifficultySettings.iaAddSpeedEffectToBoss) {
                    living.addPotionEffect(new PotionEffect(MobEffects.SPEED, maxDuration, TRAConfigs.DifficultySettings.speedAmplifier - 1, false, false));
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
            bTick++;
            if (e.world.provider.getDimension() == 0 && bTick >= 40) {
                bTick = 0;
                int taintChunks = 0;
                for (Chunk chunk : ((WorldServer) e.world).getChunkProvider().getLoadedChunks()) {
                    if (chunk == null) {
                        continue;
                    }

                    byte[] biomes = chunk.getBiomeArray();

                    if (HandlerR.arrayContainsAny(biomes, InitBiome.TAINT_BIOMES_ID)) {
                        BiomeTaint.chunkHasBiomeUpdate(chunk);
                        taintChunks++;
                    }
                }
                BiomeTaint.taintChunks = taintChunks;
            }
        }
    }


//    @SubscribeEvent
//    public static void ChunkEventLoad(ChunkEvent.Load e) {
//    }
}