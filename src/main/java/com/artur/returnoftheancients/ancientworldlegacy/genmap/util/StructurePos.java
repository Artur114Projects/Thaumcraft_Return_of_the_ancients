package com.artur.returnoftheancients.ancientworldlegacy.genmap.util;

import java.util.Objects;

public class StructurePos {
    private final int x;
    private final int y;

    public StructurePos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public StructurePos offset(Face face) {
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
        DOWN,
        RIGHT,
        LEFT
    }
}
