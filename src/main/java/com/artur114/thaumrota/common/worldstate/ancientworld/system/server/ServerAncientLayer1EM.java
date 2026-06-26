package com.artur114.thaumrota.common.worldstate.ancientworld.system.server;

import com.artur114.bananalib.mc.cap.BananaCapProv;
import com.artur114.thaumrota.common.config.RotAConfig;
import com.artur114.thaumrota.common.generation.portal.base.AncientPortalsProcessor;
import com.artur114.thaumrota.common.worldstate.ancientworld.system.base.IAncientLayer1Manager;
import com.artur114.thaumrota.common.init.InitCapabilities;
import com.artur114.thaumrota.main.ThaumRotA;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import thaumcraft.api.blocks.BlocksTC;

public class ServerAncientLayer1EM {
    public void attachCapabilitiesEventWorld(AttachCapabilitiesEvent<World> e) {
        e.addCapability(new ResourceLocation(ThaumRotA.MODID, "ancient_layer_1"), new BananaCapProv<>(new ServerAncientLayer1Manager(e.getObject()), InitCapabilities.ANCIENT_LAYER_1_MANAGER));
    }

    public void tickEventWorldTickEvent(TickEvent.WorldTickEvent e) {
        if (e.phase != TickEvent.Phase.START) {
            return;
        }

        IAncientLayer1Manager managerServer = e.world.getCapability(InitCapabilities.ANCIENT_LAYER_1_MANAGER, null);

        if (managerServer != null) {
            managerServer.worldTick();
        }
    }

    public void playerEventPlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent e) {
        IAncientLayer1Manager managerServer = e.player.world.getCapability(InitCapabilities.ANCIENT_LAYER_1_MANAGER, null);

        if (managerServer != null) {
            ((IServerAncientLayer1Manager) managerServer).onPlayerLoginIn((EntityPlayerMP) e.player);
        }
    }

    public void playerEventPlayerLoggedOutEvent(PlayerEvent.PlayerLoggedOutEvent e) {
        IAncientLayer1Manager managerServer = e.player.world.getCapability(InitCapabilities.ANCIENT_LAYER_1_MANAGER, null);

        if (managerServer != null) {
            ((IServerAncientLayer1Manager) managerServer).onPlayerLoginOut((EntityPlayerMP) e.player);
        }
    }

    public boolean playerLost(EntityPlayerMP player) {
        IAncientLayer1Manager managerServer = player.world.getCapability(InitCapabilities.ANCIENT_LAYER_1_MANAGER, null);

        if (managerServer != null) {
            return ((IServerAncientLayer1Manager) managerServer).onPlayerLost(player);
        }

        return false;
    }

    public boolean playerElope(EntityPlayerMP player) {
        IAncientLayer1Manager managerServer = player.world.getCapability(InitCapabilities.ANCIENT_LAYER_1_MANAGER, null);

        if (managerServer != null) {
            return ((IServerAncientLayer1Manager) managerServer).onPlayerElope(player);
        }

        return false;
    }

    public boolean playerInterruptBuild(EntityPlayerMP player) {
        IAncientLayer1Manager managerServer = player.world.getCapability(InitCapabilities.ANCIENT_LAYER_1_MANAGER, null);

        if (managerServer != null) {
            return ((IServerAncientLayer1Manager) managerServer).onPlayerInterruptBuild(player);
        }

        return false;
    }

    public void chunkEventUnload(ChunkEvent.Unload e) {
        IAncientLayer1Manager managerServer = e.getWorld().getCapability(InitCapabilities.ANCIENT_LAYER_1_MANAGER, null);

        if (managerServer != null) {
            for (ClassInheritanceMultiMap<Entity> entity : e.getChunk().getEntityLists()) {
                for (EntityLiving living : entity.getByClass(EntityLiving.class)) {
                    ((IServerAncientLayer1Manager) managerServer).unloadEntity(living);
                }
            }
        }
    }

    public boolean entityJoinWorldEvent(EntityJoinWorldEvent e) {
        IAncientLayer1Manager managerServer = e.getWorld().getCapability(InitCapabilities.ANCIENT_LAYER_1_MANAGER, null);
        boolean isLiving = e.getEntity() instanceof EntityLiving;

        if (managerServer != null && isLiving) {
            return ((IServerAncientLayer1Manager) managerServer).loadEntity((EntityLiving) e.getEntity());
        }

        return !isLiving;
    }

    public void livingDeathEvent(LivingDeathEvent e) {
        IAncientLayer1Manager managerServer = e.getEntity().world.getCapability(InitCapabilities.ANCIENT_LAYER_1_MANAGER, null);

        if (managerServer != null && e.getEntity() instanceof EntityLiving) {
            ((IServerAncientLayer1Manager) managerServer).onEntityDead((EntityLiving) e.getEntityLiving());
        }
    }

    public void playerInteractEventRightClickBlock(PlayerInteractEvent.RightClickBlock e) {
        if (e.getEntityPlayer().isCreative() && e.getEntityPlayer().isSneaking()) {
            return;
        }

        if (e.getItemStack().getItem() instanceof ItemBlock) {
            e.setUseItem(Event.Result.DENY);
        }
    }

    public void playerInteractEventLeftClickBlock(PlayerInteractEvent.LeftClickBlock e) {
        if (e.getEntityPlayer().isCreative() && e.getEntityPlayer().isSneaking()) {
            return;
        }
        if (e.getEntityPlayer().world.getBlockState(e.getPos()).getBlock() == BlocksTC.lootCrateRare) {
            return;
        }

        e.setCanceled(true);
    }

    public void blockEventBreakEvent(BlockEvent.BreakEvent e) {
        if (e.getPlayer().isCreative() && e.getPlayer().isSneaking()) {
            return;
        }
        if (e.getPlayer().world.getBlockState(e.getPos()).getBlock() == BlocksTC.lootCrateRare) {
            return;
        }

        e.setCanceled(true);
    }

    public void livingDropsEvent(LivingDropsEvent e) {
        e.setCanceled(true);
    }

    public void livingDamageEvent(LivingDamageEvent e) {
        if (e.getEntity() instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) e.getEntity();
            float minDamage = 0;
            if (e.getSource().damageType.equals("mob")) {
                minDamage = 2.0F;
            } else if (e.getSource() == DamageSource.IN_FIRE || e.getSource() == DamageSource.ON_FIRE) {
                minDamage = 0.5F;
            }
            float mul = 1.25F;
            float damage = Math.max(e.getAmount() * mul, minDamage);
            if (!RotAConfig.server.canDeadInAncientWorld && player.getHealth() - damage <= 0) {
                player.curePotionEffects(new ItemStack(Items.MILK_BUCKET));
                player.setFire(0);
                player.setHealth(3);
                if (!this.playerLost(player)) {
                    AncientPortalsProcessor.teleportToOverworld(player);
                }
                e.setCanceled(true);
            } else {
                e.setAmount(damage);
            }
        }
    }

    public void livingSpawnEventAllowDespawn(LivingSpawnEvent.AllowDespawn e) {
        e.setResult(Event.Result.DENY);
    }
}


