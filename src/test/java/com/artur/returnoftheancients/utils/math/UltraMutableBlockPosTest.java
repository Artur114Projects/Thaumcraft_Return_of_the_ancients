package com.artur.returnoftheancients.utils.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UltraMutableBlockPosTest {

    @Test
    public void testContextSystem() {
        UltraMutableBlockPos pos = new UltraMutableBlockPos(10, 5, 8);
        
        Assertions.assertThrows(RuntimeException.class, pos::popPos);

        pos.pushPos();
        pos.add(1, 5, 8);
        Assertions.assertEquals(11, pos.getX());
        Assertions.assertEquals(10, pos.getY());
        Assertions.assertEquals(16, pos.getZ());
        pos.popPos();
        Assertions.assertEquals(10, pos.getX());
        Assertions.assertEquals(5, pos.getY());
        Assertions.assertEquals(8, pos.getZ());


        for (int x = 0; x != 10; x++) {
            pos.add(10 * x, 0 ,0);
            pos.pushPos();
            pos.add(-(10 * x), 0 ,0);
        }

        for (int x = 10; x > 0; x--) {
            pos.popPos();
            Assertions.assertEquals((10 * x), pos.getX());
        }


        for (int x = 0; x != 10; x++) {
            pos.add(10 * x, 0 ,0);
            pos.pushPos();
            pos.add(-(10 * x), 0 ,0);
        }

        for (int x = 10; x > 0; x--) {
            pos.popPos();
            Assertions.assertEquals((10 * x), pos.getX());
        }


        Assertions.assertThrows(RuntimeException.class, pos::popPos);

        pos.pushPos();
        pos.add(1, 5, 8);
        Assertions.assertEquals(11, pos.getX());
        Assertions.assertEquals(10, pos.getY());
        Assertions.assertEquals(16, pos.getZ());
        pos.popPos();
        Assertions.assertEquals(10, pos.getX());
        Assertions.assertEquals(5, pos.getY());
        Assertions.assertEquals(8, pos.getZ());

    }
}
