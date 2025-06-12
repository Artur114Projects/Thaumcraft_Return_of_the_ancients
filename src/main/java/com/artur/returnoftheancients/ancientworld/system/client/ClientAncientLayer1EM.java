package com.artur.returnoftheancients.ancientworld.system.client;

import com.artur.returnoftheancients.ancientworld.system.base.IAncientLayer1Manager;
import com.artur.returnoftheancients.capabilities.GenericCapProviderNS;
import com.artur.returnoftheancients.capabilities.TRACapabilities;
import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ClientAncientLayer1EM {
    public void attachCapabilitiesEventWorld(AttachCapabilitiesEvent<World> e) {
        e.addCapability(new ResourceLocation(Referense.MODID, "ancient_layer_1"), new GenericCapProviderNS<>(new ClientAncientLayer1Manager(e.getObject()), TRACapabilities.ANCIENT_LAYER_1_MANAGER));
    }

    public void tickEventClientTickEvent(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.START) {
            return;
        }

        IAncientLayer1Manager managerServer = Minecraft.getMinecraft().world.getCapability(TRACapabilities.ANCIENT_LAYER_1_MANAGER, null);

        if (managerServer != null) {
            managerServer.worldTick();
        }
    }
}
