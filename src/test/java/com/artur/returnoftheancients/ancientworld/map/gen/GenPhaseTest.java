package com.artur.returnoftheancients.ancientworld.map.gen;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenPhaseTest {

    @Test
    void initAllGenPhases() {
        GenPhase phase = GenPhase.initAllGenPhases();
        phase.getMap(System.nanoTime(), 17);


        long time = System.nanoTime();

        phase.getMap(System.nanoTime(), 17);

        System.out.println("Is took " + ((System.nanoTime() - time) /  1000000.0F) + "ms");


        time = System.nanoTime();

        phase.getMap(System.nanoTime(), 17);

        System.out.println("Is took " + ((System.nanoTime() - time) /  1000000.0F) + "ms");


        time = System.nanoTime();

        phase.getMap(System.nanoTime(), 17);

        System.out.println("Is took " + ((System.nanoTime() - time) /  1000000.0F) + "ms");


        time = System.nanoTime();

        phase.getMap(System.nanoTime(), 33);

        System.out.println("Is took " + ((System.nanoTime() - time) /  1000000.0F) + "ms");
    }

    @Test
    void getMap() {
    }
}