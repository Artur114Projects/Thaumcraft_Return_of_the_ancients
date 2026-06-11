package com.artur114.thaumrota.common.energy.system;

import com.artur114.bananalib.mc.cap.BananaCapProvNoSave;
import com.artur114.thaumrota.common.init.InitCapabilities;
import com.artur114.thaumrota.main.ThaumRotA;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
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
        EnergySystemsManager manager = e.world.getCapability(InitCapabilities.ENERGY_SYSTEMS_MANAGER, null);
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
        EnergySystemsManager manager = Minecraft.getMinecraft().world.getCapability(InitCapabilities.ENERGY_SYSTEMS_MANAGER, null);
        if (manager != null) {
            manager.update(e.phase == TickEvent.Phase.START);
        }
    }

    @SubscribeEvent
    public static void attachCapabilitiesWorld(AttachCapabilitiesEvent<World> e) {
        e.addCapability(new ResourceLocation(ThaumRotA.MODID, "energy_systems_manager"), new BananaCapProvNoSave<>(new EnergySystemsManager(e.getObject()), InitCapabilities.ENERGY_SYSTEMS_MANAGER));
    }
}
