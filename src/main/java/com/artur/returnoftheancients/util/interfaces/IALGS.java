package com.artur.returnoftheancients.util.interfaces;

public interface IALGS {

    byte WAY_ID = 1;
    byte WAY_ROTATE_MAX = 2;
    byte[] WAY_MIXING = new byte[] {};
    String WAY_STRING_ID = "ancient_way_rotate-";

    byte TURN_ID = 2; // поворот
    byte TURN_CHANCE = 2;
    byte TURN_ROTATE_MAX = 4;
    byte[] TURN_MIXING = new byte[] {};
    String TURN_STRING_ID = "ancient_turn_rotate-";

    byte FORK_ID = 3; // развилка
    byte FORK_CHANCE = 3;
    byte FORK_ROTATE_MAX = 4;
    byte[] FORK_MIXING = new byte[] {};
    String FORK_STRING_ID = "ancient_fork_rotate-";

    byte CROSSROADS_ID = 4;
    byte CROSSROADS_CHANCE = 4;
    byte CROSSROADS_ROTATE_MAX = 1;
    byte[] CROSSROADS_q = new byte[] {};
    String CROSSROADS_STRING_ID = "ancient_crossroads";
    String CROSSROADS_TRAP_STRING_ID = "ancient_crossroads_trap";

    byte END_ID = 5;
    byte END_CHANCE = 1;
    byte END_ROTATE_MAX = 4;
    byte[] END_MIXING = new byte[] {};
    String END_STRING_ID = "ancient_end_rotate-";

    byte ENTRY_ID = 6;
    byte ENTRY_ROTATE_MAX = 1;
    byte[] ENTRY_MIXING = new byte[] {};
    String ENTRY_STRING_ID = "ancient_entry";

    String ENTRY_WAY_STRING_ID = "ancient_entry_way";
    String AIR_CUBE_STRING_ID = "air_cube";

    byte BOSS_ID = 7;
    byte BOSS_N_ID = -7;
    byte BOSS_ROTATE_MAX = 1;
    byte[] BOSS_MIXING = new byte[] {};
    String BOSS_STRING_ID = "ancient_boss";

    byte[] ROTATE_MAX = new byte[] {WAY_ROTATE_MAX, TURN_ROTATE_MAX, FORK_ROTATE_MAX, CROSSROADS_ROTATE_MAX, END_ROTATE_MAX, ENTRY_ROTATE_MAX, BOSS_ROTATE_MAX};


    @Deprecated
    String isBossSpawn = "IBSK";
    @Deprecated
    String isAncientWorldGenerateKey = "IAWGK";

    String isPrimalBladeDropKey = "IPBDK";
    String isAncientPortalGenerateKey = "IAPGK";
    String ancientPortalXPosKey = "APXPK";
    String ancientPortalYPosKey = "APYPK";
    String ancientPortalZPosKey = "APZPK";
    String portalDimension = "PD";

}
