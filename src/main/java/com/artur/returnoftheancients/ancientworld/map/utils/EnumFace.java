package com.artur.returnoftheancients.ancientworld.map.utils;

public enum EnumFace {
    UP,
    RIGHT,
    DOWN,
    LEFT;

    public EnumFace rotate(EnumRotate rotate) {
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

    public EnumFace getOppose() {
        return this.rotate(EnumRotate.C180);
    }

    public int xOffset() {
        switch (this) {
            case RIGHT:
                return 1;
            case LEFT:
                return -1;
            default:
                return 0;
        }
    }

    public int yOffset() {
        switch (this) {
            case UP:
                return -1;
            case DOWN:
                return 1;
            default:
                return 0;
        }
    }

    public Axis axis() {
        switch (this) {
            case UP:
            case DOWN:
                return Axis.Y;
            case LEFT:
            case RIGHT:
                return Axis.X;
            default:
                throw new IllegalArgumentException("Wtf?!");
        }
    }

    private int offsetIndex(int index, int count) {
        for (int i = 0; i != count; i++) index = offsetIndex(index);
        return index;
    }

    private int offsetIndex(int index) {
        return index + 1 >= values().length ? 0 : index + 1;
    }

    public static EnumFace[] rotateAll(EnumRotate rotate, EnumFace... faces) {
        EnumFace[] ret = new EnumFace[faces.length];
        for (int i = 0; i != ret.length; i++) {
            ret[i] = faces[i].rotate(rotate);
        }
        return ret;
    }

    public static EnumFace[] allFromAxis(Axis axis) {
        switch (axis) {
            case X:
                return new EnumFace[] {LEFT, RIGHT};
            case Y:
                return new EnumFace[] {UP, DOWN};
            default:
                throw new IllegalArgumentException("Wtf?!");
        }
    }

    public enum Axis {
        X,
        Y;

        public Axis opposite() {
            switch (this) {
                case X:
                    return Y;
                case Y:
                    return X;
                default:
                    throw new IllegalStateException("Wtf?!");
            }
        }
    }
}
