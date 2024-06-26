package com.artur.returnoftheancients.handlers;

import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.misc.WorldData;
import com.artur.returnoftheancients.blocks.TpToAncientWorldBlock;
import com.artur.returnoftheancients.generation.generators.GenStructure;
import com.artur.returnoftheancients.init.InitBiome;
import com.artur.returnoftheancients.utils.interfaces.IALGS;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.DifficultyChangeEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thaumcraft.api.capabilities.IPlayerWarp;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.client.lib.events.RenderEventHandler;

import static com.artur.returnoftheancients.init.InitDimensions.ancient_world_dim_id;


public class EventsHandler {

    public static final String tpToHomeNBT = "tpToHomeNBT";
    private static final String startUpNBT = "startUpNBT";
    private boolean capIsSet = true;
    public static boolean bossIsDead = false;
    private static byte pT = 0;
    private static boolean isAncientWorldLoad = false;
    private static byte difficultyId = -1;


    public static boolean isAncientWorldLoad() {return isAncientWorldLoad;}
    public static void setAncientWorldLoad(boolean ancientWorldLoad) {isAncientWorldLoad = ancientWorldLoad;}
    public static byte getDifficultyId() {return difficultyId;}

    public static void tpToHome(EntityPlayerMP player) {
        player.getEntityData().setBoolean(TpToAncientWorldBlock.noCollisionNBT, true);
        FreeTeleporter.teleportToDimension(player, 0, TRAConfigs.PortalSettings.x + 3, 3, TRAConfigs.PortalSettings.z + 3);
        player.getEntityData().setBoolean(tpToHomeNBT, true);
    }

    @SubscribeEvent
    public void DifficultyEvent(DifficultyChangeEvent e) {
        EnumDifficulty d = e.getDifficulty();
        difficultyId = d == EnumDifficulty.PEACEFUL ? 0 : d == EnumDifficulty.EASY ? 1 : d == EnumDifficulty.NORMAL ? 2 : d == EnumDifficulty.HARD ? 3 : (byte) -1;
    }


    @SubscribeEvent
    public void WorldEventLoad(WorldEvent.Load e) {
        if (!e.getWorld().isRemote) {
            if (TRAConfigs.PortalSettings.isGen) {
                WorldData worldData = WorldData.get();
                if (TRAConfigs.PortalSettings.toBedrock) {
                    if (!worldData.saveData.getBoolean(IALGS.isAncientPortalGenerateKey) && e.getWorld().provider.getDimension() == 0) {
                        if (!worldData.saveData.hasKey(IALGS.AncientPortalYPosKey)) {
                            worldData.saveData.setInteger(IALGS.AncientPortalYPosKey, HandlerR.CalculateGenerationHeight(e.getWorld(), TRAConfigs.PortalSettings.x, TRAConfigs.PortalSettings.z));
                        }
                        HandlerR.genAncientPortal(e.getWorld(), TRAConfigs.PortalSettings.x, TRAConfigs.PortalSettings.y, TRAConfigs.PortalSettings.z, true);
                        worldData.saveData.setBoolean(IALGS.isAncientPortalGenerateKey, true);
                    }
                } else {
                    GenStructure.generateStructure(e.getWorld(), TRAConfigs.PortalSettings.x, HandlerR.CalculateGenerationHeight(e.getWorld(), TRAConfigs.PortalSettings.x, TRAConfigs.PortalSettings.z) + TRAConfigs.PortalSettings.y, TRAConfigs.PortalSettings.z, "ancient_portal_no_bedrock");
                }
                worldData.markDirty();
            }
        }
        for (EntityPlayer player : e.getWorld().playerEntities) {
            player.getEntityData().setBoolean("isUUI", false);
        }
        if (e.getWorld().provider.getDimension() == ancient_world_dim_id) {
            isAncientWorldLoad = true;
        }
    }

    @SubscribeEvent
    public void LivingDeathEvent(LivingDeathEvent e) {
        World world = e.getEntity().world;
        if (!e.getEntity().isNonBoss() && world.provider.getDimension() == ancient_world_dim_id && !world.isRemote) {
            WorldData worldData = WorldData.get();
            if (!worldData.saveData.getBoolean(IALGS.isPrimalBladeDropKey)) {
                GenStructure.generateStructure(world, (int) e.getEntity().posX, (int) e.getEntity().posY, (int) e.getEntity().posZ, "ancient_loot");
                worldData.saveData.setBoolean(IALGS.isPrimalBladeDropKey, true);
                worldData.markDirty();
            }
            bossIsDead = true;
            System.out.println("Boss is dead");
        }
    }

    @SubscribeEvent
    public void LivingHurtEvent(LivingHurtEvent e) {
        if (!TRAConfigs.AncientWorldSettings.isDeadToAncientWorld) {
            if (e.getEntity() instanceof EntityPlayerMP) {
                if (e.getEntity().dimension == ancient_world_dim_id) {
                    EntityPlayer player = (EntityPlayer) e.getEntity();
                        if (player.getHealth() - e.getAmount() <= 0) {
                            e.setCanceled(true);
                            player.setHealth(20);
                            tpToHome((EntityPlayerMP) player);
                            System.out.println("You dead");
                            return;
                        }
                    if (HandlerR.genRandomIntRange(0, TRAConfigs.AncientWorldSettings.chanceIgnoringArmor) == 0) {
                        player.setHealth(player.getHealth() - e.getAmount());
                        e.setCanceled(true);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void LivingDropsEvent(LivingDropsEvent e) {
        if (e.getEntity().dimension == ancient_world_dim_id && !e.getEntity().isNonBoss()) {
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void BreakEvent(BlockEvent.BreakEvent e) {
        if (e.getPlayer().dimension == ancient_world_dim_id) {
            if (!e.getPlayer().isCreative())
                e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void PlaceEvent(BlockEvent.PlaceEvent e) {
        if (e.getPlayer().dimension == ancient_world_dim_id) {
            if (!e.getPlayer().isCreative()) {
                e.getPlayer().addItemStackToInventory(e.getItemInHand().splitStack(1));
                e.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void fogSetColor(EntityViewRenderEvent.FogColors e) {
        if (e.getEntity().getEntityWorld().getBiome(e.getEntity().getPosition()) == InitBiome.ANCIENT_LABYRINTH) {
            e.setRed(0);
            e.setBlue(0);
            e.setGreen(0);
        }
    }

    @SubscribeEvent
    public void PlayerTickEvent(TickEvent.PlayerTickEvent e) {
        int playerDimension = e.player.dimension;
        if (e.player.getEntityData().getBoolean(startUpNBT)) {
            if (playerDimension != ancient_world_dim_id) {
                if (e.player instanceof EntityPlayerSP) {
                    EntityPlayerSP playerSP = (EntityPlayerSP) e.player;
                    playerSP.motionY += 2 - playerSP.motionY;
                }
                if (e.player instanceof EntityPlayerMP) {
                    if (e.player.posY > WorldData.get().saveData.getInteger(IALGS.AncientPortalYPosKey)) {
                        EntityPlayerMP playerMP = (EntityPlayerMP) e.player;

                        HandlerR.setStartUpNBT(playerMP, false);
                        playerMP.removePotionEffect(Potion.getPotionById(15));
                        playerMP.fallDistance = 0;

                        e.player.getEntityData().setBoolean(TpToAncientWorldBlock.noCollisionNBT, false);
                        e.player.getEntityData().setBoolean(startUpNBT, false);

                        System.out.println("Effects clear");
                    }
                }
            }
        }

        if (e.player.getEntityData().getBoolean(tpToHomeNBT)) {
            BlockPos pos = e.player.getPosition();
            if (playerDimension != ancient_world_dim_id && pos.getY() <= 3) {
                if (e.player.getServer() != null) {
                    if (e.player instanceof EntityPlayerMP) {
                        GameSettings Settings = Minecraft.getMinecraft().gameSettings;
                        EntityPlayerMP playerMP = (EntityPlayerMP) e.player;
                        if (TRAConfigs.AncientWorldSettings.isSetWarp) {
                            IPlayerWarp playerWarp = ThaumcraftCapabilities.getWarp(e.player);
                            playerWarp.set(IPlayerWarp.EnumWarpType.PERMANENT, e.player.getEntityData().getInteger("PERMANENT"));
                            playerWarp.set(IPlayerWarp.EnumWarpType.TEMPORARY, e.player.getEntityData().getInteger("TEMPORARY"));
                            playerWarp.set(IPlayerWarp.EnumWarpType.NORMAL, e.player.getEntityData().getInteger("NORMAL"));
                            playerWarp.sync(playerMP);
                            e.player.getEntityData().setBoolean("isWarpSet", false);
                        }
                        if (TRAConfigs.AncientWorldSettings.cantChangeRenderDistanceChunks) {
                            int r = e.player.getEntityData().getInteger("renderDistanceChunks");
                            if (r != 0) {
                                Settings.renderDistanceChunks = r;
                            }
                            e.player.getEntityData().setInteger("renderDistanceChunks", 0);
                        }
                        if (TRAConfigs.AncientWorldSettings.cantChangeGammaSetting) {
                            float g = e.player.getEntityData().getFloat("gammaSetting");
                            e.player.getEntityData().setFloat("gammaSetting", 0.00001f);
                            Settings.gammaSetting = g;
                        }
                        e.player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 600, 1));
                        e.player.getEntityData().setBoolean(tpToHomeNBT, false);
                        HandlerR.setStartUpNBT(playerMP, true);
                    }
                }
            }
        }

        if (playerDimension == ancient_world_dim_id) {
            IPlayerWarp playerWarp = ThaumcraftCapabilities.getWarp(e.player);
            GameSettings Settings = Minecraft.getMinecraft().gameSettings;
            EntityPlayerSP playerSP = Minecraft.getMinecraft().player;
            BlockPos pos = e.player.getPosition();
            if (pos.getY() > 82 && !e.player.isCreative()) {
                e.player.fallDistance = 0;
                e.player.motionY += -1 - e.player.motionY;
            }
            if (pT >= 4) {
                pT = 0;
                if (TRAConfigs.AncientWorldSettings.noNightVision) {
                    if (e.player.getActivePotionEffect(MobEffects.NIGHT_VISION) != null && !e.player.isCreative()) {
                        e.player.removePotionEffect(MobEffects.NIGHT_VISION);
                    }
                }
                if (TRAConfigs.AncientWorldSettings.isSetWarp) {
                    if (!e.player.getEntityData().getBoolean("isWarpSet") && e.player.getServer() != null) {
                        if (e.player instanceof EntityPlayerMP) {
                            e.player.getEntityData().setInteger("PERMANENT", playerWarp.get(IPlayerWarp.EnumWarpType.PERMANENT));
                            e.player.getEntityData().setInteger("TEMPORARY", playerWarp.get(IPlayerWarp.EnumWarpType.TEMPORARY));
                            e.player.getEntityData().setInteger("NORMAL", playerWarp.get(IPlayerWarp.EnumWarpType.NORMAL));
                            e.player.getEntityData().setBoolean("isWarpSet", true);
                            playerWarp.set(IPlayerWarp.EnumWarpType.TEMPORARY, 100);
                            EntityPlayerMP playerMP = (EntityPlayerMP) e.player;
                            playerWarp.sync(playerMP);
                        }
                    }
                }
                if (TRAConfigs.AncientWorldSettings.cantChangeGammaSetting) {
                    if (Settings.gammaSetting != 0 && !e.player.isCreative() && playerSP.dimension == ancient_world_dim_id) {
                        if (0.00001f == e.player.getEntityData().getFloat("gammaSetting")) {
                            e.player.getEntityData().setFloat("gammaSetting", Settings.gammaSetting);
                        }
                        Settings.gammaSetting = 0;
                    }
                }
                if (TRAConfigs.AncientWorldSettings.cantChangeRenderDistanceChunks && playerSP.dimension == ancient_world_dim_id) {
                    if (Settings.renderDistanceChunks != 4 && !e.player.isCreative()) {
                        if (0 == e.player.getEntityData().getInteger("renderDistanceChunks")) {
                            e.player.getEntityData().setInteger("renderDistanceChunks", Settings.renderDistanceChunks);
                        }
                        Settings.renderDistanceChunks = 4;
                    }
                }
                if (TRAConfigs.AncientWorldSettings.noPeaceful) {
                    if (difficultyId == 0) {
                        if (e.player instanceof EntityPlayerMP) {
                            HandlerR.sendMessageString((EntityPlayerMP) e.player, "PEACEFUL DIFFICULTY ???");
                            tpToHome((EntityPlayerMP) e.player);
                        }
                    }
                }
                if (e.player instanceof EntityPlayerSP && !e.player.isCreative()) {
                    RenderEventHandler.fogFiddled = true;
                    if (RenderEventHandler.fogDuration < 200) {
                        RenderEventHandler.fogDuration = 200;
                    }
                }
            }
            pT++;
        }
    }

    @SubscribeEvent
    public void PlayerChangedDimensionEvent(PlayerEvent.PlayerChangedDimensionEvent e) {
        if (e.toDim == ancient_world_dim_id && !e.player.world.isRemote) {
            if (e.player.isCreative()) {
                e.player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 999999, 0));
            }
        }
    }

    static byte wt = 0;
    static int maxDuration = 999999999;
    PotionEffect[] potionEffects = new PotionEffect[5];

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity().dimension == ancient_world_dim_id && !event.getWorld().isRemote) {
            if (!event.getEntity().isNonBoss()) {
                if (!WorldData.get().saveData.getBoolean(IALGS.isBossSpawn)) {
                    event.getWorld().removeEntity(event.getEntity());
                    event.setCanceled(true);
                }
            }
            if (event.getEntity() instanceof EntityLiving) {
                EntityLiving living = (EntityLiving) event.getEntity();
                living.addPotionEffect(new PotionEffect(MobEffects.SPEED, maxDuration, TRAConfigs.AncientWorldSettings.speedAmplifier - 1));
                for (PotionEffect potionEffect : potionEffects) {
                    if (potionEffect != null) {
                        living.addPotionEffect(potionEffect);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void WorldTick(TickEvent.WorldTickEvent e) {
        if (e.world.provider.getDimension() == ancient_world_dim_id) {
            if (wt == 20) {
                wt = 0;
                int players = e.world.playerEntities.size();
                int resistanceAmplifier = -1;
                int strengthAmplifier = -1;
                int fireResistanceAmplifier = -1;
                int regenerationAmplifier = -1;
                int invisibilityAmplifier = -1;

                if (players >= 6) {
                    resistanceAmplifier = ((players / 3) < 5) ? (players / 3) : 4;
                    regenerationAmplifier = ((players / 3) < 5) ? (players / 3) : 4;
                    invisibilityAmplifier = (HandlerR.genRandomIntRange(0, 4) == 0) ? 0 : -1;
                    strengthAmplifier = (players / 6);
                    fireResistanceAmplifier = 0;
                } else if (players > 3) {
                    resistanceAmplifier = 1;
                    strengthAmplifier = 1;
                    regenerationAmplifier = 1;
                    fireResistanceAmplifier = 0;
                } else if (players >= 2) {
                    resistanceAmplifier = 0;
                    strengthAmplifier = 0;
                    regenerationAmplifier = 0;
                } else  {
                    for (byte i = 0; i != potionEffects.length; i++) {
                        potionEffects[i] = null;
                    }
                }

                if (resistanceAmplifier != -1) {
                    potionEffects[0] = (new PotionEffect(MobEffects.RESISTANCE, maxDuration, resistanceAmplifier));
                }
                if (strengthAmplifier != -1) {
                    potionEffects[1] = (new PotionEffect(MobEffects.STRENGTH, maxDuration, strengthAmplifier));
                }
                if (regenerationAmplifier != -1) {
                    potionEffects[2] = (new PotionEffect(MobEffects.REGENERATION, maxDuration, regenerationAmplifier));
                }
                if (fireResistanceAmplifier != -1) {
                    potionEffects[3] = (new PotionEffect(MobEffects.FIRE_RESISTANCE, maxDuration, fireResistanceAmplifier));
                }
                if (invisibilityAmplifier != -1) {
                    potionEffects[4] = (new PotionEffect(MobEffects.INVISIBILITY, maxDuration, invisibilityAmplifier));
                }
            }
            wt++;
        }
    }


//    @SubscribeEvent
//    public void canDeSpawn(LivingSpawnEvent.AllowDespawn event) {
////        if (!(event.getEntityLiving() instanceof EntityEldritchGuardian)) {
////            return;
////        }
////        event.setResult(Event.Result.DENY);
//    }
//
//    @SubscribeEvent
//    public void ChunkEventLoad(ChunkEvent.Load e) {
////        if (e.getWorld().provider.getDimension() == ancient_world_dim_id) {
////            if (e.getChunk().getPos().equals(new ChunkPos(0, 0))) {
////                World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(ancient_world_dim_id);
////                EntityEldritchGuardian q = new EntityEldritchGuardian(world);
////                q.setPositionAndUpdate(8, 81, 8);
////                q.forceSpawn = true;
////                world.spawnEntity(q);
////                System.out.println("Eldritch Guardian is gen XYZ " + 8 + " " + 81 + " " + 8);
////            }
////        }
//    }
}