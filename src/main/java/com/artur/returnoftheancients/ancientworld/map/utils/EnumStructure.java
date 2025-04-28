package com.artur.returnoftheancients.ancientworld.map.utils;

public enum EnumStructure {
    WAY("ancient_way_rotate-", true, false),
    TURN("ancient_turn_rotate-", true, false),
    FORK("ancient_fork_rotate-", true, false),
    CROSSROADS("ancient_crossroads", false, false),
    END("ancient_end_rotate-", true, false),
    ENTRY("ancient_entry", false, false),
    BOSS("ancient_boss", false, true);

    private final boolean isMultiChunk;
    private final boolean canRotate;
    private final String stringId;
    EnumStructure(String stringId, boolean canRotate, boolean isMultiChunk) {
        this.isMultiChunk = isMultiChunk;
        this.canRotate = canRotate;
        this.stringId = stringId;
    }

    public String getStringId(Rotate rotate) {
        return canRotate ? stringId + (rotate.ordinal() + 1) : stringId;
    }

    public boolean isMultiChunk() {
        return isMultiChunk;
    }

    public enum Rotate {
        NON,
        C90,
        C270,
        C180;

        public static Rotate asId(int id) {
            if (id > values().length) throw new IndexOutOfBoundsException();
            return values()[id - 1];
        }
    }

    public static EnumStructure asId(int id) {
        if (id > values().length) throw new IndexOutOfBoundsException();
        return values()[id - 1];
    }
}
