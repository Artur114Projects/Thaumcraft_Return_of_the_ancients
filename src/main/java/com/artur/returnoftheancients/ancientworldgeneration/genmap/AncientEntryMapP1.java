package com.artur.returnoftheancients.ancientworldgeneration.genmap;

import com.artur.returnoftheancients.ancientworldgeneration.genmap.util.Structure;
import com.artur.returnoftheancients.ancientworldgeneration.genmap.util.StructureMap;
import com.artur.returnoftheancients.ancientworldgeneration.genmap.util.StructurePos;
import com.artur.returnoftheancients.utils.interfaces.IALGS;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class AncientEntryMapP1 implements IALGS {
    private static boolean isFoundWay = false;
    private static boolean[][] foundArray = new boolean[17][17];
    private static final int SIZE = 17;
    public static StructureMap createAncientEntryMap(Random rand) {
        StructureMap structureMap = AncientEntryMapP0.genStructuresMap(rand);
        foundAndAddDeformation(structureMap);
        if (!foundAndCheckWeyToEntry(structureMap)) return createAncientEntryMap(rand);
        removeBossN(structureMap);
        return structureMap;
    }

    private static void removeBossN(StructureMap map) {
        for (byte y = 0; y != SIZE; y++) {
            for (byte x = 0; x != SIZE; x++) {
                if (map.getStructure(x, y) == BOSS_N_ID) {
                    map.setStructure(x, y, BOSS_ID);
                }
            }
        }
        map.swapBuffers();
    }

    private static void foundAndAddDeformation(StructureMap map) {
        for (byte y = 0; y != SIZE; y++) {
            for (byte x = 0; x != SIZE; x++) {
                if (map.getStructure(x, y) == BOSS_ID) {
                    for (StructurePos pos : map.getConnectedStructures(x, y)) {
                        if (map.getStructure(pos.x, pos.y) != BOSS_ID) {
                            addBossDeformation(map, (byte) 14, pos.x, pos.y);
                        }
                    }
                    break;
                }
            }
        }
    }

    private static boolean foundAndCheckWeyToEntry(StructureMap map) {
        foundArray = new boolean[17][17];
        for (byte y = 0; y != SIZE; y++) {
            for (byte x = 0; x != SIZE; x++) {
                if (map.getStructure(x, y) == BOSS_ID) {
                    for (StructurePos pos : map.getConnectedStructures(x, y)) {
                        if (map.getStructure(pos.x, pos.y) != BOSS_ID) {
                            checkWeyToEntry(map, pos.x, pos.y);
                        }
                    }
                    break;
                }
            }
        }
        return isFoundWay;
    }

    private static void checkWeyToEntry(StructureMap map, int x, int y) {
        if (map.getStructure(x, y) == BOSS_N_ID) return;
        if (map.getStructure(x, y) == ENTRY_ID) {
            isFoundWay = true;
            return;
        }
        if (foundArray[y][x]) {
            return;
        } else {
            foundArray[y][x] = true;
        }

        if (isFoundWay) return;

        List<StructurePos> posList = map.getConnectedStructures(x, y);
        for (StructurePos pos : posList) {
            byte structure = map.getStructure(pos.x, pos.y);
            if (structure != BOSS_ID && structure != BOSS_N_ID) {
                if (map.getStructure(pos.x, pos.y) == ENTRY_ID) {
                    isFoundWay = true;
                    return;
                }
                checkWeyToEntry(map, pos.x, pos.y);
            }
        }
    }

    private static void addBossDeformation(StructureMap map, byte value, int x, int y) {
        if (map.getStructure(x, y) == ENTRY_ID) return;
        if (map.getStructure(x, y) == BOSS_N_ID) return;

        map.setDeformation(x, y, value);
        map.swapBuffers();

        List<StructurePos> posList = map.getConnectedStructures(x, y);
        for (StructurePos pos : posList) {
            byte deformation = map.getDeformation(pos.x, pos.y);
            byte structure = map.getStructure(pos.x, pos.y);
            if (structure != BOSS_ID && structure != BOSS_N_ID) {
                if (deformation < value - 1 && value - 1 > 0) {
                    addBossDeformation(map, (byte) (value - 1), pos.x, pos.y);
                }
            }
        }
    }

}
