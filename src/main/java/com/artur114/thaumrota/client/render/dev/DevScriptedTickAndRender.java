package com.artur114.thaumrota.client.render.dev;

import com.artur114.bananalib.mc.registry.ann.AutoInstantiate;
import com.artur114.bananalib.mc.registry.interf.ILoadStagePost;
import com.artur114.thaumrota.common.util.DevScriptsShell;
import com.artur114.thaumrota.main.ThaumRotA;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@AutoInstantiate
public class DevScriptedTickAndRender implements ILoadStagePost {

    @SubscribeEvent
    public void overlayRender(RenderGameOverlayEvent.Post e) {
        Minecraft mc = Minecraft.getMinecraft();
        ThaumRotA.DEV_SHELL.evaluate(
            "render_overlay.groovy",
            new String[] {"event", "partialTicks", "world", "player"},
            new Object[] {e, e.getPartialTicks(), mc.world, mc.player}
        );
    }

    @SubscribeEvent
    public void worldRender(RenderWorldLastEvent e) {
        Minecraft mc = Minecraft.getMinecraft();
        ThaumRotA.DEV_SHELL.evaluate(
            "render_world.groovy",
            new String[] {"renderGlobal", "partialTicks", "world", "player"},
            new Object[] {e.getContext(), e.getPartialTicks(), mc.world, mc.player}
        );
    }

    @SubscribeEvent
    public void worldTick(TickEvent.ClientTickEvent e) {
        Minecraft mc = Minecraft.getMinecraft();
        ThaumRotA.DEV_SHELL.evaluate(
            "client_tick_world.groovy",
            new String[] {"world", "player", "phase"},
            new Object[] {mc.world, mc.player, e.phase}
        );
    }

    @Override
    public void onPostInit() {
        if (DevScriptsShell.isDev()) {
            MinecraftForge.EVENT_BUS.register(this);
        }
    }
}
