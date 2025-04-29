package com.artur.returnoftheancients.ancientworld.map.utils;

import java.util.Objects;

public class StructurePos {
    private final int x;
    private final int y;

    public StructurePos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public StructurePos offset(StructurePos.Face face) {
        switch (face) {
            case UP:
                return new StructurePos(this.getX(), this.getY() + 1);
            case DOWN:
                return new StructurePos(this.getX(), this.getY() - 1);
            case RIGHT:
                return new StructurePos(this.getX() + 1, this.getY());
            case LEFT:
                return new StructurePos(this.getX() - 1, this.getY());
            default:
                return null;
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
        if (obj instanceof StructurePos) {
            StructurePos pos = (StructurePos) obj;
            return pos.getX() == this.getX() && pos.getY() == this.getY();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getX(), this.getY());
    }

    public enum Face {
        UP,
        RIGHT,
        DOWN,
        LEFT;

        public Face rotate(EnumStructure.Rotate rotate) {
            switch (rotate) {
                case NON:
                    return this;
                case C90:
                    return values()[offsetIndex(this.ordinal(), 1)];
                case C180:
                    return values()[offsetIndex(this.ordinal(), 2)];
                case C270:
                    return values()[offsetIndex(this.ordinal(), 3)];
                default:
                    throw new RuntimeException("Someone forged the universe!");
            }
        }

        public Face getOppose() {
            return this.rotate(EnumStructure.Rotate.C180);
        }

        private int offsetIndex(int index, int count) {
            for (int i = 0; i != count; i++) index = offsetIndex(index);
            return index;
        }

        private int offsetIndex(int index) {
            return index + 1 >= values().length ? 0 : index + 1;
        }

        public static Face[] rotateAll(EnumStructure.Rotate rotate, Face... faces) {
            Face[] ret = new Face[faces.length];
            for (int i = 0; i != ret.length; i++) {
                ret[i] = faces[i].rotate(rotate);
            }
            return ret;
        }
    }
}
