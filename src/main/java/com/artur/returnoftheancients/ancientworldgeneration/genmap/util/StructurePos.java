package com.artur.returnoftheancients.ancientworldgeneration.genmap.util;

public class StructurePos {
    public final int x;
    public final int y;

    public StructurePos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public StructurePos offset(Face face) {
        switch (face) {
            case UP:
                return new StructurePos(x, y + 1);
            case DOWN:
                return new StructurePos(x, y - 1);
            case RIGHT:
                return new StructurePos(x + 1, y);
            case LEFT:
                return new StructurePos(x - 1, y);
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        return "{x:" + x + ", y:" + y + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StructurePos) {
            StructurePos pos = (StructurePos) obj;
            return pos.x == x && pos.y == y;
        } else {
            return false;
        }
    }

    public enum Face {
        UP,
        DOWN,
        RIGHT,
        LEFT
    }
}
