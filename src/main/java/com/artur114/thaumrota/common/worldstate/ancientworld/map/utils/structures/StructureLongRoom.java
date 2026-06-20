package com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.structures;

import com.artur114.bananalib.mc.math.EnumRot;
import com.artur114.bananalib.mc.math.m3d.vec.PosMc3IM;
import com.artur114.thaumrota.client.light.ILightSource;
import com.artur114.thaumrota.client.light.LineLightSource;
import com.artur114.thaumrota.client.light.PointLightSource;
import com.artur114.thaumrota.client.render.fx.HeatRenderer;
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.EnumMultiChunkStrType;
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.EnumRotate;
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.MultiChunkStrForm;
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.StrPos;
import com.artur114.thaumrota.server.structurebuilder.StructuresBuildManager;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class StructureLongRoom extends StructureMultiChunk {
    public StructureLongRoom(EnumRotate rotate, StrPos pos) {
        super(rotate.wrap(EnumRotate.C90), EnumMultiChunkStrType.LONG_ROOM, pos);
    }

    protected StructureLongRoom(StructureMultiChunk parent) {
        super(parent);
    }

    @Override
    public @NotNull IStructure copy() {
        return new StructureLongRoom(this);
    }

    @Override
    public void build(World world, ChunkPos pos, Random rand) {
        PosMc3IM blockPos = PosMc3IM.obtain();
        blockPos.setChunk(pos).add(8, 0, 8).setY(this.y);
        if (this.rotate == EnumRotate.NON) {
            blockPos.addX(8);
        } else {
            blockPos.addZ(8);
        }
        StructuresBuildManager.createBuildRequest(world, blockPos, this.type.stringId(this.rotate)).setIgnoreAir().setPosAsXZCenter().build();
        PosMc3IM.release(blockPos);
    }

    @Override
    protected void addLights(List<ILightSource> list) {
        list.add(new LineLightSource(new PosMc3IM(8, 5, 13), new PosMc3IM(23, 5, 13), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(23, 5, 2), new PosMc3IM(8, 5, 2), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(32, 1, 2), new PosMc3IM(42, 1, 2), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(42, 1, 13), new PosMc3IM(32, 1, 13), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-11, 1, 13), new PosMc3IM(-1, 1, 13), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-1, 1, 2), new PosMc3IM(-11, 1, 2), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(16, 13, 14), new PosMc3IM(16, 7, 14), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(15, 7, 14), new PosMc3IM(15, 13, 14), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(15, 7, 1), new PosMc3IM(15, 13, 1), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(16, 13, 1), new PosMc3IM(16, 7, 1), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(3, 11, 1), new PosMc3IM(3, 5, 1), HeatRenderer.HEAT_COLOR, 0.2F, 2.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(3, 11, 14), new PosMc3IM(3, 5, 14), HeatRenderer.HEAT_COLOR, 0.2F, 2.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(28, 11, 14), new PosMc3IM(28, 4, 14), HeatRenderer.HEAT_COLOR, 0.2F, 2.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(28, 11, 1), new PosMc3IM(28, 5, 1), HeatRenderer.HEAT_COLOR, 0.2F, 2.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-14, 6, 12), new PosMc3IM(-14, 3, 12), HeatRenderer.HEAT_COLOR, 0.2F, 2.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(-14, 6, 3), new PosMc3IM(-14, 3, 3), HeatRenderer.HEAT_COLOR, 0.2F, 2.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(45, 6, 3), new PosMc3IM(45, 3, 3), HeatRenderer.HEAT_COLOR, 0.2F, 2.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(45, 6, 12), new PosMc3IM(45, 3, 12), HeatRenderer.HEAT_COLOR, 0.2F, 2.0F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(10, 1, 6), new PosMc3IM(21, 1, 6), HeatRenderer.HEAT_COLOR, 0.6F, 3.5F, 1.0F));
        list.add(new LineLightSource(new PosMc3IM(21, 1, 9), new PosMc3IM(10, 1, 9), HeatRenderer.HEAT_COLOR, 0.6F, 3.5F, 1.0F));
        list.add(new PointLightSource(new PosMc3IM(42, 5, 14), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(42, 5, 1), HeatRenderer.HEAT_COLOR, 0.2F, 2.5F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(41, 2, 9), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(41, 2, 6), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(-10, 2, 9), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(-10, 2, 6), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(11, 15, 4), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(11, 15, 11), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(20, 15, 11), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(20, 15, 4), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(7, 6, 5), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(7, 6, 10), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(24, 6, 10), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
        list.add(new PointLightSource(new PosMc3IM(24, 6, 5), HeatRenderer.HEAT_COLOR, 0.2F, 1.0F, 0.5F));
    }

    public static class Form extends MultiChunkStrForm {

        @Override
        public char[][] form() {
            return new char[][] {
                {' ',' ',' ',' ',' ',' '},
                {' ',' ',' ',' ',' ',' '},
                {'p','s','s','c','s','p'},
                {' ',' ',' ',' ',' ',' '},
                {' ',' ',' ',' ',' ',' '}
            };
        }
    }
}
