package com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.structures;

import com.artur114.bananalib.mc.math.m3d.vec.PosMc3IM;
import com.artur114.thaumrota.client.light.ILightSource;
import com.artur114.thaumrota.client.light.LineLightSource;
import com.artur114.thaumrota.client.light.PointLightSource;
import com.artur114.thaumrota.client.render.fx.HeatRenderer;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public enum LightTemplates implements ILightTemplate {
    WAY(() -> Arrays.asList(
        new LineLightSource(new PosMc3IM(15, 1, 5), new PosMc3IM(0, 1, 5), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F),
        new LineLightSource(new PosMc3IM(0, 1, 10), new PosMc3IM(15, 1, 10), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F)
    )),
    TURN(() -> Arrays.asList(
        new LineLightSource(new PosMc3IM(0, 1, 10), new PosMc3IM(4, 1, 10), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F),
        new LineLightSource(new PosMc3IM(5, 1, 11), new PosMc3IM(5, 1, 15), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F),
        new LineLightSource(new PosMc3IM(10, 1, 15), new PosMc3IM(10, 1, 7), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F),
        new LineLightSource(new PosMc3IM(0, 1, 5), new PosMc3IM(8, 1, 5), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F),
        new LineLightSource(new PosMc3IM(10, 3, 5), new PosMc3IM(10, 7, 5), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F)
    )),
    CROSSROADS(() -> Arrays.asList(
        new LineLightSource(new PosMc3IM(11, 3, 4), new PosMc3IM(11, 7, 4), HeatRenderer.HEAT_COLOR, 0.2F, 1.5F, 1.0F),
        new LineLightSource(new PosMc3IM(4, 3, 4), new PosMc3IM(4, 7, 4), HeatRenderer.HEAT_COLOR, 0.2F, 1.5F, 1.0F),
        new LineLightSource(new PosMc3IM(4, 3, 11), new PosMc3IM(4, 7, 11), HeatRenderer.HEAT_COLOR, 0.2F, 1.5F, 1.0F),
        new LineLightSource(new PosMc3IM(11, 3, 11), new PosMc3IM(11, 7, 11), HeatRenderer.HEAT_COLOR, 0.2F, 1.5F, 1.0F),
        new LineLightSource(new PosMc3IM(5, 1, 0), new PosMc3IM(5, 1, 4), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F),
        new LineLightSource(new PosMc3IM(4, 1, 5), new PosMc3IM(0, 1, 5), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F),
        new LineLightSource(new PosMc3IM(0, 1, 10), new PosMc3IM(4, 1, 10), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F),
        new LineLightSource(new PosMc3IM(5, 1, 11), new PosMc3IM(5, 1, 15), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F),
        new LineLightSource(new PosMc3IM(10, 1, 15), new PosMc3IM(10, 1, 11), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F),
        new LineLightSource(new PosMc3IM(11, 1, 10), new PosMc3IM(15, 1, 10), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F),
        new LineLightSource(new PosMc3IM(15, 1, 5), new PosMc3IM(11, 1, 5), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F),
        new LineLightSource(new PosMc3IM(10, 1, 4), new PosMc3IM(10, 1, 0), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F)
    )),
    FORK(() -> Arrays.asList(
        new LineLightSource(new PosMc3IM(10, 1, 11), new PosMc3IM(10, 1, 15), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F),
        new LineLightSource(new PosMc3IM(5, 1, 15), new PosMc3IM(5, 1, 11), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F),
        new LineLightSource(new PosMc3IM(0, 1, 5), new PosMc3IM(15, 1, 5), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F),
        new LineLightSource(new PosMc3IM(12, 1, 10), new PosMc3IM(15, 1, 10), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F),
        new LineLightSource(new PosMc3IM(3, 1, 10), new PosMc3IM(0, 1, 10), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F),
        new LineLightSource(new PosMc3IM(11, 3, 11), new PosMc3IM(11, 7, 11), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F),
        new LineLightSource(new PosMc3IM(4, 3, 11), new PosMc3IM(4, 7, 11), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F)
    )),
    LADDER(() -> Arrays.asList(
        new LineLightSource(new PosMc3IM(15, 9, 5), new PosMc3IM(0, 1, 5), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F),
        new LineLightSource(new PosMc3IM(0, 1, 10), new PosMc3IM(15, 9, 10), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F)
    ));

    private final Supplier<List<ILightSource>> creator;

    LightTemplates(Supplier<List<ILightSource>> creator) {
        this.creator = creator;
    }

    @Override
    public List<ILightSource> createLights() {
        return this.creator.get();
    }

    public static LineLightSource heatLineLight(BlockPos from, BlockPos to) {
        return new LineLightSource(new PosMc3IM(from), new PosMc3IM(to), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1);
    }

    public static PointLightSource heatPointLight(BlockPos pos) {
        return new PointLightSource(new PosMc3IM(pos), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1);
    }
}
