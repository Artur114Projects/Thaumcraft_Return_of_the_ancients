package com.artur114.thaumrota.client.fx.shader;

import com.artur114.thaumrota.client.event.ClientEventsHandler;
import com.artur114.thaumrota.client.init.InitShaders;
import com.artur114.thaumrota.client.util.RenderHandler;
import com.artur114.thaumrota.common.util.math.BoundingBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.*;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import static org.lwjgl.opengl.GL11.*;

//@Mod.EventBusSubscriber
public class HeatShader {
    protected static Framebuffer framebuffer;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void renderShaders(RenderWorldLastEvent evt) { // TODO: 02.05.2025 Rewrite!
        if (Minecraft.getMinecraft().player == null) {
            return;//  || Minecraft.getMinecraft().player.dimension != InitDimensions.ancient_world_dim_id
        }

//        ShaderProgram.renderFullScreen(InitShaders.TEST_SHADER.shader(), (s) -> {
//            InitShaders.TEST_SHADER.shader().uniform("time", (float) (ClientEventsHandler.GLOBAL_TICK_MANAGER.interpolatedGameTickCounter(evt.getPartialTicks()) / 8.0F));
//            InitShaders.TEST_SHADER.shader().uniformInvProjMatrix("invProjMatrix");
//        });
    }
}