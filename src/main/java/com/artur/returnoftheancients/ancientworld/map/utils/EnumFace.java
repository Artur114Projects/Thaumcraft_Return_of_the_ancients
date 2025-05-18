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
}
