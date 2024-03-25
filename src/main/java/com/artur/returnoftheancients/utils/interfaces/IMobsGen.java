package com.artur.returnoftheancients.utils.interfaces;

public interface IMobsGen {

    default void gen(byte[][] array) {
        if (array.length == 17) {
            for (byte y = 0; y != 17; y++) {
                for (byte x = 0; x != 17; x++) {
                    int cx = 136 - 16 * x;
                    int cz = 136 - 16 * y;
                    if (array[y][x] != 0) {
                        for (byte i = 0; i != array[y][x]; i++) {
                            genMob(cx, cz);
                        }
                    }
                }
            }
        } else {
            System.out.println("ti durak?");
        }
    }

    void genMob(int x, int z);
}
