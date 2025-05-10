package com.artur.returnoftheancients.ancientworld.map.gen;

import com.artur.returnoftheancients.ancientworld.map.utils.StructuresMap;
import org.jetbrains.annotations.NotNull;

public abstract class GenPhase {
    public static GenPhase initAllGenPhases() {
        GenPhase adaptedLegacy = new GenPhaseAdaptLegacy();
        GenPhase checkedLegacy = new GenPhaseCheckLegacy(adaptedLegacy);
        return checkedLegacy;
    }

    protected GenPhase parent = null;
    public GenPhase() {}

    public GenPhase(GenPhase parent) {
        this.parent = parent;
    }

    public abstract @NotNull StructuresMap getMap(long seed, int size);
}
