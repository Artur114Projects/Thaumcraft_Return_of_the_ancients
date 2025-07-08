package com.artur.returnoftheancients.ancientworld.map.utils;

import net.minecraft.util.Rotation;

public enum EnumRotate {
    NON(1),
    C90(2),
    C180(4),
    C270(3);

    public final int id;
    EnumRotate(int id) {
        this.id = id;
    }

    public static Rotation toMc(EnumRotate rotate) {
        switch (rotate) {
            case C90:
                return Rotation.CLOCKWISE_90;
            case C180:
                return Rotation.CLOCKWISE_180;
            case C270:
                return Rotation.COUNTERCLOCKWISE_90;
            default:
                return Rotation.NONE;
        }
    }
}
