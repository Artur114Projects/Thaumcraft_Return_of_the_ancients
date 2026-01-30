package com.artur.returnoftheancients.util.math;

import net.minecraft.util.math.BlockPos;

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

    private static class Backed implements IArea {
        private final List<Box> area;

        private Backed(Raw raw) {
            this.area = this.bake(raw);
        }

        private List<Box> bake(Raw raw) {
            return this.sortBoxes(this.createBoxes(raw.points()));
        }

        private List<Box> createBoxes(List<BlockPos> list) {
            Set<BlockPos> set = new HashSet<>(list);
            List<Box> ret = new ArrayList<>(set.size() * set.size());

            for (BlockPos pos1 : set) {
                for (BlockPos pos2 : set) {
                    if (this.validate(pos1, pos2, set)) {
                        ret.add(new Box(pos1, pos2));
                    }
                }
            }

            return ret;
        }

        private boolean validate(BlockPos from, BlockPos to, Set<BlockPos> set) {
            for (BlockPos.MutableBlockPos pos : BlockPos.getAllInBoxMutable(from, to)) {
                if (!set.contains(pos)) {
                    return false;
                }
            }
            return true;
        }

        private List<Box> sortBoxes(List<Box> boxes) {
            List<Box> boxList = new LinkedList<>(boxes);
            List<Box> ret = new ArrayList<>();

            while (!boxList.isEmpty()) {
                Box bigger = this.findBigger(boxList);

                boxList.remove(bigger);
                boxList.removeIf(bigger::isCollide);

                ret.add(bigger);
            }

            return ret;
        }

        private Box findBigger(List<Box> boxes) {
            return boxes.stream().max(Comparator.comparingInt(Box::areaSize)).orElse(null);
        }

        @Override
        public int areaSize() {
            return 0;
        }

        @Override
        public boolean isCollide(double x, double y, double z) {
            return this.area.stream().anyMatch((b) -> b.isCollide(x, y, z));
        }

        @Override
        public BlockPos fromIndex(int index) {
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
    }
    private static class Box {
        private final BlockPos start;
        private final BlockPos end;
        private final BlockPos size;

        public Box(BlockPos start, BlockPos end) {
            this.end = new BlockPos(Math.max(start.getX(), end.getX()), Math.max(start.getY(), end.getY()), Math.max(start.getZ(), end.getZ()));
            this.start = new BlockPos(Math.min(start.getX(), end.getX()), Math.min(start.getY(), end.getY()), Math.min(start.getZ(), end.getZ()));
            this.size = this.end.add(-this.start.getX(), -this.start.getY(), -this.start.getZ());
        }

        public int areaSize() {
            return this.size.getX() * this.size.getY() * this.size.getZ();
        }

        public boolean isCollide(Box box) {
            boolean ret = false;
            ret |= this.end.getX() >= box.start.getX();
            ret |= this.end.getY() >= box.start.getY();
            ret |= this.end.getZ() >= box.start.getZ();

            ret |= this.start.getX() <= box.end.getX();
            ret |= this.start.getY() <= box.end.getY();
            ret |= this.start.getZ() <= box.end.getZ();
            return ret;
        }

        public boolean isCollide(double x, double y, double z) {
            return x >= this.start.getX() && y >= this.start.getY() && z >= this.start.getZ() && x <= this.end.getX() + 1 && y <= this.end.getY() + 1 && z <= this.end.getZ() + 1;
        }

        public List<BlockPos> points() {
            List<BlockPos> ret = new ArrayList<>();
            for (BlockPos pos : BlockPos.getAllInBox(this.start, this.end)) ret.add(pos);
            return ret;
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
