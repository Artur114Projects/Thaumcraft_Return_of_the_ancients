package com.artur.returnoftheancients.handlers;

import com.artur.returnoftheancients.ancientworldgeneration.main.AncientWorld;
import com.artur.returnoftheancients.ancientworldgeneration.structurebuilder.CustomGenStructure;
import com.artur.returnoftheancients.init.InitItems;
import com.artur.returnoftheancients.misc.PlayersCountDifficultyProcessor;
import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.misc.WorldData;
import com.artur.returnoftheancients.blocks.TpToAncientWorldBlock;
import com.artur.returnoftheancients.generation.generators.GenStructure;
import com.artur.returnoftheancients.referense.Referense;
import com.artur.returnoftheancients.utils.interfaces.IALGS;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemCompass;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.event.DifficultyChangeEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.AspectRegistryEvent;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.capabilities.IPlayerWarp;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.misc.PacketMiscEvent;

import java.util.Objects;

import static com.artur.returnoftheancients.init.InitDimensions.ancient_world_dim_id;

@Mod.EventBusSubscriber(modid = Referense.MODID)
public class ServerEventsHandler {

    public static final String tpToHomeNBT = "tpToHomeNBT";
    protected static final String startUpNBT = "startUpNBT";
    protected static final String notNoCollisionNBTTime = "notNoCollisionNBTTime";
    public static boolean bossIsDead = false;
    private static boolean isAncientAreaSet = false;
    private static byte difficultyId = -1;


    public static byte getDifficultyId() {return difficultyId;}

    public static void tpToHome(EntityPlayerMP player) {
        player.getEntityData().setBoolean(TpToAncientWorldBlock.noCollisionNBT, true);
        FreeTeleporter.teleportToDimension(player, 0, TRAConfigs.PortalSettings.x + 3, 3, TRAConfigs.PortalSettings.z + 3);
        player.getEntityData().setBoolean(tpToHomeNBT, true);
    }

    @SubscribeEvent
    public static void DifficultyEvent(DifficultyChangeEvent e) {
        EnumDifficulty d = e.getDifficulty();
        difficultyId = d == EnumDifficulty.PEACEFUL ? 0 : d == EnumDifficulty.EASY ? 1 : d == EnumDifficulty.NORMAL ? 2 : d == EnumDifficulty.HARD ? 3 : (byte) -1;
    }


    @SubscribeEvent
    public static void WorldEventLoad(WorldEvent.Load e) {
        if (!e.getWorld().isRemote) {
            WorldData worldData = WorldData.get();
            if (!worldData.saveData.hasKey("version")) {
                worldData.saveData.setString("version", Referense.VERSION);
            }
            if (e.getWorld().provider.getDimension() == 0) {
                if (TRAConfigs.PortalSettings.isGen) {
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
            if (e.getWorld().provider.getDimension() == ancient_world_dim_id) {
                if (!isAncientAreaSet) {
                    CustomGenStructure.gen(e.getWorld(), -16, 240, -16, "ancient_area");
                    CustomGenStructure.delete("ancient_area");
                    isAncientAreaSet = true;
                }
            }
        }
        for (EntityPlayer player : e.getWorld().playerEntities) {
            player.getEntityData().setBoolean("isUUI", false);
        }
    }

    @SubscribeEvent
    public static void LivingDeathEvent(LivingDeathEvent e) {
        World world = e.getEntity().world;
        if (!e.getEntity().isNonBoss() && world.provider.getDimension() == ancient_world_dim_id && !world.isRemote) {
            AncientWorld.bossDeadBuss(e.getEntity().getUniqueID());
        }
    }

    @SubscribeEvent
    public static void LivingHurtEvent(LivingHurtEvent e) {
        if (!TRAConfigs.AncientWorldSettings.isDeadToAncientWorld) {
            if (e.getEntity() instanceof EntityPlayerMP) {
                if (e.getEntity().dimension == ancient_world_dim_id) {
                    EntityPlayer player = (EntityPlayer) e.getEntity();
                        if (player.getHealth() - e.getAmount() <= 0) {
                            e.setCanceled(true);
                            player.setHealth(20);
                            AncientWorld.playerLostBuss(player.getUniqueID());
                            tpToHome((EntityPlayerMP) player);
                            return;
                        }
                    if (HandlerR.genRandomIntRange(0, TRAConfigs.DifficultySettings.chanceIgnoringArmor) == 0) {
                        player.setHealth(player.getHealth() - e.getAmount());
                        e.setCanceled(true);
                    }
                }
            }
        }
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
            if (!e.getPlayer().isCreative() && !e.getState().getBlock().equals(HandlerR.getBlockByString("thaumcraft:stone_arcane"))) {
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
        if (e.player.getEntityData().getBoolean(startUpNBT)) {
            if (playerDimension != ancient_world_dim_id) {
                if (e.player instanceof EntityPlayerMP) {
                    if (e.player.posY > WorldData.get().saveData.getInteger(IALGS.AncientPortalYPosKey)) {
                        EntityPlayerMP playerMP = (EntityPlayerMP) e.player;

                        HandlerR.setStartUpNBT(playerMP, false);
                        playerMP.removePotionEffect(Objects.requireNonNull(Potion.getPotionById(15)));
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
                        EntityPlayerMP playerMP = (EntityPlayerMP) e.player;
                        if (TRAConfigs.AncientWorldSettings.isSetWarp) {
                            IPlayerWarp playerWarp = ThaumcraftCapabilities.getWarp(e.player);
                            playerWarp.set(IPlayerWarp.EnumWarpType.PERMANENT, e.player.getEntityData().getInteger("PERMANENT"));
                            playerWarp.set(IPlayerWarp.EnumWarpType.TEMPORARY, e.player.getEntityData().getInteger("TEMPORARY"));
                            playerWarp.set(IPlayerWarp.EnumWarpType.NORMAL, e.player.getEntityData().getInteger("NORMAL"));
                            playerWarp.sync(playerMP);
                            e.player.getEntityData().setBoolean("isWarpSet", false);
                        }
                        e.player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 600, 1));
                        e.player.getEntityData().setBoolean(tpToHomeNBT, false);
                        HandlerR.setStartUpNBT(playerMP, true);
                    }
                }
            }
        }

        if (playerDimension == ancient_world_dim_id) {
            if (e.player.posY > 84 && !e.player.isCreative()) {
                e.player.fallDistance = 0;
                e.player.motionY += -1 - e.player.motionY;
            }
        }
        if (e.player.ticksExisted % 4 == 0) {
            if (playerDimension == ancient_world_dim_id) {
                if (!e.player.world.isRemote) {
                    PacketHandler.INSTANCE.sendToDimension(new PacketMiscEvent((byte)2), ancient_world_dim_id);
                }
                if (TRAConfigs.AncientWorldSettings.noNightVision) {
                    if (e.player.getActivePotionEffect(MobEffects.NIGHT_VISION) != null && !e.player.isCreative()) {
                        e.player.removePotionEffect(MobEffects.NIGHT_VISION);
                    }
                }
                if (TRAConfigs.AncientWorldSettings.isSetWarp) {
                    if (!e.player.getEntityData().getBoolean("isWarpSet") && e.player.getServer() != null) {
                        if (e.player instanceof EntityPlayerMP) {
                            IPlayerWarp playerWarp = ThaumcraftCapabilities.getWarp(e.player);
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
                if (TRAConfigs.AncientWorldSettings.noPeaceful) {
                    if (difficultyId == 0) {
                        if (e.player instanceof EntityPlayerMP) {
                            HandlerR.sendMessageString((EntityPlayerMP) e.player, "PEACEFUL DIFFICULTY ???");
                            tpToHome((EntityPlayerMP) e.player);
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
            if (e.player instanceof EntityPlayerMP) {
                checkResearch(e.player);
            }
        }
    }

    protected static void checkResearch(EntityPlayer player) {
        if (!ThaumcraftCapabilities.knowsResearchStrict(player, "m_ENTERANCIENT") && player.dimension == ancient_world_dim_id) {
            IPlayerKnowledge knowledge = ThaumcraftCapabilities.getKnowledge(player);
            if (knowledge.addResearch("m_ENTERANCIENT")) {
                knowledge.sync((EntityPlayerMP) player);
                player.sendStatusMessage(new TextComponentTranslation(Referense.MODID + ".text.entered_ancient").setStyle(new Style().setColor(TextFormatting.DARK_PURPLE)), true);
            }
        }
    }

    static byte wt = 0;
    static int maxDuration = 999999999;
    static final int[] potionEffects = new int[6];

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityPlayerMP) {
            AncientWorld.playerJoinBuss((EntityPlayerMP) event.getEntity());
        }
        if (event.getEntity().dimension == ancient_world_dim_id && !event.getWorld().isRemote) {
            if (!event.getEntity().isNonBoss()) {
                AncientWorld.bossJoinBuss(event);
            }
            if (event.getEntity() instanceof EntityLiving && !(event.getEntity() instanceof EntityPlayer)) {
                EntityLiving living = (EntityLiving) event.getEntity();
                if (potionEffects[0] != -1) {
                    living.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, maxDuration, potionEffects[0], false, false));
                }
                if (potionEffects[1] != -1) {
                    living.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, maxDuration, potionEffects[1], false, false));
                }
                if (potionEffects[2] != -1) {
                    living.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, maxDuration, potionEffects[2], false, false));
                }
                if (potionEffects[3] != -1) {
                    living.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, maxDuration, potionEffects[3], false, false));
                }
                if (potionEffects[4] != -1) {
                    living.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, maxDuration, potionEffects[4], false, false));
                }
                if (potionEffects[5] != -1) {
                    if (living.isNonBoss() || TRAConfigs.DifficultySettings.iaAddSpeedEffectToBoss) {
                        living.addPotionEffect(new PotionEffect(MobEffects.SPEED, maxDuration, potionEffects[5], false, false));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void WorldTick(TickEvent.WorldTickEvent e) {
        if (e.world.provider.getDimension() == ancient_world_dim_id) {
            if (wt == 20) {
                wt = 0;
                PlayersCountDifficultyProcessor.calculate(e.world.playerEntities.size(), potionEffects);
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