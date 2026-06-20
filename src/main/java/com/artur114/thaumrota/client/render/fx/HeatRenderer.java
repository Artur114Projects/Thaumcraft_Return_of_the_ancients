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
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.BufferUtils;

import java.awt.*;
import java.nio.FloatBuffer;
import java.util.*;
import java.util.List;
import java.util.function.Supplier;

@Mod.EventBusSubscriber
public class HeatRenderer {
    public static final Frustum FRUSTUM = new Frustum();
    public static final Color HEAT_COLOR = new Color(214, 111, 29);
    private static final @SuppressWarnings("unchecked") ListHeap<List<ILightSource>> heap = new ListHeap<List<ILightSource>>(new List[4], ArrayList::new);
    private static final ShaderRender render = ShaderRender.of(InitShaders.TEST_SHADER).withMainTex().withDepthTex();
    private static final Map<String, EnumMap<EnumLightType, List<ILightSource>>> lights = new HashMap<>();
    private static final Map<EnumLightType, List<ILightSource>> prepared = new HashMap<>();
    private static FloatBuffer buffer = BufferUtils.createFloatBuffer(64 * 3);
    private static final Map<Integer, Float> dimensions = new HashMap<>();
    private static int maxRenderDist = 48;
    private static int maxLights = 48;

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

    public static void clearLight(String lightGroup) {
        EnumMap<EnumLightType, List<ILightSource>> group = lights.get(lightGroup);
        if (group != null) group.clear();
    }

    public static void clearLight() {
        lights.clear();
    }

    public static void removeLight(BlockPos pos) {
        removeLight("all", pos);
    }

    public static void removeLight(String lightGroup, BlockPos pos) {
        EnumMap<EnumLightType, List<ILightSource>> group = lights.get(lightGroup);
        if (group != null) {
            group.forEach((type, list) -> {
                list.removeIf(light -> light.collideToPos(pos));
            });
        }
    }

    public static void addLight(String lightGroup, List<ILightSource> lights) {
        if (lights == null) return;
        for (ILightSource source : lights) {
            addLight(lightGroup, source);
        }
    }

    public static void addLight(String lightGroup, ILightSource light) {
        lights.computeIfAbsent(lightGroup, k -> new EnumMap<>(EnumLightType.class)).computeIfAbsent(light.type(), k -> new ArrayList<>()).add(light);
    }

    public static void addLight(List<ILightSource> lights) {
        if (lights == null) return;
        for (ILightSource source : lights) {
            addLight(source);
        }
    }

    public static void addLight(ILightSource light) {
        addLight("all", light);
    }

    private static List<ILightSource> prepareLights(EnumLightType type) {
        List<ILightSource> lights = allLights(type);
        List<ILightSource> sorted = heap.obtain();
        sorted.addAll(lights);
        sorted.sort(Comparator.comparingInt(ILightSource::distanceSqToPlayer));

        List<ILightSource> ret = heap.obtain();

        for (ILightSource source : sorted) {
            if (source.isOnView(maxRenderDist)) {
                if (ret.size() >= maxLights) {
                    break;
                }
                ret.add(source);
            }
        }

        heap.release(sorted);
        heap.release(lights);

        return ret;
    }

    private static int sendToShader(EnumLightType type, ShaderProgram program) {
        List<ILightSource> sources = prepared.getOrDefault(type, Collections.emptyList());
        for (int i = 0; i != type.passCount(); i++) {
            buffer.clear();
            for (ILightSource source : sources) {
                source.writeToBuff(i, buffer);
            }
            buffer.flip();
            program.uniform3(type.nameForPass(i), buffer);
        }
        return sources.size();
    }

    private static List<ILightSource> allLights(EnumLightType type) {
        List<ILightSource> ret = heap.obtain();

        lights.forEach((group, map) -> {
            ret.addAll(map.getOrDefault(type, Collections.emptyList()));
        });

        return ret;
    }

    public static int debugLightsCount(EnumLightType type) {
        List<ILightSource> list = allLights(type);
        int ret = list.size();
        heap.release(list);
        return ret;
    }

    public static int debugLightsToRenCount(EnumLightType type) {
        List<ILightSource> list = prepareLights(type);
        int ret = list.size();
        heap.release(list);
        return ret;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void renderShaders(RenderWorldLastEvent evt) {
        if (!dimensions.containsKey(Minecraft.getMinecraft().world.provider.getDimension())) {
            prepared.clear();
            lights.clear();
            return;
        }
        ClippingHelperImpl.getInstance();
        FRUSTUM.setPosition(Particle.interpPosX, Particle.interpPosY, Particle.interpPosZ);
        ShaderRenderEngine.renderFullScreen(render, program -> {
            int point = sendToShader(EnumLightType.POINT, program);
            int line = sendToShader(EnumLightType.LINE, program);

            program.uniform("pointLightCount", point);
            program.uniform("lineLightCount", line);
            program.uniformInvMVPMatrix("invMVPMatrix");
            program.uniform("globalHeat", dimensions.get(Minecraft.getMinecraft().world.provider.getDimension()));
            program.uniform("time", (float) (ClientEventsHandler.GLOBAL_TICK_MANAGER.interpolatedGameTickCounter(evt.getPartialTicks()) / 8));
        });
    }

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent e) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        World world = Minecraft.getMinecraft().world;

        if (e.phase != TickEvent.Phase.START) {
            return;
        }

        if (world == null) {
            prepared.clear();
            lights.clear();
            return;
        }

        if (player == null) {
            return;
        }

        if (!dimensions.containsKey(world.provider.getDimension())) {
            prepared.clear();
            lights.clear();
            return;
        }

        for (EnumLightType type : EnumLightType.values()) {
            List<ILightSource> light = prepareLights(type);
            List<ILightSource> out = prepared.computeIfAbsent(type, k -> new ArrayList<>());
            out.clear();
            out.addAll(light);
            heap.release(light);
        }
    }

    static {
        dimensions.put(InitDimensions.ANCIENT_WORLD_ID, 1.0F);
    }

    private static final class ListHeap<T extends List<?>> {
        private final Set<T> created = Collections.newSetFromMap(new IdentityHashMap<>());
        private final Supplier<T> creator;
        private int cursor = -1;
        private T[] heap;

        private ListHeap(T[] arr, Supplier<T> creator) {
            this.creator = creator;
            this.heap = arr;
        }

        public T obtain() {
            if (this.cursor < 0) {
                T ret = this.creator.get();
                this.created.add(ret);
                return ret;
            }

            T obj = this.heap[this.cursor];
            this.heap[this.cursor--] = null;

            if (obj == null) {
                T ret = this.creator.get();
                this.created.add(ret);
                return ret;
            }

            return obj;
        }

        public void release(T obj) {
            if (obj == null) {
                return;
            }
            if (!this.created.contains(obj)) {
                return;
            }

            if (this.cursor + 1 >= this.heap.length) {
                this.heap = Arrays.copyOf(this.heap, this.heap.length + 2);
            }

            obj.clear();
            this.heap[++this.cursor] = obj;
        }
    }
}