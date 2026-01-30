package com.artur.returnoftheancients.util.math;

import com.artur.returnoftheancients.ancientworld.map.utils.EnumRotate;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class BoundingBox implements IArea {
    private final BlockPos start;
    private final BlockPos end;

    public BoundingBox(BlockPos start, BlockPos end) {
        this.end = new BlockPos(Math.max(start.getX(), end.getX()), Math.max(start.getY(), end.getY()), Math.max(start.getZ(), end.getZ()));
        this.start = new BlockPos(Math.min(start.getX(), end.getX()), Math.min(start.getY(), end.getY()), Math.min(start.getZ(), end.getZ()));
    }

    public BoundingBox(LocalPos.Baked start, LocalPos.Baked end) {
        this(start.blockPos(), end.blockPos());
    }

    public BoundingBox(Pos3d start, Pos3d end) {
        this(new BlockPos(start.x, start.y, start.z), new BlockPos(end.x, end.y, end.z));
    }

    public BoundingBox grow(int size) {
        return this.grow(size, size, size);
    }

    public BoundingBox grow(int x, int y, int z) {
        return new BoundingBox(this.start.add(-x, -y, -z), this.end.add(x, y, z));
    }

    public BoundingBox rotate(Pos3d center, EnumRotate rotate) {
        Pos3d vecStart = new Pos3d(this.start.getX(), this.start.getY(), this.start.getZ()).deduct(center).rotateY(EnumRotate.toMc(rotate)).add(center);
        Pos3d vecEnd = new Pos3d(this.end.getX(), this.end.getY(), this.end.getZ()).deduct(center).rotateY(EnumRotate.toMc(rotate)).add(center);
        return new BoundingBox(vecStart, vecEnd);
    }

    @Override
    public int areaSize() {
        return 0;
    }

    @Override
    public boolean isCollide(double x, double y, double z) {
        return x >= this.start.getX() && y >= this.start.getY() && z >= this.start.getZ() && x <= this.end.getX() + 1 && y <= this.end.getY() + 1 && z <= this.end.getZ() + 1;
    }

    @Override
    public @Nullable BlockPos fromIndex(int index) {
        return null;
    }

    @Override
    public List<BlockPos> points() {
        List<BlockPos> ret = new ArrayList<>();
        for (BlockPos pos : BlockPos.getAllInBox(this.start, this.end)) ret.add(pos);
        return ret;
    }
}