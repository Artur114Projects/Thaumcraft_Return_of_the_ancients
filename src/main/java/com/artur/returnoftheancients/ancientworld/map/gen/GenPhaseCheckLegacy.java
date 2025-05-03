package com.artur.returnoftheancients.ancientworld.map.gen;

import com.artur.returnoftheancients.ancientworld.map.utils.StructuresMap;
import org.jetbrains.annotations.NotNull;

public class GenPhaseCheckLegacy extends GenPhase {

    public GenPhaseCheckLegacy(GenPhase parent) {
        super(parent);
    }

    @Override
    public @NotNull StructuresMap getMap(long seed, int size) {
        return this.parent.getMap(seed, size);
    }
}
