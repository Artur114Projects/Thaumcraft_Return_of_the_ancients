package com.artur114.thaumrota.common.worldstate.ancientworld.map.gen;

import com.artur114.thaumrota.common.worldstate.ancientworld.map.utils.maps.ImmutableMap;
import org.junit.jupiter.api.Test

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

class GenPhaseTest {

    boolean equals(ImmutableMap map1, ImmutableMap map2) {
        for (i in 0..map1.area()) {
            if (map1.structureType(i) != map2.structureType(i)) {
                return false
            }
        }

        return true
    }
}