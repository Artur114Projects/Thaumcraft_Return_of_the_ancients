package com.artur.returnoftheancients.generation.generators;

import com.artur.returnoftheancients.ancientworldlegacy.util.interfaces.IALGS;

public class AncientLabyrinthGeneratorHandler implements IALGS {

    public static int getVoidStructures(byte[][] array) {
        int i = 0;
        for (int y = 0; y != array.length; y++) {
            for (int x = 0; x != array.length; x++) {
                if (array[y][x] == 0) {
                    i++;
                }
            }
        }
        return i;
    }

    public static int getVoidStructures(byte[] array) {
        int i = 0;
        for (int q = 0; q != array.length; q++) {
            if (array[q] == 0) {
                i++;
            }
        }
        return i;
    }

}
