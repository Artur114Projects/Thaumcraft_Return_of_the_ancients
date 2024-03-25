package com.artur.returnoftheancients.generation.generators;

import com.artur.returnoftheancients.utils.interfaces.IALGS;

import static com.artur.returnoftheancients.handlers.Handler.genRandomIntRange;

public class AncientLabyrinthOldMap extends AncientLabyrinthGeneratorHandler implements IALGS {
    private static byte[][] ANCIENT_LABYRINTH_STRUCTURES = new byte[17][17];
    private static byte[][] ANCIENT_LABYRINTH_STRUCTURES_ROTATE = new byte[17][17];
    private static final byte SIZE = (byte) ANCIENT_LABYRINTH_STRUCTURES.length;
    protected static void genRandomStructures() {
        for (byte y = 0; y != SIZE; y++) {
            for (byte x = 0; x != SIZE; x++) {
                ANCIENT_LABYRINTH_STRUCTURES[y][x] = 0;
                ANCIENT_LABYRINTH_STRUCTURES_ROTATE[y][x] = 0;
            }
        }
        ANCIENT_LABYRINTH_STRUCTURES[8][8] = ENTRY_ID;
        ANCIENT_LABYRINTH_STRUCTURES_ROTATE[8][8] = ROTATE_MAX[ENTRY_ID - 1];
        for (byte q = 0; q != 8; q++) {
            boolean foundRandom = false;
            byte x = 0;
            byte y = 0;
            while (!foundRandom) {
                x = (byte) genRandomIntRange(0, 16);
                y = (byte) genRandomIntRange(0, 16);
                foundRandom = ANCIENT_LABYRINTH_STRUCTURES[y][x] == 0;
            }
            ANCIENT_LABYRINTH_STRUCTURES[y][x] = CROSSROADS_ID;
            ANCIENT_LABYRINTH_STRUCTURES_ROTATE[y][x] = ROTATE_MAX[CROSSROADS_ID - 1];
        }
        boolean foundRandom = false;
        byte x = 0;
        byte y = 0;
        while (!foundRandom) {
            x = (byte) genRandomIntRange(0, 16);
            y = (byte) genRandomIntRange(0, 16);
            if (x + 1 <= SIZE - 1 && y + 1 <= SIZE - 1) {
                foundRandom =
                        ANCIENT_LABYRINTH_STRUCTURES[y][x] == 0 &&
                                ANCIENT_LABYRINTH_STRUCTURES[y][x + 1] == 0 &&
                                ANCIENT_LABYRINTH_STRUCTURES[y + 1][x] == 0 &&
                                ANCIENT_LABYRINTH_STRUCTURES[y + 1][x + 1] == 0 &&
                                x >= 12 || x <= 4 &&  y >= 12 || y <= 4;
            }
        }
        ANCIENT_LABYRINTH_STRUCTURES[y][x] = BOSS_ID;
        ANCIENT_LABYRINTH_STRUCTURES[y][x + 1] = BOSS_ID;
        ANCIENT_LABYRINTH_STRUCTURES[y + 1][x] = BOSS_ID;
        ANCIENT_LABYRINTH_STRUCTURES[y + 1][x + 1] = BOSS_ID;
    } // Gen Random Structures

    protected static byte[][][] genStructuresMap() {
        int void0 = 0;
        int void1 = 0;
        byte exit = 0;

        genRandomStructures();
        byte[][] ANCIENT_LABYRINTH_STRUCTURES_IN_WORK = ANCIENT_LABYRINTH_STRUCTURES;
        byte[][] ANCIENT_LABYRINTH_STRUCTURES_ROTATE_IN_WORK = ANCIENT_LABYRINTH_STRUCTURES_ROTATE;

        while (true) {
            ANCIENT_LABYRINTH_STRUCTURES = ANCIENT_LABYRINTH_STRUCTURES_IN_WORK;
            ANCIENT_LABYRINTH_STRUCTURES_ROTATE = ANCIENT_LABYRINTH_STRUCTURES_ROTATE_IN_WORK;

            void0 = getVoidStructures(ANCIENT_LABYRINTH_STRUCTURES);
            if (void0 == void1) {
                exit++;
            } else {
                exit = 0;
            }
            if (exit >= 4) {
                return new byte[][][] {ANCIENT_LABYRINTH_STRUCTURES, ANCIENT_LABYRINTH_STRUCTURES_ROTATE};
            }

            void1 = getVoidStructures(ANCIENT_LABYRINTH_STRUCTURES);

            for (byte y = 0; y != SIZE; y++) {
                for (byte x = 0; x != SIZE; x++) {
                    // get structures
                    byte structure = ANCIENT_LABYRINTH_STRUCTURES[y][x];
                    byte structureRotate = ANCIENT_LABYRINTH_STRUCTURES_ROTATE[y][x];

                    byte upYStructure = 0;
                    byte upYStructureRotate = 0;
                    if (y > 0) {
                        upYStructure = ANCIENT_LABYRINTH_STRUCTURES[y - 1][x];
                        upYStructureRotate = ANCIENT_LABYRINTH_STRUCTURES_ROTATE[y - 1][x];
                    }

                    byte belowYStructure = 0;
                    byte belowYStructureRotate = 0;
                    if (y < SIZE - 1) {
                        belowYStructure = ANCIENT_LABYRINTH_STRUCTURES[y + 1][x];
                        belowYStructureRotate = ANCIENT_LABYRINTH_STRUCTURES_ROTATE[y + 1][x];
                    }

                    byte backStructure = 0;
                    byte backStructureRotate = 0;
                    if (x > 0) {
                        backStructure = ANCIENT_LABYRINTH_STRUCTURES[y][x - 1];
                        backStructureRotate = ANCIENT_LABYRINTH_STRUCTURES_ROTATE[y][x - 1];
                    }

                    byte forwardStructure = 0;
                    byte forwardStructureRotate = 0;
                    if (x < SIZE - 1) {
                        forwardStructure = ANCIENT_LABYRINTH_STRUCTURES[y][x + 1];
                        forwardStructureRotate = ANCIENT_LABYRINTH_STRUCTURES_ROTATE[y][x + 1];
                    }

                    // gen
                    if (structure == CROSSROADS_ID || structure == ENTRY_ID || structure == BOSS_ID) {
                        if (x < SIZE - 1) {
                            if (forwardStructure == 0) {
                                ANCIENT_LABYRINTH_STRUCTURES_IN_WORK[y][x + 1] = WAY_ID;
                                ANCIENT_LABYRINTH_STRUCTURES_ROTATE_IN_WORK[y][x + 1] = 1;
                            } else if (forwardStructure == WAY_ID && forwardStructureRotate == 2) {
                                ANCIENT_LABYRINTH_STRUCTURES_IN_WORK[y][x + 1] = CROSSROADS_ID;
                                ANCIENT_LABYRINTH_STRUCTURES_ROTATE_IN_WORK[y][x + 1] = 1;
                            }
                        }

                        if (x > 0) {
                            if (backStructure == 0) {
                                ANCIENT_LABYRINTH_STRUCTURES_IN_WORK[y][x - 1] = WAY_ID;
                                ANCIENT_LABYRINTH_STRUCTURES_ROTATE_IN_WORK[y][x - 1] = 1;
                            } else if (backStructure == WAY_ID && backStructureRotate == 2) {
                                ANCIENT_LABYRINTH_STRUCTURES_IN_WORK[y][x - 1] = CROSSROADS_ID;
                                ANCIENT_LABYRINTH_STRUCTURES_ROTATE_IN_WORK[y][x - 1] = 1;
                            }
                        }

                        if (y > 0) {
                            if (upYStructure == 0) {
                                ANCIENT_LABYRINTH_STRUCTURES_IN_WORK[y - 1][x] = WAY_ID;
                                ANCIENT_LABYRINTH_STRUCTURES_ROTATE_IN_WORK[y - 1][x] = 2;
                            } else if (upYStructure == WAY_ID && upYStructureRotate == 1) {
                                ANCIENT_LABYRINTH_STRUCTURES_IN_WORK[y - 1][x] = CROSSROADS_ID;
                                ANCIENT_LABYRINTH_STRUCTURES_ROTATE_IN_WORK[y - 1][x] = 1;
                            }
                        }

                        if (y < SIZE - 1) {
                            if (belowYStructure == 0) {
                                ANCIENT_LABYRINTH_STRUCTURES_IN_WORK[y + 1][x] = WAY_ID;
                                ANCIENT_LABYRINTH_STRUCTURES_ROTATE_IN_WORK[y + 1][x] = 2;
                            } else if (belowYStructure == WAY_ID && belowYStructureRotate == 1) {
                                ANCIENT_LABYRINTH_STRUCTURES_IN_WORK[y + 1][x] = CROSSROADS_ID;
                                ANCIENT_LABYRINTH_STRUCTURES_ROTATE_IN_WORK[y + 1][x] = 1;
                            }
                        }
                    }

                    if (upYStructure == WAY_ID && upYStructureRotate == 2) {
                        if (structure == 0) {
                            ANCIENT_LABYRINTH_STRUCTURES_IN_WORK[y][x] = WAY_ID;
                            ANCIENT_LABYRINTH_STRUCTURES_ROTATE_IN_WORK[y][x] = 2;
                        } else if (structure == WAY_ID && structureRotate == 1) {
                            ANCIENT_LABYRINTH_STRUCTURES_IN_WORK[y][x] = CROSSROADS_ID;
                            ANCIENT_LABYRINTH_STRUCTURES_ROTATE_IN_WORK[y][x] = 1;
                        }
                    }

                    if (belowYStructure == WAY_ID && belowYStructureRotate == 2) {
                        if (structure == 0) {
                            ANCIENT_LABYRINTH_STRUCTURES_IN_WORK[y][x] = WAY_ID;
                            ANCIENT_LABYRINTH_STRUCTURES_ROTATE_IN_WORK[y][x] = 2;
                        } else if (structure == WAY_ID && structureRotate == 1) {
                            ANCIENT_LABYRINTH_STRUCTURES_IN_WORK[y][x] = CROSSROADS_ID;
                            ANCIENT_LABYRINTH_STRUCTURES_ROTATE_IN_WORK[y][x] = 1;
                        }
                    }

                    if (backStructure == WAY_ID && backStructureRotate == 1) {
                        if (structure == 0) {
                            ANCIENT_LABYRINTH_STRUCTURES_IN_WORK[y][x] = WAY_ID;
                            ANCIENT_LABYRINTH_STRUCTURES_ROTATE_IN_WORK[y][x] = 1;
                        } else if (structure == WAY_ID && structureRotate == 2) {
                            ANCIENT_LABYRINTH_STRUCTURES_IN_WORK[y][x] = CROSSROADS_ID;
                            ANCIENT_LABYRINTH_STRUCTURES_ROTATE_IN_WORK[y][x] = 1;
                        }
                    }

                    if (forwardStructure == WAY_ID && forwardStructureRotate == 1) {
                        if (structure == 0) {
                            ANCIENT_LABYRINTH_STRUCTURES_IN_WORK[y][x] = WAY_ID;
                            ANCIENT_LABYRINTH_STRUCTURES_ROTATE_IN_WORK[y][x] = 1;
                        } else if (structure == WAY_ID && structureRotate == 2) {
                            ANCIENT_LABYRINTH_STRUCTURES_IN_WORK[y][x] = CROSSROADS_ID;
                            ANCIENT_LABYRINTH_STRUCTURES_ROTATE_IN_WORK[y][x] = 1;
                        }
                    }
                }
            }

        }
    }
}
