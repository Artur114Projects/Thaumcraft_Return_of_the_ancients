package com.artur.returnoftheancients.ancientworldgeneration.genmap;

import com.artur.returnoftheancients.ancientworldgeneration.util.StructureMap;
import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.utils.interfaces.IALGS;

import java.util.Random;

import static com.artur.returnoftheancients.generation.generators.AncientLabyrinthGeneratorHandler.getVoidStructures;

public class AncientEntryMap implements IALGS {

    private static byte[][] ANCIENT_LABYRINTH_STRUCTURES = new byte[17][17];
    private static byte[][] ANCIENT_LABYRINTH_STRUCTURES_ROTATE = new byte[17][17];
    private static byte[][] ANCIENT_LABYRINTH_STRUCTURES_IN_WORK = new byte[17][17];
    private static byte[][] ANCIENT_LABYRINTH_STRUCTURES_ROTATE_IN_WORK = new byte[17][17];
    private static Random random = null;
    private static final byte f_index = 0;
    private static final byte b_index = 1;
    private static final byte bl_index = 2;
    private static final byte u_index = 3;
    private static final int SIZE = 17;

    public static int genRandomIntRange(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }

    protected static byte[] setWay(byte y, byte x, int wayRotate, int index, boolean back) {
        if (index == u_index && back) {
            index = bl_index;
        } else if (index == bl_index && back) {
            index = u_index;
        } else if (index == b_index && back) {
            index = f_index;
        } else if (index == f_index && back) {
            index = b_index;
        }

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

        byte f_x = (byte) (x + 1);
        byte b_x = (byte) (x - 1);
        byte bl_y = (byte) (y + 1);
        byte u_y = (byte) (y - 1);

        boolean is_f = x < SIZE - 1;
        boolean is_b = x > 0;
        boolean is_bl = y < SIZE - 1;
        boolean is_u = y > 0;

//        System.out.println("Way gen start IYX " + index + " " + y + " " + x);
        if (wayRotate > 2) {
            return new byte[2];
        }

        if (ANCIENT_LABYRINTH_STRUCTURES_IN_WORK[y][x] != 0) {
            return new byte[] {ANCIENT_LABYRINTH_STRUCTURES_IN_WORK[y][x], ANCIENT_LABYRINTH_STRUCTURES_ROTATE_IN_WORK[y][x]};
        }

        // forwardStructure X+
        // backStructure X-
        // belowYStructure Z+
        // upYStructure Z-

        if (random.nextInt(TRAConfigs.AncientWorldSettings.AncientWorldGenerationSettings.crossroadsChance + 1) == 0) {
            if (x < SIZE - 1 && x > 0 && y < SIZE - 1 && y > 0) {
                if (wayRotate == 1) {
                    if (upYStructure == 0 && belowYStructure == 0) {
                        return new byte[] {CROSSROADS_ID, 1};
                    }
                }
                if (wayRotate == 2) {
                    if (backStructure == 0 && forwardStructure == 0) {
                        return new byte[] {CROSSROADS_ID, 1};
                    }
                }
            }
        }

        if (random.nextInt(TRAConfigs.AncientWorldSettings.AncientWorldGenerationSettings.turnChance + 1) == 0) {
            if (index == f_index) {
                if (y == 0 || y == 16) {
                    if (y == 0) {
                        if (belowYStructure == 0) {
                            return new byte[] {TURN_ID, 2};
                        }
                    } else {
                        if (upYStructure == 0) {
                            return new byte[] {TURN_ID, 1};
                        }
                    }
                } else {
                    if (random.nextInt(2) == 0) {
                        if (is_bl && belowYStructure == 0){
                            return new byte[]{TURN_ID, 2};
                        } else if (is_u && upYStructure == 0) {
                            return new byte[] {TURN_ID, 1};
                        }
                    } else {
                        if (is_u && upYStructure == 0){
                            return new byte[]{TURN_ID, 1};
                        } else if (is_bl && belowYStructure == 0) {
                            return new byte[] {TURN_ID, 2};
                        }
                    }
                }
            }
            if (index == b_index) {
                if (y == 0 || y == 16) {
                    if (y == 0) {
                        if (belowYStructure == 0) {
                            return new byte[]{TURN_ID, 4};
                        }
                    } else {
                        if (upYStructure == 0) {
                            return new byte[]{TURN_ID, 3};
                        }
                    }
                } else {
                    if (random.nextInt(2) == 0) {
                        if (is_bl && belowYStructure == 0){
                            return new byte[] {TURN_ID, 4};
                        } else if (is_u && upYStructure == 0) {
                            return new byte[] {TURN_ID, 3};
                        }
                    } else {
                        if (is_u && upYStructure == 0){
                            return new byte[]{TURN_ID, 3};
                        } else if (is_bl && belowYStructure == 0) {
                            return new byte[] {TURN_ID, 4};
                        }
                    }
                }
            }
            if (index == bl_index) {
                if (x == 0 || x == 16) {
                    if (x == 0) {
                        if (forwardStructure == 0) {
                            return new byte[] {TURN_ID, 2};
                        }
                    } else {
                        if (backStructure == 0) {
                            return new byte[] {TURN_ID, 4};
                        }
                    }
                } else {
                    if (random.nextInt(2) == 0) {
                        if (backStructure == 0){
                            return new byte[] {TURN_ID, 4};
                        } else if (forwardStructure == 0) {
                            return new byte[] {TURN_ID, 2};
                        }
                    } else {
                        if (forwardStructure == 0) {
                            return new byte[] {TURN_ID, 2};
                        } else if (backStructure == 0) {
                            return new byte[] {TURN_ID, 4};
                        }
                    }
                }
            }
            if (index == u_index) {
                if (x == 0 || x == 16) {
                    if (x == 0) {
                        if (forwardStructure == 0) {
                            return new byte[]{TURN_ID, 1};
                        }
                    } else {
                        if (backStructure == 0) {
                            return new byte[]{TURN_ID, 3};
                        }
                    }
                } else {
                    if (random.nextInt(2) == 0) {
                        if (backStructure == 0){
                            return new byte[] {TURN_ID, 3};
                        } else if (forwardStructure == 0) {
                            return new byte[] {TURN_ID, 1};
                        }
                    } else {
                        if (forwardStructure == 0) {
                            return new byte[] {TURN_ID, 1};
                        } else if (backStructure == 0) {
                            return new byte[] {TURN_ID, 3};
                        }
                    }
                }
            }
        } // TURN

        if (genRandomIntRange(0, TRAConfigs.AncientWorldSettings.AncientWorldGenerationSettings.forkChance) == -9999999) {
            if (index == f_index) {
                if (y == 0 || y == 16) {
                    if (y == 0) {
                        return new byte[] {TURN_ID, 2};
                    } else {
                        return new byte[]{TURN_ID, 1};
                    }
                }
                if (genRandomIntRange(0, 1) == 0) {
                    return new byte[] {TURN_ID, 1};
                } else {
                    return new byte[] {TURN_ID, 2};
                }
            }
            if (index == b_index) {
                if (y == 0 || y == 16) {
                    if (y == 0) {
                        return new byte[] {TURN_ID, 4};
                    } else {
                        return new byte[]{TURN_ID, 3};
                    }
                }
                if (genRandomIntRange(0, 1) == 0) {
                    return new byte[] {TURN_ID, 3};
                } else {
                    return new byte[] {TURN_ID, 4};
                }
            }
            if (index == bl_index) {
                if (x == 0 || x == 16) {
                    if (x == 0) {
                        return new byte[] {TURN_ID, 2};
                    } else {
                        return new byte[]{TURN_ID, 4};
                    }
                }
                if (genRandomIntRange(0, 1) == 0) {
                    return new byte[] {TURN_ID, 2};
                } else {
                    return new byte[] {TURN_ID, 4};
                }
            }
            if (index == u_index) {
                if (x == 0 || x == 16) {
                    if (x == 0) {
                        return new byte[] {TURN_ID, 1};
                    } else {
                        return new byte[] {TURN_ID, 3};
                    }
                }
                if (genRandomIntRange(0, 1) == 0) {
                    return new byte[] {TURN_ID, 1};
                } else {
                    return new byte[] {TURN_ID, 3};
                }
            }
        } // FORK

        if (index == f_index) {
            if (x == 0) {
                if (random.nextInt(2) == 0) {
                    if (is_bl && belowYStructure == 0){
                        return new byte[]{TURN_ID, 2};
                    } else if (is_u && upYStructure == 0) {
                        return new byte[] {TURN_ID, 1};
                    } else {
                        return new byte[] {END_ID, 4};
                    }
                } else {
                    if (is_u && upYStructure == 0){
                        return new byte[]{TURN_ID, 1};
                    } else if (is_bl && belowYStructure == 0) {
                        return new byte[] {TURN_ID, 2};
                    } else {
                        return new byte[] {END_ID, 4};
                    }
                }
            }
            if (is_b) {
                if (backStructure != 0) {
                    if (random.nextInt(2) == 0) {
                        if (random.nextInt(TRAConfigs.AncientWorldSettings.AncientWorldGenerationSettings.chanceToReplaceWayToFork + 1) != 0 && backStructure == WAY_ID) {
                            if (is_bl && belowYStructure == 0) {
                                return new byte[]{TURN_ID, 2};
                            } else if (is_u && upYStructure == 0) {
                                return new byte[]{TURN_ID, 1};
                            } else {
                                return new byte[]{END_ID, 4};
                            }
                        } else {
                            if (is_u && upYStructure == 0) {
                                return new byte[]{TURN_ID, 1};
                            } else if (is_bl && belowYStructure == 0) {
                                return new byte[]{TURN_ID, 2};
                            } else {
                                return new byte[]{END_ID, 4};
                            }
                        }
                    }
                }
            }
        }
        if (index == b_index) {
            if (x == 16) {
                if (random.nextInt(2) == 0) {
                    if (is_bl && belowYStructure == 0) {
                        return new byte[] {TURN_ID, 4};
                    } else if (is_u && upYStructure == 0) {
                        return new byte[] {TURN_ID, 3};
                    } else {
                        return new byte[] {END_ID, 1};
                    }
                } else {
                    if (is_u && upYStructure == 0) {
                        return new byte[] {TURN_ID, 3};
                    } else if (is_bl && belowYStructure == 0) {
                        return new byte[] {TURN_ID, 4};
                    } else {
                        return new byte[] {END_ID, 1};
                    }
                }
            }
            if (is_f) {
                if (forwardStructure != 0) {
                    if (random.nextInt(TRAConfigs.AncientWorldSettings.AncientWorldGenerationSettings.chanceToReplaceWayToFork + 1) != 0 && forwardStructure == WAY_ID) {
                        if (random.nextInt(2) == 0) {
                            if (is_bl && belowYStructure == 0) {
                                return new byte[]{TURN_ID, 4};
                            } else if (is_u && upYStructure == 0) {
                                return new byte[]{TURN_ID, 3};
                            } else {
                                return new byte[]{END_ID, 1};
                            }
                        } else {
                            if (is_u && upYStructure == 0) {
                                return new byte[]{TURN_ID, 3};
                            } else if (is_bl && belowYStructure == 0) {
                                return new byte[]{TURN_ID, 4};
                            } else {
                                return new byte[]{END_ID, 1};
                            }
                        }
                    }
                }
            }
        }
        if (index == bl_index) {
            if (y == 0) {
                if (random.nextInt(2) == 0) {
                    if (is_f && forwardStructure == 0) {
                        return new byte[] {TURN_ID, 2};
                    } else if (is_b && backStructure == 0) {
                        return new byte[] {TURN_ID, 4};
                    } else {
                        return new byte[] {END_ID, 3};
                    }
                } else {
                    if (is_b && backStructure == 0) {
                        return new byte[] {TURN_ID, 4};
                    } else if (is_f && forwardStructure == 0) {
                        return new byte[] {TURN_ID, 2};
                    } else {
                        return new byte[] {END_ID, 3};
                    }
                }
            }
            if (is_u) {
                if (upYStructure != 0) {
                    if (random.nextInt(TRAConfigs.AncientWorldSettings.AncientWorldGenerationSettings.chanceToReplaceWayToFork + 1) != 0 && upYStructure == WAY_ID) {
                        if (random.nextInt(2) == 0) {
                            if (is_f && forwardStructure == 0) {
                                return new byte[]{TURN_ID, 2};
                            } else if (is_b && backStructure == 0) {
                                return new byte[]{TURN_ID, 4};
                            } else {
                                return new byte[]{END_ID, 3};
                            }
                        } else {
                            if (is_b && backStructure == 0) {
                                return new byte[]{TURN_ID, 4};
                            } else if (is_f && forwardStructure == 0) {
                                return new byte[]{TURN_ID, 2};
                            } else {
                                return new byte[]{END_ID, 3};
                            }
                        }
                    }
                }
            }
        }
        if (index == u_index) {
            if (y == 16) {
                if (random.nextInt(2) == 0) {
                    if (is_f && forwardStructure == 0) {
                        return new byte[] {TURN_ID, 1};
                    } else if (is_b && backStructure == 0) {
                        return new byte[] {TURN_ID, 3};
                    } else {
                        return new byte[] {END_ID, 2};
                    }
                } else {
                    if (is_b && backStructure == 0) {
                        return new byte[] {TURN_ID, 1};
                    } else if (is_f && forwardStructure == 0) {
                        return new byte[] {TURN_ID, 3};
                    } else {
                        return new byte[] {END_ID, 2};
                    }
                }
            }
            if (is_bl) {
                if (belowYStructure != 0) {
                    if (random.nextInt(TRAConfigs.AncientWorldSettings.AncientWorldGenerationSettings.chanceToReplaceWayToFork + 1) != 0 && belowYStructure == WAY_ID) {
                        if (random.nextInt(2) == 0) {
                            if (is_f && forwardStructure == 0) {
                                return new byte[]{TURN_ID, 1};
                            } else if (is_b && backStructure == 0) {
                                return new byte[]{TURN_ID, 3};
                            } else {
                                return new byte[]{END_ID, 2};
                            }
                        } else {
                            if (is_b && backStructure == 0) {
                                return new byte[]{TURN_ID, 3};
                            } else if (is_f && forwardStructure == 0) {
                                return new byte[]{TURN_ID, 1};
                            } else {
                                return new byte[]{END_ID, 2};
                            }
                        }
                    }
                }
            }
        }

        return new byte[] {WAY_ID, (byte) wayRotate};
    }

    protected static byte[][][] genRandomStructures() {
        byte[][] ANCIENT_LABYRINTH_STRUCTURES = new byte[17][17];
        byte[][] ANCIENT_LABYRINTH_STRUCTURES_ROTATE = new byte[17][17];
        for (byte y = 0; y != SIZE; y++) {
            for (byte x = 0; x != SIZE; x++) {
                ANCIENT_LABYRINTH_STRUCTURES[y][x] = 0;
                ANCIENT_LABYRINTH_STRUCTURES_ROTATE[y][x] = 0;
            }
        }

        ANCIENT_LABYRINTH_STRUCTURES[8][8] = ENTRY_ID;
        ANCIENT_LABYRINTH_STRUCTURES_ROTATE[8][8] = ROTATE_MAX[ENTRY_ID - 1];

//        ANCIENT_LABYRINTH_STRUCTURES[8][8] = FORK_ID;
//        ANCIENT_LABYRINTH_STRUCTURES_ROTATE[8][8] = (byte) genRandomIntRange(1, 4);

        boolean foundRandom = false;
        byte x = 0;
        byte y = 0;
        while (!foundRandom) {
            x = (byte) random.nextInt(17);
            y = (byte) random.nextInt(17);
            if (x + 1 <= SIZE - 1 && y + 1 <= SIZE - 1) {
                foundRandom =
                        ANCIENT_LABYRINTH_STRUCTURES[y][x] == 0 &&
                                ANCIENT_LABYRINTH_STRUCTURES[y][x + 1] == 0 &&
                                ANCIENT_LABYRINTH_STRUCTURES[y + 1][x] == 0 &&
                                ANCIENT_LABYRINTH_STRUCTURES[y + 1][x + 1] == 0 &&
                                (x >= 12 || x <= 4) && (y >= 12 || y <= 4) && (y < 14 && y > 2) && (x < 14 && x > 2);
            }
        }
        byte[] ns = new byte[] {BOSS_N_ID, BOSS_N_ID, BOSS_N_ID, BOSS_N_ID};

        ns[random.nextInt(4)] = BOSS_ID;

        ANCIENT_LABYRINTH_STRUCTURES[y][x] = ns[0];
        ANCIENT_LABYRINTH_STRUCTURES[y][x + 1] = ns[1];
        ANCIENT_LABYRINTH_STRUCTURES[y + 1][x] = ns[2];
        ANCIENT_LABYRINTH_STRUCTURES[y + 1][x + 1] = ns[3];

        ANCIENT_LABYRINTH_STRUCTURES_ROTATE[y][x] = 1;
        ANCIENT_LABYRINTH_STRUCTURES_ROTATE[y][x + 1] = 1;
        ANCIENT_LABYRINTH_STRUCTURES_ROTATE[y + 1][x] = 1;
        ANCIENT_LABYRINTH_STRUCTURES_ROTATE[y + 1][x + 1] = 1;

        return new byte[][][] {ANCIENT_LABYRINTH_STRUCTURES, ANCIENT_LABYRINTH_STRUCTURES_ROTATE};
    } // Gen Random Structures


    /*
    [[0][1][2][3][4][5][6][7][8]] 0
    [[0][1][2][3][4][5][6][7][8]] 1
    [[0][1][2][3][4][5][6][7][8]] 2
    [[0][1][2][3][u][5][6][7][8]] 3
    [[0][1][2][b][s][f][6][7][8]] 4
    [[0][1][2][3][bl][5][6][7][8]] 5
    [[0][1][2][3][4][5][6][7][8]] 6
    [[0][1][2][3][4][5][6][7][8]] 7
    [[0][1][2][3][4][5][6][7][8]] 8
     */


    public static StructureMap genStructuresMap() {
        random = new Random();
        int void0 = 0;
        int void1 = 0;
        byte exit = 0;
        byte e = 0;

        byte[][][] a = genRandomStructures();
        ANCIENT_LABYRINTH_STRUCTURES = a[0];
        ANCIENT_LABYRINTH_STRUCTURES_ROTATE = a[1];

        ANCIENT_LABYRINTH_STRUCTURES_IN_WORK = ANCIENT_LABYRINTH_STRUCTURES;
        ANCIENT_LABYRINTH_STRUCTURES_ROTATE_IN_WORK = ANCIENT_LABYRINTH_STRUCTURES_ROTATE;

        while (true) {
            exit++;
            ANCIENT_LABYRINTH_STRUCTURES = ANCIENT_LABYRINTH_STRUCTURES_IN_WORK;
            ANCIENT_LABYRINTH_STRUCTURES_ROTATE = ANCIENT_LABYRINTH_STRUCTURES_ROTATE_IN_WORK;

            void0 = getVoidStructures(ANCIENT_LABYRINTH_STRUCTURES);
            if (void0 == void1) {
                e++;
            }
            if (e >= 4) {
                System.out.println("is took " + exit + " passes to generate");
                for (byte y = 0; y != SIZE; y++) {
                    for (byte x = 0; x != SIZE; x++) {
                        if (ANCIENT_LABYRINTH_STRUCTURES[y][x] == BOSS_N_ID) {
                            ANCIENT_LABYRINTH_STRUCTURES[y][x] = BOSS_ID;
                        }
                    }
                }
                if (void0 < 145 || !TRAConfigs.AncientWorldSettings.AncientWorldGenerationSettings.isNeedMoreThan50Fill) {
                    System.out.println("void structures: " + void0);
                    return new StructureMap(ANCIENT_LABYRINTH_STRUCTURES, ANCIENT_LABYRINTH_STRUCTURES_ROTATE);
                } else {
                    System.out.println("void structures: " + void0);
                    System.out.println("regenerate");
                    return genStructuresMap();
                }
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

                    byte f_x = (byte) (x + 1);
                    byte b_x = (byte) (x - 1);
                    byte bl_y = (byte) (y + 1);
                    byte u_y = (byte) (y - 1);

                    boolean is_f = x < SIZE - 1;
                    boolean is_b = x > 0;
                    boolean is_bl = y < SIZE - 1;
                    boolean is_u = y > 0;

                    // gen
                    if (structure == CROSSROADS_ID || structure == ENTRY_ID || structure == BOSS_ID) {
                        if (forwardStructure == 0) {
                            setForwardWay(y, f_x, is_f, true);
                        } else {
                            setBackStructure(y, f_x, forwardStructure, forwardStructureRotate);
                        }

                        if (backStructure == 0) {
                            setBackWay(y, b_x, is_b, true);
                        } else {
                            setForwardStructure(y, b_x, backStructure, backStructureRotate);
                        }

                        if (upYStructure == 0) {
                            setUpWay(u_y, x, is_u, true);
                        } else {
                            setBelowStructure(u_y, x, upYStructure, upYStructureRotate);
                        }

                        if (belowYStructure == 0) {
                            setBelowWay(bl_y, x, is_bl, true);
                        } else {
                            setUpStructure(bl_y, x, belowYStructure, belowYStructureRotate);
                        }
                    }

                    if (upYStructure == WAY_ID && upYStructureRotate == 2) {
                        if (structure == 0) {
                            setUpWay(y, x, is_u, false);
                        } else {
                            setUpStructure(y, x, structure, structureRotate);
                        }
                    }

                    if (belowYStructure == WAY_ID && belowYStructureRotate == 2) {
                        if (structure == 0) {
                            setBelowWay(y, x, is_bl, false);
                        } else {
                            setBelowStructure(y, x, structure, structureRotate);
                        }
                    }

                    if (backStructure == WAY_ID && backStructureRotate == 1) {
                        if (structure == 0) {
                            setBackWay(y, x, is_b, false);
                        } else {
                            setBackStructure(y, x, structure, structureRotate);
                        }
                    }

                    if (forwardStructure == WAY_ID && forwardStructureRotate == 1) {
                        if (structure == 0) {
                            setForwardWay(y, x, is_f, false);
                        } else {
                            setForwardStructure(y, x, structure, structureRotate);
                        }
                    }

                    // forwardStructure X+
                    // backStructure X-
                    // belowYStructure Z+
                    // upYStructure Z-

                    if (structure == TURN_ID) {
                        if (structureRotate == 1) {
                            if (forwardStructure == 0) {
                                setForwardWay(y, f_x, is_f, true);
                            } else {
                                setBackStructure(y, f_x, forwardStructure, forwardStructureRotate);
                            }

                            if (upYStructure == 0) {
                                setUpWay(u_y, x, is_u, true);
                            } else {
                                setBelowStructure(u_y, x, upYStructure, upYStructureRotate);
                            }
                        }
                        if (structureRotate == 2) {
                            if (forwardStructure == 0) {
                                setForwardWay(y, f_x, is_f, true);
                            } else {
                                setBackStructure(y, f_x, forwardStructure, forwardStructureRotate);
                            }

                            if (belowYStructure == 0) {
                                setBelowWay(bl_y, x, is_bl, true);
                            } else {
                                setUpStructure(bl_y, x, belowYStructure, belowYStructureRotate);
                            }
                        }
                        if (structureRotate == 3) {
                            if (backStructure == 0) {
                                setBackWay(y, b_x, is_b, true);
                            } else {
                                setForwardStructure(y, b_x, backStructure, backStructureRotate);
                            }

                            if (upYStructure == 0) {
                                setUpWay(u_y, x, is_u, true);
                            } else {
                                setBelowStructure(u_y, x, upYStructure, upYStructureRotate);
                            }
                        }
                        if (structureRotate == 4) {
                            if (backStructure == 0) {
                                setBackWay(y, b_x, is_b, true);
                            } else {
                                setForwardStructure(y, b_x, backStructure, backStructureRotate);
                            }

                            if (belowYStructure == 0) {
                                setBelowWay(bl_y, x, is_bl, true);
                            } else {
                                setUpStructure(bl_y, x, belowYStructure, belowYStructureRotate);
                            }
                        }
                    }

                    // forwardStructure X+
                    // backStructure X-
                    // belowYStructure Z+
                    // upYStructure Z-

                    if (structure == FORK_ID) {
                        if (structureRotate == 1) {
                            if (forwardStructure == 0) {
                                setForwardWay(y, f_x, is_f, true);
                            } else {
                                setBackStructure(y, f_x, forwardStructure, forwardStructureRotate);
                            }

                            if (backStructure == 0) {
                                setBackWay(y, b_x, is_b, true);
                            } else {
                                setForwardStructure(y, b_x, backStructure, backStructureRotate);
                            }

                            if (upYStructure == 0) {
                                setUpWay(u_y, x, is_u, true);
                            } else {
                                setBelowStructure(u_y, x, upYStructure, upYStructureRotate);
                            }
                        }
                        if (structureRotate == 2) {
                            if (belowYStructure == 0) {
                                setBelowWay(bl_y, x, is_bl, true);
                            } else {
                                setUpStructure(bl_y, x, belowYStructure, belowYStructureRotate);
                            }

                            if (upYStructure == 0) {
                                setUpWay(u_y, x, is_u, true);
                            } else {
                                setBelowStructure(u_y, x, upYStructure, upYStructureRotate);
                            }

                            if (forwardStructure == 0) {
                                setForwardWay(y, f_x, is_f, true);
                            } else {
                                setBackStructure(y, f_x, forwardStructure, forwardStructureRotate);
                            }
                        }
                        if (structureRotate == 3) {
                            if (upYStructure == 0) {
                                setUpWay(u_y, x, is_u, true);
                            } else {
                                setBelowStructure(u_y, x, upYStructure, upYStructureRotate);
                            }

                            if (belowYStructure == 0) {
                                setBelowWay(bl_y, x, is_bl, true);
                            } else {
                                setUpStructure(bl_y, x, belowYStructure, belowYStructureRotate);
                            }

                            if (backStructure == 0) {
                                setBackWay(y, b_x, is_b, true);
                            } else {
                                setForwardStructure(y, b_x, backStructure, backStructureRotate);
                            }
                        }
                        if (structureRotate == 4) {
                            if (forwardStructure == 0) {
                                setForwardWay(y, f_x, is_f, true);
                            } else {
                                setBackStructure(y, f_x, forwardStructure, forwardStructureRotate);
                            }

                            if (backStructure == 0) {
                                setBackWay(y, b_x, is_b, true);
                            } else {
                                setForwardStructure(y, b_x, backStructure, backStructureRotate);
                            }

                            if (belowYStructure == 0) {
                                setBelowWay(bl_y, x, is_bl, true);
                            } else {
                                setUpStructure(bl_y, x, belowYStructure, belowYStructureRotate);
                            }
                        }
                    }

                }
            }
        }
    }

    protected static void setForwardStructure(byte y, byte x, byte structure, byte structureRotate) {
        if (structure == WAY_ID && structureRotate == 2) {
            setFork(y, x, 2);
        } else if (structure == TURN_ID) {
            if (structureRotate == 3) {
                setFork(y, x, 1);
            }
            if (structureRotate == 4) {
                setFork(y, x, 4);
            }
        } else if (structure == FORK_ID && structureRotate == 3) {
            setCrossroads(y, x);
        }
    }

    protected static void setBackStructure(byte y, byte x, byte structure, byte structureRotate) {
        if (structure == WAY_ID && structureRotate == 2) {
            setFork(y, x, 3);
        } else if (structure == TURN_ID) {
            if (structureRotate == 1) {
                setFork(y, x, 1);
            }
            if (structureRotate == 2) {
                setFork(y, x, 4);
            }
        } else if (structure == FORK_ID && structureRotate == 2) {
            setCrossroads(y, x);
        }
    }

    protected static void setUpStructure(byte y, byte x, byte structure, byte structureRotate) {
        if (structure == WAY_ID && structureRotate == 1) {
            setFork(y, x, 1);
        } else if (structure == TURN_ID) {
            if (structureRotate == 2) {
                setFork(y, x, 2);
            }
            if (structureRotate == 4) {
                setFork(y, x, 3);
            }
        } else if (structure == FORK_ID && structureRotate == 4) {
            setCrossroads(y, x);
        }
    }

    protected static void setBelowStructure(byte y, byte x, byte structure, byte structureRotate) {
        if (structure == WAY_ID && structureRotate == 1) {
            setFork(y, x, 4);
        } else if (structure == TURN_ID) {
            if (structureRotate == 1) {
                setFork(y, x, 2);
            }
            if (structureRotate == 3) {
                setFork(y, x, 3);
            }
        } else if (structure == FORK_ID && structureRotate == 1) {
            setCrossroads(y, x);
        }
    }

    protected static void setBelowWay(byte y, byte x, boolean is, boolean back) {
        if (is) {
            byte[] m = setWay(y, x, 2, bl_index, back);
            ANCIENT_LABYRINTH_STRUCTURES_IN_WORK[y][x] = m[0];
            ANCIENT_LABYRINTH_STRUCTURES_ROTATE_IN_WORK[y][x] = m[1];
        }
    }

    protected static void setBackWay(byte y, byte x, boolean is, boolean back) {
        if (is) {
            byte[] m = setWay(y, x, 1, b_index, back);
            ANCIENT_LABYRINTH_STRUCTURES_IN_WORK[y][x] = m[0];
            ANCIENT_LABYRINTH_STRUCTURES_ROTATE_IN_WORK[y][x] = m[1];
        }
    }

    protected static void setUpWay(byte y, byte x, boolean is, boolean back) {
        if (is) {
            byte[] m = setWay(y, x, 2, u_index, back);
            ANCIENT_LABYRINTH_STRUCTURES_IN_WORK[y][x] = m[0];
            ANCIENT_LABYRINTH_STRUCTURES_ROTATE_IN_WORK[y][x] = m[1];
        }
    }

    protected static void setForwardWay(byte y, byte x, boolean is, boolean back) {
        if (is) {
            byte[] m = setWay(y, x, 1, f_index, back);
            ANCIENT_LABYRINTH_STRUCTURES_IN_WORK[y][x] = m[0];
            ANCIENT_LABYRINTH_STRUCTURES_ROTATE_IN_WORK[y][x] = m[1];
        }
    }

    protected static void setFork(byte y, byte x, int rotate) {
        ANCIENT_LABYRINTH_STRUCTURES_IN_WORK[y][x] = FORK_ID;
        ANCIENT_LABYRINTH_STRUCTURES_ROTATE_IN_WORK[y][x] = (byte) rotate;
    }

    protected static void setCrossroads(byte y, byte x) {
        ANCIENT_LABYRINTH_STRUCTURES_IN_WORK[y][x] = CROSSROADS_ID;
        ANCIENT_LABYRINTH_STRUCTURES_ROTATE_IN_WORK[y][x] = (byte) 1;
    }
}
