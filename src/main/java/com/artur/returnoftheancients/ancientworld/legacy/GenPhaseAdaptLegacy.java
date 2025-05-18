package com.artur.returnoftheancients.ancientworld.legacy;

import com.artur.returnoftheancients.ancientworld.map.gen.GenPhase;
import com.artur.returnoftheancients.ancientworld.map.utils.EnumFace;
import com.artur.returnoftheancients.ancientworld.map.utils.maps.ImmutableMap;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class GenPhaseAdaptLegacy extends GenPhase {
    @Override
    public @NotNull ImmutableMap getMap(long seed, int size) {
        if (size != 17) {
            throw new IllegalArgumentException("Legacy gen not support mutable size!");
        }
        ImmutableMap map = new ImmutableMap(size);
        byte[][][] legacyMap = AncientLayer1LegacyGen.genStructuresMap(new Random(seed));
        byte[][] legacyMapStructures = legacyMap[0];
        byte[][] legacyMapRotates = legacyMap[1];
        for (byte y = 0; y != AncientLayer1LegacyGen.SIZE; y++) {
            for (byte x = 0; x != AncientLayer1LegacyGen.SIZE; x++) {
                int structure = legacyMapStructures[y][x];
                int rotate = legacyMapRotates[y][x];
                if (structure < 0) structure = -structure;

//                switch (structure) {
//                    case AncientLayer1LegacyGen.LADDER_ID:
//                        map.insetStructure(new StructureBase(EnumRotate.asId(rotate), EnumStructureType.asId(structure), new StrPos(x, y)).setY(79));
//                        break;
//                    case AncientLayer1LegacyGen.ENTRY_ID:
//                        if (this.isCenter(x, y, AncientLayer1LegacyGen.ENTRY_ID, legacyMapStructures)) {
//                            map.insetStructure(new StructureEntry(EnumRotate.asId(rotate), new StrPos(x, y)));
//                        }
//                        break;
//                    case AncientLayer1LegacyGen.BOSS_ID:
//                        if (this.isCenter(x, y, AncientLayer1LegacyGen.BOSS_ID, legacyMapStructures)) {
//                            map.insetStructure(new StructureBoss(EnumRotate.NON, new StrPos(x, y)));
//                        }
//                        break;
//                    case 0:
//                        break;
//                    default:
//                        map.createBaseStructure(x, y, EnumStructureType.asId(structure), EnumRotate.asId(rotate));
//                }
            }
        }
        return map;
    }

    private boolean isCenter(int x, int y, byte structure, byte[][] legacyMapStructures) {
        for (EnumFace face : EnumFace.values()) {
            int fx = x + face.xOffset();
            int fy = y + face.yOffset();

            if (fx < 0 || fy < 0 || fx >= AncientLayer1LegacyGen.SIZE || fy >= AncientLayer1LegacyGen.SIZE || legacyMapStructures[fy][fx] != structure) {
                return false;
            }
        }
        return true;
    }
}
