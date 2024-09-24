package com.artur.returnoftheancients.ancientworldgeneration.genmap;

import com.artur.returnoftheancients.ancientworldgeneration.genmap.util.StructureMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Random;

public class AncientEntryMapP1Test {
    @Test
    public void foundAndCheckWeyToEntryTest() {
        StructureMap badMap = AncientEntryMapP0.genStructuresMap(new Random(5578899088763705022L));
        StructureMap goodMap = AncientEntryMapP0.genStructuresMap(new Random(0));
        Method method = ReflectionUtils.findMethod(AncientEntryMapP1.class, "foundAndCheckWeyToEntry", StructureMap.class).get();
        Assertions.assertFalse((Boolean) ReflectionUtils.invokeMethod(method, new AncientEntryMapP1(),  badMap));
        Assertions.assertTrue((Boolean) ReflectionUtils.invokeMethod(method, new AncientEntryMapP1(), goodMap));
        Assertions.assertFalse((Boolean) ReflectionUtils.invokeMethod(method, new AncientEntryMapP1(),  badMap));
    }
}
