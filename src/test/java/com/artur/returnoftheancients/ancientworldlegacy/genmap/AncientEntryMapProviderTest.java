package com.artur.returnoftheancients.ancientworldlegacy.genmap;

import com.artur.returnoftheancients.ancientworldlegacy.genmap.util.StructureMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class AncientEntryMapProviderTest {

    @Test
    public void createAncientEntryMapTest() {
        for (int i = 0; i != 10; i++){
            long seed = new Random().nextLong();
            System.out.println(seed);
            StructureMap map0 = AncientEntryMapProvider.createAncientEntryMap(new Random(seed));
            System.out.println(seed);
            StructureMap map1 = AncientEntryMapProvider.createAncientEntryMap(new Random(seed));
            Assertions.assertEquals(map0, map1);
            Assertions.assertTrue(map0.equals(map1));
        }
    }
}
