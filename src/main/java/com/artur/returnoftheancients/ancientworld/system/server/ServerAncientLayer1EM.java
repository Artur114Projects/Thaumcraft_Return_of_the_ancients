package com.artur.returnoftheancients.ancientworld.system.server;

import com.artur.returnoftheancients.ancientworld.system.base.IAncientLayer1Manager;
import com.artur.returnoftheancients.capabilities.GenericCapProviderS;
import com.artur.returnoftheancients.capabilities.TRACapabilities;
import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ServerAncientLayer1EM {
    public void attachCapabilitiesEventWorld(AttachCapabilitiesEvent<World> e) {
        e.addCapability(new ResourceLocation(Referense.MODID, "ancient_layer_1"), new GenericCapProviderS<>(new ServerAncientLayer1Manager(e.getObject()), TRACapabilities.ANCIENT_LAYER_1_MANAGER));
    }

    public void tickEventWorldTickEvent(TickEvent.WorldTickEvent e) {
        if (e.phase != TickEvent.Phase.START) {
            return;
        }

        IAncientLayer1Manager managerServer = e.world.getCapability(TRACapabilities.ANCIENT_LAYER_1_MANAGER, null);

        if (managerServer != null) {
            managerServer.worldTick();
        }
    }

    public void playerEventPlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent e) {
        IAncientLayer1Manager managerServer = e.player.world.getCapability(TRACapabilities.ANCIENT_LAYER_1_MANAGER, null);

        if (managerServer != null) {
            ((IServerAncientLayer1Manager) managerServer).onPlayerLoginIn((EntityPlayerMP) e.player);
        }
    }

    public void playerEventPlayerLoggedOutEvent(PlayerEvent.PlayerLoggedOutEvent e) {
        IAncientLayer1Manager managerServer = e.player.world.getCapability(TRACapabilities.ANCIENT_LAYER_1_MANAGER, null);

        if (managerServer != null) {
            ((IServerAncientLayer1Manager) managerServer).onPlayerLoginOut((EntityPlayerMP) e.player);
        }
    }

    public boolean playerLost(EntityPlayerMP player) {
        IAncientLayer1Manager managerServer = player.world.getCapability(TRACapabilities.ANCIENT_LAYER_1_MANAGER, null);

        if (managerServer != null) {
            return ((IServerAncientLayer1Manager) managerServer).onPlayerLost(player);
        }

        return false;
    }

    public boolean playerElope(EntityPlayerMP player) {
        IAncientLayer1Manager managerServer = player.world.getCapability(TRACapabilities.ANCIENT_LAYER_1_MANAGER, null);

        if (managerServer != null) {
            return ((IServerAncientLayer1Manager) managerServer).onPlayerElope(player);
        }

        return false;
    }

    public boolean playerInterruptBuild(EntityPlayerMP player) {
        IAncientLayer1Manager managerServer = player.world.getCapability(TRACapabilities.ANCIENT_LAYER_1_MANAGER, null);

        if (managerServer != null) {
            return ((IServerAncientLayer1Manager) managerServer).onPlayerInterruptBuild(player);
        }

        return false;
    }

    public void chunkEventUnload(ChunkEvent.Unload e) {
        IAncientLayer1Manager managerServer = e.getWorld().getCapability(TRACapabilities.ANCIENT_LAYER_1_MANAGER, null);

        if (managerServer != null) {
            for (ClassInheritanceMultiMap<Entity> entity : e.getChunk().getEntityLists()) {
                for (EntityLiving living : entity.getByClass(EntityLiving.class)) {
                    ((IServerAncientLayer1Manager) managerServer).unloadEntity(living);
                }
            }
        }
    }

    public boolean entityJoinWorldEvent(EntityJoinWorldEvent e) {
        IAncientLayer1Manager managerServer = e.getWorld().getCapability(TRACapabilities.ANCIENT_LAYER_1_MANAGER, null);

        if (managerServer != null && e.getEntity() instanceof EntityLiving) {
            return ((IServerAncientLayer1Manager) managerServer).loadEntity((EntityLiving) e.getEntity());
        }

        return false;
    }

    public void livingDeathEvent(LivingDeathEvent e) {
        IAncientLayer1Manager managerServer = e.getEntity().world.getCapability(TRACapabilities.ANCIENT_LAYER_1_MANAGER, null);

        if (managerServer != null && e.getEntity() instanceof EntityLiving) {
            ((IServerAncientLayer1Manager) managerServer).onEntityDead((EntityLiving) e.getEntityLiving());
        }
    }
}
