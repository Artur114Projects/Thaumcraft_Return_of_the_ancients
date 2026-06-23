package com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.structures;

import com.artur114.bananalib.math.m3d.matrix.Matrix3FM;
import com.artur114.bananalib.mc.math.m3d.vec.PosMc3IM;
import com.artur114.thaumrota.client.light.ILightSource;
import com.artur114.thaumrota.client.light.LineLightSource;
import com.artur114.thaumrota.client.light.PointLightSource;
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.*;
import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.maps.AbstractMap;
import com.artur114.thaumrota.server.structurebuilder.StructuresBuildManager;
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
    public int yPos() {
        return this.y;
    }

    @Override
    public void setYPos(int y) {
        this.y = y;
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
        PosMc3IM blockPos = PosMc3IM.obtain();
        blockPos.setChunk(pos).setY(this.y);
        StructuresBuildManager.createBuildRequest(world, blockPos, this.type.stringId(this.rotate)).setIgnoreAir().build();
        PosMc3IM.release(blockPos);
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
        List<ILightSource> list = new ArrayList<>();
        this.addLights(list);
        Matrix3FM matrix = Matrix3FM.obtain();
        if (this.rotate != null) {
            matrix.rotateYAround(7.5, 0, 7.5, this.rotate.degrees());
        }
        matrix.translate(pos.x << 4, this.y, pos.z << 4);
        for (ILightSource source : list) {
            if (source instanceof PointLightSource) {
                matrix.transform(((PointLightSource) source).pos());
            } else if (source instanceof LineLightSource) {
                matrix.transform(((LineLightSource) source).from());
                matrix.transform(((LineLightSource) source).to());
            }
        }
        Matrix3FM.release(matrix);
        return list;
    }

    protected void addLights(List<ILightSource> list) {
        if (this.type != null && this.type.light() != null){
            List<ILightSource> lights = this.type.light().createLights();
            if (lights != null) {
                list.addAll(lights);
            }
        }
    }

    private void compilePorts() {
        this.ports.clear();
        this.ports.addAll(this.type.ports(this.rotate));
    }
}
