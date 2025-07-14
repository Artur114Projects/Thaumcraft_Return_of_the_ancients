package com.artur.returnoftheancients.ancientworld.map.utils;

import net.minecraft.util.math.Vec2f;

public enum EnumFace {
    UP(new Vec2f(0, -1)),
    RIGHT(new Vec2f(1, 0)),
    DOWN(new Vec2f(0, 1)),
    LEFT(new Vec2f(-1, 0));

    private final Vec2f directionVec;

    EnumFace(Vec2f directionVec) {
        this.directionVec = directionVec;
    }

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

    public EnumRotate rotateFromDefFace(EnumFace nonFace) {
        for (EnumRotate rotate : EnumRotate.values()) {
            if (nonFace.rotate(rotate) == this) {
                return rotate;
            }
        }
        return EnumRotate.NON;
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

    public static EnumFace fromVector(Vec2f vec2d) {
        return fromVector(vec2d.x, vec2d.y);
    }

    public static EnumFace fromVector(float x, float y) {
        EnumFace face = RIGHT;
        float f = Float.MIN_VALUE;

        for (EnumFace face1 : values()) {
            float f1 = x * face1.directionVec.x + y * face1.directionVec.y;
            if (f1 > f) {
                f = f1;
                face = face1;
            }
        }

        return face;
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
