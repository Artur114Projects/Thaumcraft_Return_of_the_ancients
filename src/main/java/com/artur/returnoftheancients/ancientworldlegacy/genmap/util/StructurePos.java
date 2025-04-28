package com.artur.returnoftheancients.ancientworldlegacy.genmap.util;

import com.artur.returnoftheancients.ancientworld.map.utils.EnumStructure;

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

         private int offsetIndex(int index, int count) {
             for (int i = 0; i != count; count++) index = offsetIndex(index);
             return index;
         }

         private int offsetIndex(int index) {
             return index + 1 >= values().length ? 0 : index + 1;
         }
    }
}
