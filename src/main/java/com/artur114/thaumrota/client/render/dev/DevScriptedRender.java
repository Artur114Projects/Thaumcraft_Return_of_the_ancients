package com.artur114.thaumrota.client.render.dev;

import com.artur114.bananalib.mc.registry.ann.AutoInstantiate;
import com.artur114.bananalib.mc.registry.interf.ILoadStagePost;
import com.artur114.thaumrota.common.util.DevScriptsShell;
import com.artur114.thaumrota.main.ThaumRotA;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@AutoInstantiate
public class DevScriptedRender implements ILoadStagePost {

    @SubscribeEvent
    public void worldRender(RenderWorldLastEvent e) {
        Minecraft mc = Minecraft.getMinecraft();
        ThaumRotA.DEV_SHELL.evaluate(
            "render_world.groovy",
            new String[] {"renderGlobal", "partialTicks", "world", "player"},
            new Object[] {e.getContext(), e.getPartialTicks(), mc.world, mc.player}
        );
    }

    @Override
    public void onPostInit() {
        if (DevScriptsShell.isDev()) {
            MinecraftForge.EVENT_BUS.register(this);
        }
    }
}
