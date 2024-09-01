package com.artur.returnoftheancients.ancientworldgeneration.genmap;

import com.artur.returnoftheancients.ancientworldgeneration.genmap.util.StructureMap;

import java.util.Random;

public class AncientEntryMapProvider {
    public static StructureMap createAncientEntryMap(Random rand) {
        return AncientEntryMapP1.createAncientEntryMap(rand);
    }
}
