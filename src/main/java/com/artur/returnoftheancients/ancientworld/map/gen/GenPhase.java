package com.artur.returnoftheancients.ancientworld.map.gen;

import com.artur.returnoftheancients.ancientworld.map.utils.StructuresMap;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public abstract class GenPhase {
    public static GenPhase initAllGenPhases() {
        GenPhase adaptLegacy = new GenPhaseAdaptLegacy();
        GenPhase checked = new GenPhaseCheckLegacy(adaptLegacy);
        return checked;
    }

    protected GenPhase parent = null;
    public GenPhase() {}

    public GenPhase(GenPhase parent) {
        this.parent = parent;
    }

    public abstract @NotNull StructuresMap getMap(long seed, int size);
}
