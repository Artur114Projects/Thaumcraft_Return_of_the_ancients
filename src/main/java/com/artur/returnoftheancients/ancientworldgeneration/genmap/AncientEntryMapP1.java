package com.artur.returnoftheancients.ancientworldgeneration.genmap;

import com.artur.returnoftheancients.ancientworldgeneration.genmap.util.StructureMap;
import com.artur.returnoftheancients.ancientworldgeneration.genmap.util.StructurePos;
import com.artur.returnoftheancients.utils.interfaces.IALGS;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Random;

public class AncientEntryMapP1 implements IALGS {
    public static StructureMap createAncientEntryMap(Random rand) {
        StructureMap structureMap = AncientEntryMapP0.genStructuresMap(rand);
        if (!checkWayToEntry(structureMap)) return createAncientEntryMap(rand);
        foundAndAddDeformation(structureMap);
        removeBossN(structureMap);
        return structureMap;
    }

    private static void removeBossN(StructureMap map) {
        for (byte y = 0; y != map.SIZE; y++) {
            for (byte x = 0; x != map.SIZE; x++) {
                if (map.getStructure(x, y) == BOSS_N_ID) {
                    map.setStructure(x, y, BOSS_ID);
                }
            }
        }
        map.swapBuffers();
    }

    private static void foundAndAddDeformation(StructureMap map) {
        for (byte y = 0; y != map.SIZE; y++) {
            for (byte x = 0; x != map.SIZE; x++) {
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

    private static boolean checkWayToEntry(StructureMap map) {
        boolean[] checked = new boolean[map.SIZE * map.SIZE];
        ArrayDeque<StructurePos> queue = new ArrayDeque<>(40);
        queue.addLast(foundBoss(map));

        while (!queue.isEmpty()) {
            StructurePos currentPos = queue.poll();
            if (map.getStructure(currentPos) == IALGS.ENTRY_ID) return true;
            checked[currentPos.x + currentPos.y * map.SIZE] = true;
            for (StructurePos connected : map.getConnectedStructures(currentPos)) {
                if (map.getStructure(connected) == IALGS.ENTRY_ID) return true;
                if (checked[connected.x + connected.y * map.SIZE]) continue;
                byte structure = map.getStructure(connected);
                if (structure != BOSS_ID && structure != BOSS_N_ID) {
                    queue.addLast(connected);
                }
            }
        }
        return false;
    }

    private static StructurePos foundBoss(StructureMap map) {
        for (byte y = 0; y != map.SIZE; y++) {
            for (byte x = 0; x != map.SIZE; x++) {
                if (map.getStructure(x, y) == BOSS_ID) {
                    return new StructurePos(x, y);
                }
            }
        }
        return new StructurePos(0, 0);
    }
}
