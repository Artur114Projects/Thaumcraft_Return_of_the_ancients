package com.artur.returnoftheancients.ancientworld.map.utils;

public enum EnumStructure {
    WAY("ancient_way_rotate-", true, false, StructurePos.Face.RIGHT, StructurePos.Face.LEFT),
    TURN("ancient_turn_rotate-", true, false, StructurePos.Face.UP, StructurePos.Face.RIGHT),
    FORK("ancient_fork_rotate-", true, false, StructurePos.Face.UP, StructurePos.Face.LEFT, StructurePos.Face.RIGHT),
    CROSSROADS("ancient_crossroads", false, false, StructurePos.Face.values()),
    END("ancient_end_rotate-", true, false, StructurePos.Face.LEFT),
    ENTRY("ancient_entry", false, false, StructurePos.Face.values()),
    BOSS("ancient_boss", false, true);


    private final StructurePos.Face[] portsOnDefRotate;
    private final boolean isMultiChunk;
    private final boolean canRotate;
    private final String stringId;
    EnumStructure(String stringId, boolean canRotate, boolean isMultiChunk, StructurePos.Face... portsOnDefRotate) {
        this.portsOnDefRotate = portsOnDefRotate;
        this.isMultiChunk = isMultiChunk;
        this.canRotate = canRotate;
        this.stringId = stringId;
    }

    public String getStringId(Rotate rotate) {
        return this.canRotate ? this.stringId + (rotate.ordinal() + 1) : this.stringId;
    }

    public boolean isMultiChunk() {
        return this.isMultiChunk;
    }

    public StructurePos.Face[] ports(Rotate rotate) {
        return StructurePos.Face.rotateAll(rotate, this.portsOnDefRotate);
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
