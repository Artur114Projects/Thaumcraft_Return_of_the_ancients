package com.artur.returnoftheancients.ancientworldgeneration.genmap;

import com.artur.returnoftheancients.ancientworldgeneration.genmap.util.StructureMap;

import java.util.Random;

public class AncientEntryMapP1 extends AncientEntryMapP0 {
    private static final int SIZE = 17;
    public static StructureMap createAncientEntryMap(Random rand) {
        StructureMap structureMap = AncientEntryMapP0.genStructuresMap(rand);

        return structureMap;
    }

    private static void removeBossN(StructureMap map) {
        for (byte y = 0; y != SIZE; y++) {
            for (byte x = 0; x != SIZE; x++) {
                if (map.getStructure(x, y) == BOSS_N_ID) {
                    map.setStructure(x, y, BOSS_ID);
                }
            }
        }
        map.swapBuffers();
    }
}
