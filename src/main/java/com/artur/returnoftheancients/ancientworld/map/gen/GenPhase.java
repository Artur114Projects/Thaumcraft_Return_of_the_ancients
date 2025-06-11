package com.artur.returnoftheancients.ancientworld.map.gen;

import com.artur.returnoftheancients.ancientworld.map.utils.maps.ImmutableMap;
import org.jetbrains.annotations.NotNull;

public abstract class GenPhase {
    private static GenPhase instance;

    public static GenPhase initAllGenPhases() {
        GenPhase baseMap = new GenPhaseCreateBaseMap();
        GenPhase build = new GenPhaseBuildWays(baseMap);
        return build;
    }

    public static GenPhase InstanceAllGenPhases() {
        if (instance == null) {
            instance = initAllGenPhases();
        }

        return instance;
    }

    protected GenPhase parent = null;
    public GenPhase() {}

    public GenPhase(GenPhase parent) {
        this.parent = parent;
    }

    public abstract @NotNull ImmutableMap getMap(long seed, int size);
}
