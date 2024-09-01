package com.artur.returnoftheancients.ancientworldgeneration.genmap.util;

public class StructurePos {
    public final int x;
    public final int y;

    public StructurePos(int x, int y) {
        this.x = x;
        this.y = y;
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
}
