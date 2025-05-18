package com.artur.returnoftheancients.ancientworld.map.utils;

import java.util.Objects;

public class StrPos {
    private final int x;
    private final int y;

    public StrPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public StrPos offset(EnumFace face) {
        switch (face) {
            case UP:
                return new StrPos(this.getX(), this.getY() - 1);
            case DOWN:
                return new StrPos(this.getX(), this.getY() + 1);
            case RIGHT:
                return new StrPos(this.getX() + 1, this.getY());
            case LEFT:
                return new StrPos(this.getX() - 1, this.getY());
            default:
                return this;
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
            switch (face) {
                case UP:
                    return this.setPos(this.getX(), this.getY() - 1);
                case DOWN:
                    return this.setPos(this.getX(), this.getY() + 1);
                case RIGHT:
                    return this.setPos(this.getX() + 1, this.getY());
                case LEFT:
                    return this.setPos(this.getX() - 1, this.getY());
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
    }
}
