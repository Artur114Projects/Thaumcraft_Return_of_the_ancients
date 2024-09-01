package com.artur.returnoftheancients.ancientworldgeneration.genmap;

import com.artur.returnoftheancients.ancientworldgeneration.genmap.util.StructureMap;
import com.artur.returnoftheancients.ancientworldgeneration.genmap.util.StructurePos;
import com.artur.returnoftheancients.utils.interfaces.IALGS;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class StructureMapTest implements IALGS {

    StructureMap structureMap;

    @BeforeEach
    public void setUp() {
        byte[][] structures = new byte[][] {
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, WAY_ID, WAY_ID, WAY_ID, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
        };
        byte[][] structuresRotate = new byte[][] {
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 1, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
        };
        structureMap = new StructureMap(structures, structuresRotate);

    }


    @Test
    public void testGetConnectedStructures() {
        List<StructurePos> posList = structureMap.getConnectedStructures(3, 3);
        System.out.println(posList);
        Assertions.assertEquals(WAY_ID, structureMap.getStructure(3, 3));
        Assertions.assertEquals(1, structureMap.getRotate(3, 3));
        Assertions.assertEquals(2, posList.size());

        byte[][] structures = new byte[][] {
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, WAY_ID, 0, 0, 0},
                {0, 0, WAY_ID, FORK_ID, WAY_ID, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
        };
        byte[][] structuresRotate = new byte[][] {
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 2, 0, 0, 0},
                {0, 0, 1, 1, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
        };
        structureMap = new StructureMap(structures, structuresRotate);
        posList = structureMap.getConnectedStructures(3, 3);
        Assertions.assertEquals(3, posList.size());
        List<StructurePos> posList1 = new ArrayList<>();
        posList1.add(new StructurePos(4, 3));
        posList1.add(new StructurePos(3, 2));
        posList1.add(new StructurePos(2, 3));
        Assertions.assertEquals(posList1, posList);

        structures = new byte[][] {
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, WAY_ID, 0, 0, 0},
                {0, 0, WAY_ID, TURN_ID, WAY_ID, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
        };
        structuresRotate = new byte[][] {
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 2, 0, 0, 0},
                {0, 0, 1, 1, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0},
        };
        structureMap = new StructureMap(structures, structuresRotate);
        posList = structureMap.getConnectedStructures(3, 3);
        Assertions.assertEquals(2, posList.size());
        posList1 = new ArrayList<>();
        posList1.add(new StructurePos(4, 3));
        posList1.add(new StructurePos(3, 2));
        Assertions.assertEquals(posList1, posList);
    }

    @Test
    public void testComparePorts() {
        Method method = ReflectionUtils.findMethod(StructureMap.class, "comparePorts", boolean[][].class, boolean[][].class).get();
        boolean[][] ports0 = new boolean[][] {
                {false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false},
                {false, false, true, false, true, false, false},
                {false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false}
        };

        boolean[][] ports1 = new boolean[][] {
                {false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false},
                {false, false, false, false, true, false, true},
                {false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false}
        };

        Assertions.assertTrue((Boolean) ReflectionUtils.invokeMethod(method, structureMap, ports0, ports1));
        ports1[3][4] = false;
        Assertions.assertFalse((Boolean) ReflectionUtils.invokeMethod(method, structureMap, ports0, ports1));
    }

    @Test
    public void testAddPorts() {
        Method method = ReflectionUtils.findMethod(StructureMap.class, "addPorts", byte.class, byte.class, boolean[][].class, int.class, int.class).get();
        boolean[][] ports = new boolean[7][7];
        ReflectionUtils.invokeMethod(method, structureMap, WAY_ID, (byte) 1, ports, 0, 0);
        Assertions.assertTrue(ports[3][2]);
        Assertions.assertTrue(ports[3][4]);
        ports = new boolean[7][7];
        ReflectionUtils.invokeMethod(method, structureMap, WAY_ID, (byte) 1, ports, 2, 0);
        Assertions.assertTrue(ports[3][4]);
        Assertions.assertTrue(ports[3][6]);
        ports = new boolean[7][7];
        ReflectionUtils.invokeMethod(method, structureMap, FORK_ID, (byte) 1, ports, 0, 0);
        Assertions.assertTrue(ports[3][2]);
        Assertions.assertTrue(ports[3][4]);
        Assertions.assertTrue(ports[2][3]);
    }

    @Test
    public void testEquals() {
        Assertions.assertFalse(structureMap.equals(new Object()));
        Assertions.assertTrue(structureMap.equals(structureMap));
        Assertions.assertFalse(structureMap.equals(new StructureMap(new byte[17][17], new byte[17][17])));
    }
}
