package com.artur.returnoftheancients.ancientworld.map.utils;

public enum EnumRotate {
    NON(1),
    C90(2),
    C180(4),
    C270(3);

    public final int id;
    EnumRotate(int id) {
        this.id = id;
    }
}
