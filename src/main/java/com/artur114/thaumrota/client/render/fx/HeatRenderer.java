package com.artur114.thaumrota.client.render.fx;

import com.artur114.thaumrota.client.event.ClientEventsHandler;
import com.artur114.thaumrota.client.fx.shader.ShaderProgram;
import com.artur114.thaumrota.client.fx.shader.ShaderRender;
import com.artur114.thaumrota.client.fx.shader.ShaderRenderEngine;
import com.artur114.thaumrota.client.init.InitShaders;
import com.artur114.thaumrota.client.light.EnumLightType;
import com.artur114.thaumrota.client.light.ILightSource;
import com.artur114.thaumrota.common.init.InitDimensions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.BufferUtils;

import java.awt.*;
import java.nio.FloatBuffer;
import java.util.*;
import java.util.List;

@Mod.EventBusSubscriber
public class HeatRenderer {
    public static final Color HEAT_COLOR = new Color(214, 111, 29);
    private static final ShaderRender render = ShaderRender.of(InitShaders.TEST_SHADER).withMainTex().withDepthTex();
    private static FloatBuffer buffer = BufferUtils.createFloatBuffer(64 * 3);
    private static final List<ILightSource> pointLights = new ArrayList<>();
    private static final List<ILightSource> lineLights = new ArrayList<>();
    private static final Map<Integer, Float> dimensions = new HashMap<>();
    private static int maxRenderDist = 48;
    private static int maxLights = 64;

    public static void registerDim(int id, float globalHeat) {
        dimensions.put(id, globalHeat);
    }

    public static void setMaxRenderDist(int maxRenderDist) {
        HeatRenderer.maxRenderDist = maxRenderDist;
    }

    public static void setMaxLights(int maxLights) {
        HeatRenderer.buffer = BufferUtils.createFloatBuffer(maxLights * 3);
        HeatRenderer.maxLights = maxLights;
    }

    public static void clearLight() {
        pointLights.clear();
        lineLights.clear();
    }

    public static void addLight(List<ILightSource> lights) {
        if (lights == null) return;
        for (ILightSource source : lights) {
            addLight(source);
        }
    }

    public static void addLight(ILightSource light) {
        switch (light.type()) {
            case POINT:
                pointLights.add(light);
                break;
            case LINE:
                lineLights.add(light);
                break;
        }
    }

    private static List<ILightSource> prepareLights(List<ILightSource> lights) {
        lights.sort(Comparator.comparingInt(ILightSource::distanceSqToPlayer));
        List<ILightSource> ret = new ArrayList<>();

        for (ILightSource source : lights) {
            if (source.isOnView(maxRenderDist)) {
                if (ret.size() >= maxLights) {
                    break;
                }
                ret.add(source);
            }
        }

        return ret;
    }

    private static void sendToShader(EnumLightType type, List<ILightSource> lights, ShaderProgram program) {
        for (int i = 0; i != type.passCount(); i++) {
            buffer.clear();
            for (ILightSource source : lights) {
                source.writeToBuff(i, buffer);
            }
            buffer.flip();
            program.uniform3(type.nameForPass(i), buffer);
        }
    }

    public static int debugLightsCount(EnumLightType type) {
        switch (type) {
            case LINE:
                return lineLights.size();
            case POINT:
                return pointLights.size();
            default:
                return -1;
        }
    }

    public static int debugLightsToRenCount(EnumLightType type) {
        switch (type) {
            case LINE:
                return prepareLights(lineLights).size();
            case POINT:
                return prepareLights(pointLights).size();
            default:
                return -1;
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void renderShaders(RenderWorldLastEvent evt) {
        if (!dimensions.containsKey(Minecraft.getMinecraft().world.provider.getDimension())) {
            pointLights.clear();
            lineLights.clear();
            return;
        }
        EntityPlayer player = Minecraft.getMinecraft().player;
        List<ILightSource> point = prepareLights(pointLights);
        List<ILightSource> line = prepareLights(lineLights);

        ShaderRenderEngine.renderFullScreen(render, program -> {
            sendToShader(EnumLightType.POINT, point, program);
            sendToShader(EnumLightType.LINE, line, program);

            program.uniform("pointLightCount", point.size());
            program.uniform("lineLightCount", line.size());
            program.uniformInvMVPMatrix("invMVPMatrix");
            program.uniform("cameraPos", (float) (player.posX - Particle.interpPosX), (float) (player.posY - Particle.interpPosY), (float) (player.posZ - Particle.interpPosZ));
            program.uniform("globalHeat", dimensions.get(Minecraft.getMinecraft().world.provider.getDimension()));
            program.uniform("time", (float) (ClientEventsHandler.GLOBAL_TICK_MANAGER.interpolatedGameTickCounter(evt.getPartialTicks()) / 8));
        });
    }

    static {
        dimensions.put(InitDimensions.ancient_world_dim_id, 1.0F);
    }
}