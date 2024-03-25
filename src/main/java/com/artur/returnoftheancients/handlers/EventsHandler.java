package com.artur.returnoftheancients.handlers;

import com.artur.returnoftheancients.ancientworldutilities.Configs;
import com.artur.returnoftheancients.ancientworldutilities.WorldData;
import com.artur.returnoftheancients.blocks.TpToAncientWorldBlock;
import com.artur.returnoftheancients.generation.generators.GenStructure;
import com.artur.returnoftheancients.init.InitBiome;
import com.artur.returnoftheancients.sounds.ModSounds;
import com.artur.returnoftheancients.utils.interfaces.IALGS;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.capabilities.IPlayerWarp;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.common.entities.monster.EntityEldritchGuardian;

import static com.artur.returnoftheancients.init.InitDimensions.ancient_world_dim_id;


public class EventsHandler {

    public static boolean tpToHome = false;
    public static boolean bossIsDead = false;

    private boolean capIsSet = true;
    private boolean startUp = false;
    private static boolean isAncientWorldLoad = false;


    public static boolean isAncientWorldLoad() {return isAncientWorldLoad;}

    public static void setAncientWorldLoad(boolean ancientWorldLoad) {isAncientWorldLoad = ancientWorldLoad;}

    public static void tpToHome(EntityPlayer player, int dimension, int x, int y, int z) {
        TpToAncientWorldBlock.noCollision = true;
        FreeTeleporter.teleportToDimension(player, dimension, x, y, z);
        System.out.println(player);
        System.out.println(dimension);
        System.out.println(x);
        System.out.println(y);
        System.out.println(z);
        tpToHome = true;
    }

    @SubscribeEvent
    public void WorldEventLoad(WorldEvent.Load e) {
        if (Configs.PortalSettings.isGen) {
            WorldData worldData = WorldData.get();
            if (Configs.PortalSettings.toBedrock) {
                if (!worldData.saveData.getBoolean(IALGS.isAncientPortalGenerateKey) && e.getWorld().provider.getDimension() == 0) {
                    if (!worldData.saveData.hasKey(IALGS.AncientPortalYPosKey)) {
                        worldData.saveData.setInteger(IALGS.AncientPortalYPosKey, Handler.CalculateGenerationHeight(e.getWorld(), Configs.PortalSettings.x, Configs.PortalSettings.z));
                    }
                    Handler.genAncientPortal(e.getWorld(), Configs.PortalSettings.x, Configs.PortalSettings.y, Configs.PortalSettings.z, true);
                    worldData.saveData.setBoolean(IALGS.isAncientPortalGenerateKey, true);
                }
            } else {
                GenStructure.generateStructure(e.getWorld(), Configs.PortalSettings.x, Handler.CalculateGenerationHeight(e.getWorld(), Configs.PortalSettings.x, Configs.PortalSettings.z) + Configs.PortalSettings.y, Configs.PortalSettings.z, "ancient_portal_no_bedrock");
            }
            worldData.markDirty();
        }
        System.out.println("World: " + e.getWorld().provider.getDimension() + " is load");
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
        if (e.getEntity() instanceof EntityPlayerMP) {
            if (e.getEntity().dimension == ancient_world_dim_id) {
                EntityPlayer player = (EntityPlayer) e.getEntity();
                if (player.getHealth() - e.getAmount() <= 0) {
                    e.setCanceled(true);
                    player.setHealth(20);
                    player.removePotionEffect(Potion.getPotionById(19));
                    player.removePotionEffect(Potion.getPotionById(20));
                    tpToHome(player, 0, 8, 3, 8);
                    System.out.println("You dead");
                }
            }
        }
    }

    @SubscribeEvent
    public void BreakEvent(BlockEvent.BreakEvent e) {
        if (e.getPlayer().dimension == ancient_world_dim_id) {
            if (!e.getPlayer().capabilities.isCreativeMode)
                e.setCanceled(true);
            if (BlocksTC.nitor.get(EnumDyeColor.BLACK).getBlockState().equals(e.getState())) {
                System.out.println("nitor is break");
            }
        }
    }

    @SubscribeEvent
    public void PlaceEvent(BlockEvent.PlaceEvent e) {
        if (e.getPlayer().dimension == ancient_world_dim_id) {
            if (!e.getPlayer().capabilities.isCreativeMode)
                e.setCanceled(true);
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

        if (startUp) {
            if (playerDimension != ancient_world_dim_id) {
                EntityPlayer player = Minecraft.getMinecraft().player;
                BlockPos pos = e.player.getPosition();
                if (player.motionY < 2) {
                    player.motionY = player.motionY + 0.1;
                }
                if (pos.getY() > WorldData.get().saveData.getInteger(IALGS.AncientPortalYPosKey)) {
                    player.motionY = player.motionY + 1;
                    startUp = false;
                    TpToAncientWorldBlock.noCollision = false;
                    e.player.removePotionEffect(Potion.getPotionById(15));
                    System.out.println("yse");
                    System.out.println(pos.getY());
                }
                player.motionX = 0;
                player.motionZ = 0;
            }
        }

        if (tpToHome) {
            BlockPos pos = e.player.getPosition();
            if (playerDimension != ancient_world_dim_id && pos.getY() <= 3) {
                if (e.player.getServer() != null) {
                    if (e.player instanceof EntityPlayerMP) {
                        IPlayerWarp playerWarp = ThaumcraftCapabilities.getWarp(e.player);
                        GameSettings Settings = Minecraft.getMinecraft().gameSettings;
                        playerWarp.set(IPlayerWarp.EnumWarpType.PERMANENT, e.player.getEntityData().getInteger("PERMANENT"));
                        playerWarp.set(IPlayerWarp.EnumWarpType.TEMPORARY, e.player.getEntityData().getInteger("TEMPORARY"));
                        playerWarp.set(IPlayerWarp.EnumWarpType.NORMAL, e.player.getEntityData().getInteger("NORMAL"));
                        EntityPlayerMP playerMP = (EntityPlayerMP) e.player;
                        playerWarp.sync(playerMP);
                        startUp = true;
                        int r = e.player.getEntityData().getInteger("renderDistanceChunks");
                        float g = e.player.getEntityData().getFloat("gammaSetting");
                        if (r != 0) {
                            Settings.renderDistanceChunks = r;
                        }
                        e.player.getEntityData().setInteger("renderDistanceChunks", 0);
                        e.player.getEntityData().setFloat("gammaSetting", 0.00001f);
                        Settings.gammaSetting = g;
                        e.player.getEntityData().setBoolean("isWarpSet", false);
                        e.player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 600, 1));
                        tpToHome = false;
                    }
                }
            }
        }

        if (playerDimension == ancient_world_dim_id) {
            IPlayerWarp playerWarp = ThaumcraftCapabilities.getWarp(e.player);
            GameSettings Settings = Minecraft.getMinecraft().gameSettings;
            BlockPos pos = e.player.getPosition();
            if (pos.getY() > 82 && e.player.motionY < -1 && !e.player.isCreative()) {
                e.player.fallDistance = 0;
                e.player.motionY = e.player.motionY + 0.1;
            }
            if (e.player.getActivePotionEffect(MobEffects.NIGHT_VISION) != null && !e.player.capabilities.isCreativeMode) {
                e.player.removePotionEffect(MobEffects.NIGHT_VISION);
                e.player.sendMessage(new TextComponentString("Only darkness"));
            }
            if (Settings.gammaSetting != 0 && !e.player.isCreative()) {
                if (0.00001f == e.player.getEntityData().getFloat("gammaSetting"))
                    e.player.getEntityData().setFloat("gammaSetting", Settings.gammaSetting);
                Settings.gammaSetting = 0;
                e.player.sendMessage(new TextComponentString("Only darkness"));
            }
            if (Settings.renderDistanceChunks != 4 && !e.player.isCreative()) {
                if (0 == e.player.getEntityData().getInteger("renderDistanceChunks"))
                    e.player.getEntityData().setInteger("renderDistanceChunks", Settings.renderDistanceChunks);
                Settings.renderDistanceChunks = 4;
                e.player.sendMessage(new TextComponentString("Only darkness"));
            }
            if (Settings.difficulty == EnumDifficulty.PEACEFUL && !e.player.isCreative()) {
                Settings.difficulty = EnumDifficulty.HARD;
                e.player.sendMessage(new TextComponentString("Peaceful???"));
            }
            if (!(e.player.getEntityData().getBoolean("isWarpSet") && e.player instanceof EntityPlayerMP && e.player.getServer() != null)) {
                if (e.player instanceof EntityPlayerMP) {
                    e.player.getEntityData().setInteger("PERMANENT", playerWarp.get(IPlayerWarp.EnumWarpType.PERMANENT));
                    e.player.getEntityData().setInteger("TEMPORARY", playerWarp.get(IPlayerWarp.EnumWarpType.TEMPORARY));
                    e.player.getEntityData().setInteger("NORMAL", playerWarp.get(IPlayerWarp.EnumWarpType.NORMAL));
                    e.player.getEntityData().setBoolean("isWarpSet", true);
                    playerWarp.set(IPlayerWarp.EnumWarpType.PERMANENT, 100);
                    EntityPlayerMP playerMP = (EntityPlayerMP) e.player;
                    playerWarp.sync(playerMP);
                }
            }
            if (!capIsSet) {
                if (pos.getY() == 81 && pos.getX() <= 9 && pos.getX() >= 6 && pos.getZ() <= 9 && pos.getZ() >= 6) {
                    Handler.playSound(ModSounds.TP_SOUND);
                    if (e.player.isCreative()) {
                        e.player.move(MoverType.SHULKER_BOX, 8, 125, -10);
                    }
                    GenStructure.generateStructure(e.player.world, 6, 85, 6, "ancient_cap");
                    capIsSet = true;
                }
            }

        }
    }

    @SubscribeEvent
    public void PlayerChangedDimensionEvent(PlayerEvent.PlayerChangedDimensionEvent e) {
        if (e.toDim == ancient_world_dim_id) {
            if (e.player.isCreative()) {
                e.player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 999999, 0));
            }
            capIsSet = false;
            WorldData worldData = WorldData.get();
            worldData.saveData.setBoolean(IALGS.isBossSpawn, false);
            worldData.markDirty();
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
//        if (event.getWorld().provider.getDimension() == ancient_world_dim_id && !event.getWorld().isRemote && !(event.getEntity() instanceof EntityItem)) {
//            if (!event.getEntity().getEntityData().hasKey("A_ID")) {
//                event.getWorld().getChunkFromBlockCoords(event.getEntity().getPosition()).removeEntity(event.getEntity());
//                event.getWorld().removeEntity(event.getEntity());
//                event.setCanceled(true);
//            } else if (event.getEntity().getEntityData().getLong("A_ID") != WorldData.get().saveData.getLong("mobId")) {
//                event.getWorld().getChunkFromBlockCoords(event.getEntity().getPosition()).removeEntity(event.getEntity());
//                event.getWorld().removeEntity(event.getEntity());
//                event.setCanceled(true);
//            }
//        }
    }

    @SubscribeEvent
    public void canDeSpawn(LivingSpawnEvent.AllowDespawn event) {
        if (!(event.getEntityLiving() instanceof EntityEldritchGuardian)) {
            return;
        }
        event.setResult(Event.Result.DENY);
    }

    @SubscribeEvent
    public void ChunkEventLoad(ChunkEvent.Load e) {
//        if (e.getWorld().provider.getDimension() == ancient_world_dim_id) {
//            if (e.getChunk().getPos().equals(new ChunkPos(0, 0))) {
//                World world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(ancient_world_dim_id);
//                EntityEldritchGuardian q = new EntityEldritchGuardian(world);
//                q.setPositionAndUpdate(8, 81, 8);
//                q.forceSpawn = true;
//                world.spawnEntity(q);
//                System.out.println("Eldritch Guardian is gen XYZ " + 8 + " " + 81 + " " + 8);
//            }
//        }
    }
}