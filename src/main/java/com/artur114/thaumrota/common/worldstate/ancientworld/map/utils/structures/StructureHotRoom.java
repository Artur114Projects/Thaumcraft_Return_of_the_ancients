package com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.structures;

import com.artur114.bananalib.mc.math.m3d.vec.PosMc3IM;
import com.artur114.thaumrota.client.light.ILightSource;
import com.artur114.thaumrota.client.light.LineLightSource;
import com.artur114.thaumrota.client.light.PointLightSource;
import com.artur114.thaumrota.client.render.fx.HeatRenderer;
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.EnumMultiChunkStrType;
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.EnumRotate;
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.MultiChunkStrForm;
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.StrPos;
import net.minecraft.util.math.ChunkPos;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StructureHotRoom extends StructureMultiChunk {
    public StructureHotRoom(EnumRotate rotate, StrPos pos) {
        super(rotate, EnumMultiChunkStrType.HOT_ROOM, pos);

        this.down(16);
    }

    protected StructureHotRoom(StructureMultiChunk parent) {
        super(parent);
    }

    @Override
    public @NotNull IStructure copy() {
        return new StructureHotRoom(this);
    }

    @Override
    protected void addLights(List<ILightSource> list) {
        list.add(new LineLightSource(new PosMc3IM(-1, 35, 16), new PosMc3IM(-1, 2, 16), HeatRenderer.HEAT_COLOR, 0.2F, 3.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(16, 2, 16), new PosMc3IM(16, 35, 16), HeatRenderer.HEAT_COLOR, 0.2F, 3.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(7, 36, 0), new PosMc3IM(8, 1, 1), HeatRenderer.HEAT_COLOR, 0.3F, 5.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(25, 2, -6), new PosMc3IM(25, 35, -6), HeatRenderer.HEAT_COLOR, 0.3F, 5.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-10, 35, -6), new PosMc3IM(-10, 2, -6), HeatRenderer.HEAT_COLOR, 0.3F, 5.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-10, 2, 25), new PosMc3IM(-10, 35, 25), HeatRenderer.HEAT_COLOR, 0.3F, 5.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(25, 35, 25), new PosMc3IM(25, 2, 25), HeatRenderer.HEAT_COLOR, 0.3F, 5.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-12, 34, 7), new PosMc3IM(-12, 27, 7), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-12, 27, 8), new PosMc3IM(-12, 34, 8), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(7, 34, 27), new PosMc3IM(7, 27, 27), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(8, 27, 27), new PosMc3IM(8, 34, 27), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(27, 34, 7), new PosMc3IM(27, 27, 7), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(27, 27, 8), new PosMc3IM(27, 34, 8), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new PointLightSource(new PosMc3IM(5, 36, 15), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(10, 36, 15), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(10, 36, 17), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(5, 36, 17), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(9, 36, 8), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(6, 36, 8), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(-9, 36, 6), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(-9, 36, 10), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(9, 36, 24), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(6, 36, 24), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(24, 36, 10), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(24, 36, 6), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
    }

    public static class Form extends MultiChunkStrForm {
        @Override
        public char[][] form() {
            return new char[][] {
                {' ',' ','p',' ',' '},
                {' ','s','s','s',' '},
                {'p','s','c','s','p'},
                {' ','s','s','s',' '},
                {' ',' ',' ',' ',' '}
            };
        }
    }
}
