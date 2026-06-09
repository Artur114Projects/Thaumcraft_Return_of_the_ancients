package com.artur114.returnoftheancients.ancientworldlegacy.genmap;

import com.artur114.returnoftheancients.common.ancientworldlegacy.genmap.AncientEntryMapP0;
import com.artur114.returnoftheancients.common.ancientworldlegacy.genmap.AncientEntryMapP1;
import com.artur114.returnoftheancients.common.ancientworldlegacy.genmap.util.StructureMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Random;

public class AncientEntryMapP1Test {
    @Test
    public void foundAndCheckWeyToEntryTest() {
        StructureMap badMap = AncientEntryMapP0.genStructuresMap(new Random(5578899088763705022L));
        StructureMap goodMap = AncientEntryMapP0.genStructuresMap(new Random(0));
        Optional<Method> optionalMethod = ReflectionUtils.findMethod(AncientEntryMapP1.class, "checkWayToEntry", StructureMap.class);
        Assertions.assertTrue(optionalMethod.isPresent());
        Method method = optionalMethod.get();
        Assertions.assertFalse((Boolean) ReflectionUtils.invokeMethod(method, null,  badMap));
        Assertions.assertTrue((Boolean) ReflectionUtils.invokeMethod(method, null, goodMap));
        Assertions.assertFalse((Boolean) ReflectionUtils.invokeMethod(method, null,  badMap));
    }
}
