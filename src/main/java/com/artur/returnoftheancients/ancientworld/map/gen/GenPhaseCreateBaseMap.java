package com.artur.returnoftheancients.ancientworld.map.gen;

import com.artur.returnoftheancients.ancientworld.map.utils.EnumRotate;
import com.artur.returnoftheancients.ancientworld.map.utils.StrPos;
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
        map.insetStructure(new StructureEntry(new StrPos(size / 2, size / 2)));

        return map;
    }
}
