package com.artur.returnoftheancients.client.fx.shader;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class HeatShader {

    @SubscribeEvent
    public static void renderShaders(RenderWorldLastEvent evt) {
//        if (evt.getType() != RenderGameOverlayEvent.ElementType.ALL) return;

        ShaderProgram.renderFullScreen(InitShaders.HEAT.shader(), () -> InitShaders.HEAT.shader().uniform("Time", (System.currentTimeMillis() % 100000L) / 20000.0F));
    }
}