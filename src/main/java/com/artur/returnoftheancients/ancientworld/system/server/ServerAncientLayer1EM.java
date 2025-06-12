package com.artur.returnoftheancients.ancientworld.system.server;

import com.artur.returnoftheancients.ancientworld.system.base.IAncientLayer1Manager;
import com.artur.returnoftheancients.capabilities.GenericCapProviderS;
import com.artur.returnoftheancients.capabilities.TRACapabilities;
import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
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

    public void playerLost(EntityPlayerMP player) {
        IAncientLayer1Manager managerServer = player.world.getCapability(TRACapabilities.ANCIENT_LAYER_1_MANAGER, null);

        if (managerServer != null) {
            ((IServerAncientLayer1Manager) managerServer).onPlayerLost(player);
        }
    }

    public void playerElope(EntityPlayerMP player) {
        IAncientLayer1Manager managerServer = player.world.getCapability(TRACapabilities.ANCIENT_LAYER_1_MANAGER, null);

        if (managerServer != null) {
            ((IServerAncientLayer1Manager) managerServer).onPlayerElope(player);
        }
    }
}
