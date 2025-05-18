package com.artur.returnoftheancients.ancientworld.map.gen;

import com.artur.returnoftheancients.ancientworld.map.utils.EnumStructureType;
import com.artur.returnoftheancients.ancientworld.map.utils.maps.ImmutableMap;
import org.jetbrains.annotations.NotNull;

public class GenPhaseBuildWays extends GenPhase {
    public GenPhaseBuildWays(GenPhase parent) {
        super(parent);


    }



    @Override
    public @NotNull ImmutableMap getMap(long seed, int size) {
        ImmutableMap map = this.parent.getMap(seed, size);

        return map;
    }



    private static class RandomReplacerBase {
        private RandomReplacerBase(EnumStructureType type, float chance) {

        }
    }
}
