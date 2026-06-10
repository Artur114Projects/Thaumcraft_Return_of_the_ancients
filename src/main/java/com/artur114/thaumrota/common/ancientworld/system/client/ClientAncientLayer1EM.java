package com.artur114.thaumrota.common.ancientworld.system.client;

import com.artur114.thaumrota.common.ancientworld.system.base.IAncientLayer1Manager;
import com.artur114.thaumrota.common.capabilities.GenericCapProviderNS;
import com.artur114.thaumrota.common.init.InitCapabilities;
import com.artur114.thaumrota.main.ThaumicRotA;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ClientAncientLayer1EM {
    public void attachCapabilitiesEventWorld(AttachCapabilitiesEvent<World> e) {
        e.addCapability(new ResourceLocation(ThaumicRotA.MODID, "ancient_layer_1"), new GenericCapProviderNS<>(new ClientAncientLayer1Manager(e.getObject()), InitCapabilities.ANCIENT_LAYER_1_MANAGER));
    }

    public void tickEventClientTickEvent(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.START || Minecraft.getMinecraft().isGamePaused()) {
            return;
        }

        IAncientLayer1Manager managerServer = Minecraft.getMinecraft().world.getCapability(InitCapabilities.ANCIENT_LAYER_1_MANAGER, null);

        if (managerServer != null) {
            managerServer.worldTick();
        }
    }
}
