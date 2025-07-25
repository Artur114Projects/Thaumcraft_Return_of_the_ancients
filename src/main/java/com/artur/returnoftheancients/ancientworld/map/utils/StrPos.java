package com.artur.returnoftheancients.ancientworld.map.utils;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

public class StrPos {
    private final int x;
    private final int y;

    public StrPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public StrPos(long data) {
        this((int) (data), (int) (data >> 32));
    }

    public StrPos offset(EnumFace face) {
        return this.offset(face, 1);
    }

    public StrPos offset(EnumFace face, int n) {
        switch (face) {
            case UP:
                return new StrPos(this.getX(), this.getY() - n);
            case DOWN:
                return new StrPos(this.getX(), this.getY() + n);
            case RIGHT:
                return new StrPos(this.getX() + n, this.getY());
            case LEFT:
                return new StrPos(this.getX() - n, this.getY());
            default:
                return this;
        }
    }

    public StrPos rotate(EnumRotate rotate) {
        switch (rotate) {
            case NON:
            default:
                return this;
            case C90:
                return new StrPos(-this.getY(), this.getX());
            case C180:
                return new StrPos(-this.getX(), -this.getY());
            case C270:
                return new StrPos(this.getY(), -this.getX());
        }
    }

    public boolean isOutOfBounds(int areaSize) {
        return this.getX() < 0 || this.getX() >= areaSize || this.getY() < 0 || this.getY() >= areaSize;
    }

    public boolean isOutOfBounds(int areaMin, int areaMax) {
        return this.isOutOfBounds(areaMin, areaMax, areaMin, areaMax);
    }

    public boolean isOutOfBounds(int xSizeMin, int xSizeMax, int ySizeMin, int ySizeMax) {
        return this.getX() < xSizeMin || this.getX() > xSizeMax || this.getY() < ySizeMin || this.getY() > ySizeMax;
    }

    public StrPos toImmutable() {
        return this;
    }

    public long asLong() {
        return ((long) this.getX() & 4294967295L) | ((long) this.getY() & 4294967295L) << 32;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "[x:" + this.getX() + ", y:" + this.getY() + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StrPos) {
            StrPos pos = (StrPos) obj;
            return pos.getX() == this.getX() && pos.getY() == this.getY();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getX(), this.getY());
    }


    public static class MutableStrPos extends StrPos {
        private int x;
        private int y;

        public MutableStrPos(StrPos pos) {
            this(pos.getX(), pos.getY());
        }

        public MutableStrPos(int x, int y) {
            super(0, 0);

            this.x = x;
            this.y = y;
        }

        public MutableStrPos() {
            this(0, 0);
        }

        @Override
        public int getX() {
            return x;
        }

        @Override
        public int getY() {
            return y;
        }

        @Override
        public MutableStrPos offset(EnumFace face) {
            return this.offset(face, 1);
        }

        @Override
        public MutableStrPos offset(EnumFace face, int n) {
            switch (face) {
                case UP:
                    return this.setPos(this.getX(), this.getY() - n);
                case DOWN:
                    return this.setPos(this.getX(), this.getY() + n);
                case RIGHT:
                    return this.setPos(this.getX() + n, this.getY());
                case LEFT:
                    return this.setPos(this.getX() - n, this.getY());
                default:
                    return this;
            }
        }

        public MutableStrPos setPos(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public MutableStrPos setPos(StrPos pos) {
            return this.setPos(pos.getX(), pos.getY());
        }

        public MutableStrPos fromLong(long data) {
            return this.setPos((int) (data), (int) (data >> 32));
        }

        @Override
        public StrPos toImmutable() {
            return new StrPos(this.getX(), this.getY());
        }
    }
}
