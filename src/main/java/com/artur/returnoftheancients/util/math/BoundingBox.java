package com.artur.returnoftheancients.util.math;

import com.artur.returnoftheancients.ancientworld.map.utils.EnumRotate;
import net.minecraft.entity.Entity;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class BoundingBox {
    private final BlockPos start;
    private final BlockPos end;

    public BoundingBox(BlockPos start, BlockPos end) {
        this.end = new BlockPos(Math.max(start.getX(), end.getX()) + 1, Math.max(start.getY(), end.getY()) + 1, Math.max(start.getZ(), end.getZ()) + 1);
        this.start = new BlockPos(Math.min(start.getX(), end.getX()), Math.min(start.getY(), end.getY()), Math.min(start.getZ(), end.getZ()));
    }

    public boolean isCollide(BlockPos pos) {
        return this.isCollide(pos.getX(), pos.getY(), pos.getZ());
    }

    public boolean isCollide(int x, int y, int z) {
        return this.isCollide(x, y, (double) z);
    }

    public boolean isCollide(Entity entity) {
        return this.isCollide(entity.posX, entity.posY, entity.posZ);
    }

    public boolean isCollide(double x, double y, double z) {
        return x >= this.start.getX() && y >= this.start.getY() && z >= this.start.getZ() && x <= this.end.getX() && y <= this.end.getY() && z <= this.end.getZ();
    }

    public <T extends Entity> List<T> sortCollided(List<T> entities) {
        List<T> ret = new ArrayList<>(entities);
        ret.removeIf(e -> !this.isCollide(e));
        return ret;
    }

    public <T> List<T> sortCollided(List<T> entities, Function<T, Entity> extractor) {
        List<T> ret = new ArrayList<>(entities);
        ret.removeIf(e -> !this.isCollide(extractor.apply(e)));
        return ret;
    }

    public BoundingBox rotate(BlockPos center, EnumRotate rotate) {
        return new BoundingBox(this.start.add(-center.getX(), -center.getY(), -center.getZ()).rotate(EnumRotate.toMc(rotate)), this.end.add(-center.getX(), -center.getY(), -center.getZ()).rotate(EnumRotate.toMc(rotate)));
    }
}