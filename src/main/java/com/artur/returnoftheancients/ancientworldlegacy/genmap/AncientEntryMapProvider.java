package com.artur.returnoftheancients.ancientworldlegacy.genmap;

import com.artur.returnoftheancients.ancientworldlegacy.genmap.util.StructureMap;
import com.artur.returnoftheancients.misc.TRAConfigs;

import java.util.Random;

public class AncientEntryMapProvider {
    public static StructureMap createAncientEntryMap(Random rand) {
        long time = System.nanoTime();
        StructureMap map = AncientEntryMapP1.createAncientEntryMap(rand);
        if (TRAConfigs.Any.debugMode) System.out.println("Created new map! Is took " + ((System.nanoTime() - time) / 1000000.0F) + "ms");
        return map;
    }
}
