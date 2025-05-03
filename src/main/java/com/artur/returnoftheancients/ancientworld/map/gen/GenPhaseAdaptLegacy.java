package com.artur.returnoftheancients.ancientworld.map.gen;

import com.artur.returnoftheancients.ancientworld.legacy.AncientLayer1LegacyGen;
import com.artur.returnoftheancients.ancientworld.map.utils.EnumStructure;
import com.artur.returnoftheancients.ancientworld.map.utils.StructurePos;
import com.artur.returnoftheancients.ancientworld.map.utils.StructuresMap;
import com.artur.returnoftheancients.ancientworld.map.utils.structures.IStructure;
import com.artur.returnoftheancients.ancientworld.map.utils.structures.StructureBase;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class GenPhaseAdaptLegacy extends GenPhase {
    @Override
    public @NotNull StructuresMap getMap(long seed, int size) {
        if (size != 17) {
            throw new IllegalArgumentException("Legacy gen not support mutable size!");
        }
        StructuresMap map = new StructuresMap(size);
        byte[][][] legacyMap = AncientLayer1LegacyGen.genStructuresMap(new Random(seed));
        byte[][] legacyMapStructures = legacyMap[0];
        byte[][] legacyMapRotates = legacyMap[1];
        for (byte y = 0; y != AncientLayer1LegacyGen.SIZE; y++) {
            for (byte x = 0; x != AncientLayer1LegacyGen.SIZE; x++) {
                int structure = legacyMapStructures[y][x];
                int rotate = legacyMapRotates[y][x];
                if (structure < 0) structure = -structure;

                map.createBaseStructure(x, y, EnumStructure.asId(structure), EnumStructure.Rotate.asId(rotate));
            }
        }
        return map;
    }
}
