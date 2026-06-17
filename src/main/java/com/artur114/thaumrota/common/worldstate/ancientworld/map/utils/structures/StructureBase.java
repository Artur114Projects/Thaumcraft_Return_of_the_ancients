package com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.structures;

import com.artur114.bananalib.mc.math.m3d.vec.PosMc3IM;
import com.artur114.thaumrota.client.light.ILightSource;
import com.artur114.thaumrota.client.light.LineLightSource;
import com.artur114.thaumrota.client.light.PointLightSource;
import com.artur114.thaumrota.client.render.fx.HeatRenderer;
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.*;
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.maps.AbstractMap;
import com.artur114.thaumrota.server.structurebuilder.StructuresBuildManager;
import com.artur114.thaumrota.common.util.math.UltraMutableBlockPos;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class StructureBase implements IStructure {
    public static final int baseY = 80;
    protected final Set<EnumFace> ports = new HashSet<>();
    protected AbstractMap map = null;
    protected EnumStructureType type;
    protected EnumRotate rotate;
    protected int y = baseY;
    protected StrPos pos;

    @SideOnly(Side.CLIENT)
    protected Map<ChunkPos, List<ILightSource>> lightSources = null;

    protected StructureBase() {}

    public StructureBase(EnumRotate rotate, EnumStructureType type, StrPos pos) {

        this.pos = pos.toImmutable();
        this.rotate = rotate;
        this.type = type;

        this.compilePorts();
    }

    protected StructureBase(StructureBase parent) {
        this.ports.addAll(parent.ports);
        this.rotate = parent.rotate;
        this.type = parent.type;
        this.pos = parent.pos;
        this.y = parent.y;
        this.map = null;
    }

    @Override
    public @NotNull IStructure copy() {
        return new StructureBase(this);
    }

    @Override
    public @NotNull StrPos pos() {
        return this.pos;
    }

    @Override
    public @NotNull IStructureType type() {
        return this.type;
    }

    @Override
    public @NotNull EnumFace[] ports() {
        return this.ports.toArray(new EnumFace[0]);
    }

    @Override
    public @NotNull EnumRotate rotate() {
        return this.rotate;
    }

    @Override
    public @NotNull IStructure up(int n) {
        this.y += n;
        return this;
    }

    @Override
    public @NotNull IStructure down(int n) {
        this.y -= n;
        return this;
    }

    @Override
    public boolean canConnect(EnumFace face) {
        return this.ports.contains(face);
    }

    @Override
    public void build(World world, ChunkPos pos, Random rand) {
        UltraMutableBlockPos blockPos = UltraMutableBlockPos.obtain();
        blockPos.setPos(pos).setY(this.y);
        StructuresBuildManager.createBuildRequest(world, blockPos, this.type.stringId(this.rotate)).setIgnoreAir().build();
        UltraMutableBlockPos.release(blockPos);
    }

    @Override
    public void setRotate(EnumRotate rotate) {
        this.rotate = rotate;
        this.compilePorts();
    }

    @Override
    public void bindMap(AbstractMap map) {
        this.map = map;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public List<ILightSource> light(ChunkPos pos) {
        if (this.lightSources == null) {
            this.lightSources = new HashMap<>();
        }
        return this.lightSources.computeIfAbsent(pos, p -> {
            List<ILightSource> list = new ArrayList<>();
            this.addLights(list);
            for (ILightSource source : list) {
                if (source instanceof PointLightSource) {
                    ((PointLightSource) source).pos().add(p.x << 4, this.y, p.z << 4);
                } else if (source instanceof LineLightSource) {
                    ((LineLightSource) source).from().add(p.x << 4, this.y, p.z << 4);
                    ((LineLightSource) source).to().add(p.x << 4, this.y, p.z << 4);
                }
            }
            return list;
        });
    }

    protected LineLightSource heatLineLight(BlockPos from, BlockPos to) {
        return new LineLightSource(new PosMc3IM(from), new PosMc3IM(to), HeatRenderer.HEAT_COLOR, 0.2F, 2, 1);
    }

    protected PointLightSource heatPointLight(BlockPos pos) {
        return new PointLightSource(new PosMc3IM(pos), HeatRenderer.HEAT_COLOR, 0.2F, 2, 1);
    }

    protected void addLights(List<ILightSource> list) {
        list.add(this.heatLineLight(new BlockPos(10, 1, 0), new BlockPos(10, 1, 15)));
        list.add(this.heatLineLight(new BlockPos(5, 1, 0), new BlockPos(5, 1, 15)));
    }

    private void compilePorts() {
        this.ports.clear();
        this.ports.addAll(this.type.ports(this.rotate));
    }
}
