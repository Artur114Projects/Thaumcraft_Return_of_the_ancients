package com.artur.returnoftheancients.ancientworld.map.gen;

import com.artur.returnoftheancients.ancientworld.map.utils.*;
import com.artur.returnoftheancients.ancientworld.map.utils.maps.ImmutableMap;
import com.artur.returnoftheancients.ancientworld.map.utils.structures.StructureEntry;
import org.jetbrains.annotations.NotNull;

public class GenPhaseCreateBaseMap extends GenPhase {
    @Override
    public @NotNull ImmutableMap getMap(long seed, int size) {
        if (size % 2 == 0) {
            throw new IllegalStateException("The size must not be even!");
        }

        ImmutableMap map = new ImmutableMap(size);

        StrPos center = new StrPos(size / 2, size / 2);

        map.insetStructure(EnumMultiChunkStrType.ENTRY.create(EnumRotate.NON, center).up(16));

        for (int i = 0; i != 4; i++) {
            map.insetStructure(EnumStructureType.LADDER.create(EnumRotate.values()[i], center.offset(EnumFace.values()[i + 1 >= 4 ? 0 : i + 1], 2)).up(8));
            map.insetStructure(EnumStructureType.LADDER.create(EnumRotate.values()[i], center.offset(EnumFace.values()[i + 1 >= 4 ? 0 : i + 1], 3)));
        }

        return map;
    }
}
