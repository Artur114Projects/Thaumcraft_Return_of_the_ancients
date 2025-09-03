package com.artur.returnoftheancients.client.fx.shader;

import com.artur.returnoftheancients.init.InitDimensions;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class HeatShader {

    @SubscribeEvent()
    public static void renderShaders(RenderWorldLastEvent evt) { // TODO: 02.05.2025 Rewrite!
        if (Minecraft.getMinecraft().player == null || Minecraft.getMinecraft().player.dimension != InitDimensions.ancient_world_dim_id) {
            return;
        }

        ShaderProgram.renderFullScreen(InitShaders.HEAT.shader(), () -> InitShaders.HEAT.shader().uniform("Time", (System.currentTimeMillis() % 100000L) / 30000.0F));
    }
}