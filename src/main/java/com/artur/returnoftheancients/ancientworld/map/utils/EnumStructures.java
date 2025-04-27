package com.artur.returnoftheancients.ancientworld.map.utils;

public enum EnumStructures {

    A(0, "");



    private final String stringId;
    private final int intId;
    EnumStructures(int intId, String stringId) {
        this.stringId = stringId;
        this.intId = intId;
    }

    public int getIntId() {
        return intId;
    }

    public String getStringId() {
        return stringId;
    }

    enum Rotate {

    }
}
