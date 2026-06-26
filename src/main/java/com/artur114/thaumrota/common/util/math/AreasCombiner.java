package com.artur114.thaumrota.common.util.math;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.*;

public class AreasCombiner {
    private IArea area = null;

    public AreasCombiner addArea(IArea area) {
        if (this.area == null) {
            this.area = area; return this;
        }

        this.area = AreasCombiner.combine(this.area, area);
        return this;
    }

    public AreasCombiner subtractArea(IArea area) {
        if (this.area == null) {
            return this;
        }

        this.area = AreasCombiner.subtract(this.area, area);
        return this;
    }

    public IArea bake() {
        IArea ret = AreasCombiner.bake(this.area);
        this.area = null;
        return ret;
    }

    public static class Backed implements IArea {
        private static final Vec3i x = new Vec3i(1, 0, 0);
        private static final Vec3i y = new Vec3i(0, 1, 0);
        private static final Vec3i z = new Vec3i(0, 0, 1);
        private static final Vec3i xn = new Vec3i(-1, 0, 0);
        private static final Vec3i yn = new Vec3i(0, -1, 0);
        private static final Vec3i zn = new Vec3i(0, 0, -1);

        private final List<Box> area;
        private int areaSize = -1;

        private Backed(Raw raw) {
            this.area = this.bake(raw);
        }

        private List<Box> bake(Raw raw) {
            LinkedList<BlockPos> list = new LinkedList<>(raw.points());
            List<Box> ret = new ArrayList<>();

            while (!list.isEmpty()) {
                BlockPos pos = list.peek();

                Box box = this.expandBox(pos, new HashSet<>(list));

                list.removeIf(box::isCollide);

                ret.add(box);
            }

            return ret;
        }

        private Box expandBox(BlockPos from, Set<BlockPos> allPoints) {
            int fx = from.getX();
            int fy = from.getY();
            int fz = from.getZ();

            int tx = from.getX();
            int ty = from.getY();
            int tz = from.getZ();

            while (this.canExpand(allPoints, x, fx, fy, fz, tx, ty, tz)) {
                tx++;
            }

            while (this.canExpand(allPoints, y, fx, fy, fz, tx, ty, tz)) {
                ty++;
            }

            while (this.canExpand(allPoints, z, fx, fy, fz, tx, ty, tz)) {
                tz++;
            }

            while (this.canExpand(allPoints, xn, fx, fy, fz, tx, ty, tz)) {
                fx--;
            }

            while (this.canExpand(allPoints, yn, fx, fy, fz, tx, ty, tz)) {
                fy--;
            }

            while (this.canExpand(allPoints, zn, fx, fy, fz, tx, ty, tz)) {
                fz--;
            }


            return new Box(new BlockPos(fx, fy, fz), new BlockPos(tx, ty, tz));
        }

        private boolean canExpand(Set<BlockPos> allPoints, Vec3i vec, int fx, int fy, int fz, int tx, int ty, int tz) {
            int sx = tx - fx;
            int sy = ty - fy;
            int sz = tz - fz;

            if (vec.getX() >= 0 && vec.getY() >= 0 && vec.getZ() >= 0) {
                fx += sx * vec.getX();
                fy += sy * vec.getY();
                fz += sz * vec.getZ();

                tx += vec.getX();
                ty += vec.getY();
                tz += vec.getZ();
            } else {
                tx += sx * vec.getX();
                ty += sy * vec.getY();
                tz += sz * vec.getZ();

                fx += vec.getX();
                fy += vec.getY();
                fz += vec.getZ();
            }

            for (BlockPos.MutableBlockPos pos : BlockPos.getAllInBoxMutable(fx, fy, fz, tx, ty, tz)) {
                if (!allPoints.contains(pos)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public int areaSize() {
            if (this.areaSize == -1) {
                this.areaSize = 0;

                for (Box box : this.area) {
                    this.areaSize += box.areaSize();
                }
            }
            return this.areaSize;
        }

        @Override
        public boolean isCollide(double x, double y, double z) {
            return this.area.stream().anyMatch((b) -> b.isCollide(x, y, z));
        }

        @Override
        public BlockPos fromIndex(int index) {
            int i = 0;
            for (Box box : this.area) {
                if (i + box.areaSize() > index) {
                    return box.fromIndex(index - i);
                }

                i += box.areaSize();
            }
            return null;
        }

        @Override
        public List<BlockPos> points() {
            List<BlockPos> ret = new ArrayList<>(this.areaSize());
            for (Box box : this.area) {
                ret.addAll(box.points());
            }
            return ret;
        }

        @Override
        public void renderArea(float alpha) {
            Random rand = new Random(this.hashCode());
            for (Box box : this.area) {
                box.renderBox(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), alpha);
            }
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.area);
        }
    }

    private static class Box {
        private final BlockPos start;
        private final BlockPos end;
        private final BlockPos size;

        public Box(BlockPos start, BlockPos end) {
            this.end = new BlockPos(Math.max(start.getX(), end.getX()), Math.max(start.getY(), end.getY()), Math.max(start.getZ(), end.getZ()));
            this.start = new BlockPos(Math.min(start.getX(), end.getX()), Math.min(start.getY(), end.getY()), Math.min(start.getZ(), end.getZ()));
            this.size = this.end.add(-this.start.getX(), -this.start.getY(), -this.start.getZ()).add(1, 1, 1);
        }

        public int areaSize() {
            return this.size.getX() * this.size.getY() * this.size.getZ();
        }

        public BlockPos fromIndex(int index) {
            return this.start.add((index % size.getX()), ((index / size.getX()) % size.getY()), (((index / size.getX()) / size.getY()) % size.getZ()));
        }

        public boolean isCollide(BlockPos pos) {
            return this.isCollide(pos.getX(), pos.getY(), pos.getZ());
        }

        public boolean isCollide(double x, double y, double z) {
            return x >= this.start.getX() && y >= this.start.getY() && z >= this.start.getZ() && x < this.end.getX() + 1 && y < this.end.getY() + 1 && z < this.end.getZ() + 1;
        }

        public void renderBox(float r, float g, float b, float a) {
            Minecraft mc = Minecraft.getMinecraft();
            EntityPlayer player = mc.player;

            double x = Particle.interpPosX;
            double y = Particle.interpPosY;
            double z = Particle.interpPosZ;
            double d = 0.001;

            RenderGlobal.drawBoundingBox(this.start.getX() - x + d, this.start.getY() - y + d, this.start.getZ() - z + d, this.end.getX() + 1 - x - d, this.end.getY() + 1 - y - d, this.end.getZ() + 1 - z - d, r, g, b, a);
        }

        public List<BlockPos> points() {
            List<BlockPos> ret = new ArrayList<>();
            for (BlockPos pos : BlockPos.getAllInBox(this.start, this.end)) ret.add(pos);
            return ret;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.start, this.end);
        }
    }

    private static class Raw implements IArea {
        private final UltraMutableBlockPos util = new UltraMutableBlockPos();
        private final List<BlockPos> area;

        private Raw(List<BlockPos> area) {
            this.area = area;
        }

        @Override
        public int areaSize() {
            return this.area.size();
        }

        @Override
        public boolean isCollide(double x, double y, double z) {
            return false; //
        }

        @Override
        public BlockPos fromIndex(int index) {
            if (index > this.areaSize()) {
                return null;
            }
            return this.area.get(index);
        }

        @Override
        public List<BlockPos> points() {
            return this.area;
        }

        @Override
        public void renderArea(float alpha) {
            Minecraft mc = Minecraft.getMinecraft();
            EntityPlayer player = mc.player;

            double x = Particle.interpPosX;
            double y = Particle.interpPosY;
            double z = Particle.interpPosZ;
            double d = 0.001;

            for (BlockPos pos : this.area) {
                RenderGlobal.drawBoundingBox(pos.getX() - x + d, pos.getY() - y + d, pos.getZ() - z + d, pos.getX() + 1 - x - d, pos.getY() + 1 - y - d, pos.getZ() + 1 - z - d, 1.0F, 1.0F, 1.0F, alpha);
            }
        }
    }

    public static IArea combine(IArea area1, IArea area2) {
        List<BlockPos> points1 = area1.points();
        List<BlockPos> points2 = area2.points();
        List<BlockPos> ret = new ArrayList<>(points1.size() + points2.size());
        ret.addAll(points1);
        ret.addAll(points2);
        return new Raw(ret);
    }

    public static IArea subtract(IArea from, IArea area) {
        List<BlockPos> fromPoints = from.points();
        List<BlockPos> areaPoints = area.points();
        List<BlockPos> ret = new ArrayList<>(fromPoints);
        ret.removeAll(areaPoints);
        return new Raw(ret);
    }

    public static IArea bake(IArea raw) {
        if (raw instanceof Raw) {
            return new Backed((Raw) raw);
        }
        return raw;
    }
}
