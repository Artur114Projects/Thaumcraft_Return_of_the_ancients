package com.artur.returnoftheancients.energy.system;

import com.artur.returnoftheancients.capabilities.GenericCapProviderNS;
import com.artur.returnoftheancients.capabilities.TRACapabilities;
import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class EnergySystemsEventsHandler {

    @SubscribeEvent
    public static void tickEventWorldTickEvent(TickEvent.WorldTickEvent e) {
        EnergySystemsManager manager = e.world.getCapability(TRACapabilities.ENERGY_SYSTEMS_MANAGER, null);
        if (manager != null) {
            manager.update(e.phase == TickEvent.Phase.START);
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void tickEventClientTickEvent(TickEvent.ClientTickEvent e) {
        if (Minecraft.getMinecraft().world == null || Minecraft.getMinecraft().player == null || Minecraft.getMinecraft().isGamePaused()) {
            return;
        }
        EnergySystemsManager manager = Minecraft.getMinecraft().world.getCapability(TRACapabilities.ENERGY_SYSTEMS_MANAGER, null);
        if (manager != null) {
            manager.update(e.phase == TickEvent.Phase.START);
        }
    }

    @SubscribeEvent
    public static void attachCapabilitiesWorld(AttachCapabilitiesEvent<World> e) {
        e.addCapability(new ResourceLocation(Referense.MODID, "energy_systems_manager"), new GenericCapProviderNS<>(new EnergySystemsManager(e.getObject()), TRACapabilities.ENERGY_SYSTEMS_MANAGER));
    }
}
