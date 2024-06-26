package com.artur.returnoftheancients.generation.generators;

import static com.artur.returnoftheancients.blocks.TpToAncientWorldBlock.noCollisionNBT;
import static com.artur.returnoftheancients.misc.TRAConfigs.AncientWorldSettings;
import static com.artur.returnoftheancients.misc.TRAConfigs.MobGenSettings;

import static com.artur.returnoftheancients.init.InitDimensions.ancient_world_dim_id;

import com.artur.returnoftheancients.blocks.BossTriggerBlock;
import com.artur.returnoftheancients.events.MCTimer;
import com.artur.returnoftheancients.handlers.EventsHandler;
import com.artur.returnoftheancients.handlers.FreeTeleporter;
import com.artur.returnoftheancients.handlers.HandlerR;
import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.misc.WorldData;
import com.artur.returnoftheancients.utils.interfaces.IALGS;
import com.artur.returnoftheancients.utils.interfaces.IWorldTimer;
import com.artur.returnoftheancients.utils.interfaces.IStructure;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.Random;

public class AncientLabyrinthGenerator implements IStructure, IALGS{
    protected static byte[][] ANCIENT_LABYRINTH_STRUCTURES = new byte[17][17];
    protected static byte[][] ANCIENT_LABYRINTH_STRUCTURES_ROTATE = new byte[17][17];
    protected static final byte SIZE = 17;
    protected static WorldServer world;
    @Deprecated
    protected static final byte[] YX_states = new byte[2];
    protected static int bossGen = 0;
    protected static ArrayList<EntityPlayer> players = new ArrayList<>();
    protected static boolean isGenerateStart = false;
    public static boolean isGen = false;
    public static byte PHASE = -1;
    public static boolean waitPlayersOut = false;
    @Deprecated
    public static long mobId;

    /*
    [[0][1][2][3][4][5][6][7][8]] 0
    [[0][1][2][3][4][5][6][7][8]] 1
    [[0][1][2][3][4][5][6][7][8]] 2
    [[0][1][2][3][4][5][6][7][8]] 3
    [[0][1][2][3][4][5][6][7][8]] 4
    [[0][1][2][3][4][5][6][7][8]] 5
    [[0][1][2][3][4][5][6][7][8]] 6
    [[0][1][2][3][4][5][6][7][8]] 7
    [[0][1][2][3][4][5][6][7][8]] 8
     */
    public static byte[][] getAncientLabyrinthStructures() {return ANCIENT_LABYRINTH_STRUCTURES;}
    public static byte[][] getAncientLabyrinthStructuresRotate() {return ANCIENT_LABYRINTH_STRUCTURES_ROTATE;}

    // размещение структур
    @Deprecated
    protected static void genStructuresInWorld() {
        for (byte y = 0; y != SIZE; y++) {
            for (byte x = 0; x != SIZE; x++) {
                byte structure = ANCIENT_LABYRINTH_STRUCTURES[y][x];
                byte structureRotate  = ANCIENT_LABYRINTH_STRUCTURES_ROTATE[y][x];
                int cx = 128 - 16 * x;
                int cz = 128 - 16 * y;
                int dx = 0;
                int dz = 0;
                switch (structureRotate) {
                    case 1:
                        settings.setRotation(Rotation.NONE);
                        break;
                    case 2:
                        settings.setRotation(Rotation.CLOCKWISE_90);
                        dx = -15;
                        break;
                    case 3:
                        settings.setRotation(Rotation.COUNTERCLOCKWISE_90);
                        dz = -15;
                        break;
                    case 4:
                        settings.setRotation(Rotation.CLOCKWISE_180);
                        dz = -15;
                        dx = -15;
                        break;
                }
                cx = cx - dx;
                cz = cz - dz;
                switch (structure) {
                    case WAY_ID:
                        GenStructure.generateStructure(world, cx, 80, cz, WAY_STRING_ID);
                        break;
                    case CROSSROADS_ID:
                        GenStructure.generateStructure(world, cx, 80, cz, CROSSROADS_STRING_ID);
                        break;
                    case ENTRY_ID:
                        GenStructure.generateStructure(world, cx, 80, cz, ENTRY_STRING_ID);
                        break;
                    case TURN_ID:
                        GenStructure.generateStructure(world, cx, 80, cz, TURN_STRING_ID);
                        break;
                    case FORK_ID:
                        GenStructure.generateStructure(world, cx, 80, cz, FORK_STRING_ID);
                        break;
                    case END_ID:
                        GenStructure.generateStructure(world, cx, 80, cz, END_STRING_ID);
                        break;
                    case BOSS_ID:
                        bossGen++;
                        if (bossGen == 4) {
                            GenStructure.generateStructure(world, cx, 79, cz, BOSS_STRING_ID);
                            bossGen = 0;
                        }
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("WTF????? " + structure);
                        break;
                }
                YX_states[0] = y;
                YX_states[1] = x;
            }
        }
        settings.setRotation(Rotation.NONE);
        GenStructure.generateStructure(world, 4, 124, -14, "ancient_developer_platform");
    }

    @Deprecated
    protected static void clearArea() {
        for (byte y = 0; y != SIZE; y++) {
            for (byte x = 0; x != SIZE; x++) {
                int cx = 128 - 16 * x;
                int cz = 128 - 16 * y;
                GenStructure.generateStructure(world, cx, 80, cz, AIR_CUBE_STRING_ID);
                GenStructure.generateStructure(world, cx, 80 - 31, cz, AIR_CUBE_STRING_ID);
                YX_states[0] = y;
                YX_states[1] = x;
            }
        }
    }

    // EldritchGuardian MindSpider InhabitedZombie
    @Deprecated
    public static void genMobs() {
//        byte[][] ANCIENT_LABYRINTH_MOB_ELDRITCH_GUARDIAN = genMobsMap();
//        byte[][] ANCIENT_LABYRINTH_MOB_MIND_SPIDER = genMobsMap();
//        byte[][] ANCIENT_LABYRINTH_MOB_INHABITED_ZOMBIE = genMobsMap();
//
//        ANCIENT_LABYRINTH_MOB_ELDRITCH_GUARDIAN[8][8] = 8;
//
//        IMobsGen G = (x, z) -> {
////            if (!world.getChunkFromBlockCoords(new BlockPos(x, 81, z)).isLoaded()) {
////                world.getChunkFromBlockCoords(new BlockPos(x, 81, z)).markLoaded(true);
////            }
//            EntityEldritchGuardian e = new EntityEldritchGuardian(world);
//            e.getEntityData().setLong("A_ID", mobId);
//            e.setPositionAndUpdate(x, 81, z);
//            e.forceSpawn = true;
//            world.spawnEntity(e);
//            System.out.println("Eldritch Guardian is gen XYZ " + x + " " + 81 + " " + z);
//        };
//        G.gen(ANCIENT_LABYRINTH_MOB_ELDRITCH_GUARDIAN);
//
//        IMobsGen S = (x, z) -> {
//            EntityMindSpider e = new EntityMindSpider(world);
//            e.getEntityData().setLong("A_ID", mobId);
//            e.setPositionAndUpdate(x, 81, z);
//            e.forceSpawn = true;
//            world.spawnEntity(e);
//            System.out.println("Mind Spider is gen XYZ " + x + " " + 81 + " " + z);
//        };
//        S.gen(ANCIENT_LABYRINTH_MOB_MIND_SPIDER);
//
//        IMobsGen Z = (x, z) -> {
//            Random r = new Random();
//            EntityInhabitedZombie e = new EntityInhabitedZombie(world);
//            e.getEntityData().setLong("A_ID", mobId);
//            e.setPositionAndUpdate(x, 81, z);
//
//            float d = e.world.getDifficulty() == EnumDifficulty.HARD ? 0.9F : 0.6F;
//            e.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(ItemsTC.crimsonPlateHelm));
//            if (r.nextFloat() <= d) {
//                e.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(ItemsTC.crimsonPlateChest));
//            }
//            if (r.nextFloat() <= d) {
//                e.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(ItemsTC.crimsonPlateLegs));
//            }
//
//            e.forceSpawn = true;
//            world.spawnEntity(e);
//            System.out.println("Inhabited Zombie is gen XYZ " + x + " " + 81 + " " + z);
//        };
//        Z.gen(ANCIENT_LABYRINTH_MOB_INHABITED_ZOMBIE);
    }

    @Deprecated
    protected static void startGenMobs() {
        world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(ancient_world_dim_id);
        IWorldTimer iWorldTimer = new IWorldTimer() {
            @Override
            public void start(long time, World world) {
                genMobs();
                EventsHandler.setAncientWorldLoad(false);
            }

            @Override
            public boolean is(long time, World world) {
                return EventsHandler.isAncientWorldLoad();
            }

            @Override
            public boolean isCycle(long time, World world) {
                return false;
            }
        };
        MCTimer.startWorldTimer(iWorldTimer);
    }
    @Deprecated
    protected static void reloadLight() {
        for (int z = -128; z != 144; z++) {
            for (int x = -128; x != 144; x++) {
                world.checkLight(new BlockPos(x, 84, z));
            }
        }
    }

    protected static void genAncientEntryWay() {
        for (int y = 0, cordY = 112; cordY < world.getHeight(); y++) {
            cordY = 112 + 32 * y;
            GenStructure.generateStructure(world, 0, cordY, 0, ENTRY_WAY_STRING_ID);
        }
        GenStructure.generateStructure(world, 6, 255, 6, "ancient_border_cap");
    }


    // разное
    public static void tpToAncientWorld(EntityPlayerMP player) {
        if (world == null) {
            world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(ancient_world_dim_id);
        }
        if (!WorldData.get().saveData.getBoolean(isAncientWorldGenerateKey) || world.playerEntities.isEmpty()) {
            if (!world.playerEntities.isEmpty() && !isGenerateStart) {
                for (EntityPlayer player1 : world.playerEntities) {
                    EventsHandler.tpToHome(player1);
                }
                player.getEntityData().setBoolean(noCollisionNBT, true);
                player.getEntityData().setBoolean(EventsHandler.tpToHomeNBT, true);
                return;
            }
            players.add(player);
            FreeTeleporter.teleportToDimension(player, ancient_world_dim_id, 8, 126, -10);
            HandlerR.setLoadingGuiState(player, true);
            if (!isGenerateStart) {
                genAncientLabyrinth(player);
            } else if (world.playerEntities.isEmpty()) {
                genAncientLabyrinth(player);
            } else {
                player.getEntityData().setLong("getReward", WorldData.get().saveData.getLong("getReward"));
                HandlerR.injectPhaseOnClient(player, PHASE);
            }
        } else {
            if (player.isCreative()) {
                FreeTeleporter.teleportToDimension(player, ancient_world_dim_id, 8, 126, -10);
            } else {
                if (WorldData.get().saveData.getBoolean(isBossSpawn)) {
                    int[] a = WorldData.get().saveData.getIntArray("bossTriggerBlockPos");
                    FreeTeleporter.teleportToDimension(player, ancient_world_dim_id, a[0], a[1] + 2, a[2] + 8);
                } else {
                    player.getEntityData().setLong("getReward", WorldData.get().saveData.getLong("getReward"));
                    FreeTeleporter.teleportToDimension(player, ancient_world_dim_id, 8, 253, 8);
                }
            }
        }
    }

    protected static void genAncientLabyrinth(EntityPlayer player) {
        WorldData worldData = WorldData.get();
        worldData.saveData.setBoolean(isAncientWorldGenerateKey, false);
        worldData.saveData.setBoolean(isBossSpawn, false);
        worldData.saveData.setLong("getReward", new Random().nextLong());
        worldData.markDirty();
        player.getEntityData().setLong("getReward", WorldData.get().saveData.getLong("getReward"));
        if (TRAConfigs.PortalSettings.isSendWorldLoadMessage) {
            HandlerR.sendAllWorldLoadMessage(true);
        }
        isGen = false;
        isGenerateStart = true;
        EventsHandler.setAncientWorldLoad(false);
        GenStructure.generateStructure(world, 4, 124, -14, "ancient_developer_platform");

        System.out.println("Generating ancient labyrinth start");
        PHASE = 0;
        for (EntityPlayer player1 : players){
            HandlerR.injectPhaseOnClient((EntityPlayerMP) player1, (byte) 0);
        }
        byte[][][] a;
        if (AncientWorldSettings.isOldGenerator) {
            a = AncientLabyrinthOldMap.genStructuresMap();
        } else {
            a = AncientLabyrinthMap.genStructuresMap();
        }
        ANCIENT_LABYRINTH_STRUCTURES = a[0];
        ANCIENT_LABYRINTH_STRUCTURES_ROTATE = a[1];

        PHASE = 1;
        for (EntityPlayer player1 : players){
            HandlerR.injectPhaseOnClient((EntityPlayerMP) player1, (byte) 1);
        }

        System.out.println("Cleaning area");
        AncientWorldBuildProcessor.clearArea();
    }
    private static void genFinish() {
        System.out.println("Generate ancient labyrinth finish");
        PHASE = 4;
        for (EntityPlayer player1 : players){
            HandlerR.injectPhaseOnClient((EntityPlayerMP) player1, (byte) 4);
        }

        isGen = true;
        for (EntityPlayer player : players) {
            HandlerR.setLoadingGuiState((EntityPlayerMP) player, false);
            if (player != null) {
                player.setHealth(20);
                if (!player.isCreative()) {
                    ((EntityPlayerMP) player).connection.setPlayerLocation(8, 253, 8, -181, 0);
                }
            }
        }
        WorldData worldData = WorldData.get();
        worldData.saveData.setBoolean(isAncientWorldGenerateKey, true);
        worldData.markDirty();
        players.clear();
        if (TRAConfigs.PortalSettings.isSendWorldLoadMessage) {
            HandlerR.sendAllWorldLoadMessage(false);
        }
        isGenerateStart = false;
    }

    public static class AncientWorldBuildProcessor {
        public static void clearArea() {
            clear = true;
        }

        public static void genStructuresInWorld() {
            please = true;
        }

        public static void reloadLight() {
            reloadLight = true;
        }

        static byte xtp = 0;
        static byte ytp = 0;
        static byte xtc = 0;
        static byte ytc = 0;
        static int xtl = -128;
        static int ytl = -128;

        static boolean reloadLight = false;
        static boolean please = false;
        static boolean clear = false;

        static byte t = 0;

        @SubscribeEvent
        public void Tick(TickEvent.WorldTickEvent e) {
            if (!e.world.isRemote) {
                if (please) {
                    if (t == AncientWorldSettings.AncientWorldGenerationSettings.structuresGenerationDelay) {
                        t = 0;
//                        System.out.println("please " + "x" + xtp + " y" + ytp);
                        if (xtp == SIZE) {
                            ytp++;
                            xtp = 0;
                            for (EntityPlayer player1 : players){
                                HandlerR.injectPercentagesOnClient((EntityPlayerMP) player1, xtp, ytp);
                            }
                        }
                        if (ytp == SIZE) {
                            ytp = 0;
                            xtp = 0;
                            settings.setRotation(Rotation.NONE);
                            please = false;
                            PHASE = 3;
                            for (EntityPlayer player1 : players){
                                HandlerR.injectPhaseOnClient((EntityPlayerMP) player1, (byte) 3);
                            }
                            System.out.println("Reload light");
                            reloadLight();
                            return;
                        }
                        byte structure = ANCIENT_LABYRINTH_STRUCTURES[ytp][xtp];
                        byte structureRotate = ANCIENT_LABYRINTH_STRUCTURES_ROTATE[ytp][xtp];
                        int cx = 128 - 16 * xtp;
                        int cz = 128 - 16 * ytp;
                        int dx = 0;
                        int dz = 0;
                        switch (structureRotate) {
                            case 1:
                                settings.setRotation(Rotation.NONE);
                                break;
                            case 2:
                                settings.setRotation(Rotation.CLOCKWISE_90);
                                dx = -15;
                                break;
                            case 3:
                                settings.setRotation(Rotation.COUNTERCLOCKWISE_90);
                                dz = -15;
                                break;
                            case 4:
                                settings.setRotation(Rotation.CLOCKWISE_180);
                                dz = -15;
                                dx = -15;
                                break;
                        }
                        cx = cx - dx;
                        cz = cz - dz;
                        switch (structure) {
                            case WAY_ID:
                                GenStructure.generateStructure(world, cx, 80, cz, WAY_STRING_ID);
                                break;
                            case CROSSROADS_ID:
                                GenStructure.generateStructure(world, cx, 80, cz, CROSSROADS_STRING_ID);
                                break;
                            case ENTRY_ID:
                                GenStructure.generateStructure(world, cx, 80, cz, ENTRY_STRING_ID);
                                break;
                            case TURN_ID:
                                GenStructure.generateStructure(world, cx, 80, cz, TURN_STRING_ID);
                                break;
                            case FORK_ID:
                                GenStructure.generateStructure(world, cx, 80, cz, FORK_STRING_ID);
                                break;
                            case END_ID:
                                GenStructure.generateStructure(world, cx, 80, cz, END_STRING_ID);
                                break;
                            case BOSS_ID:
                                bossGen++;
                                if (bossGen == 4) {
                                    GenStructure.generateStructure(world, cx, 79, cz, BOSS_STRING_ID);
                                    bossGen = 0;
                                }
                                break;
                            case 0:
                                break;
                            default:
                                System.out.println("WTF????? " + structure);
                                break;
                        }

                        xtp++;
                    }
                    t++;
                }
                if (clear) {
                    for (byte i = 0; i != AncientWorldSettings.AncientWorldGenerationSettings.numberSetClearPerTick; i++) {
                        if (xtc == SIZE) {
                            ytc++;
                            xtc = 0;
                            for (EntityPlayer player1 : players){
                                HandlerR.injectPercentagesOnClient((EntityPlayerMP) player1, xtc, ytc);
                            }
                        }
                        if (ytc == SIZE) {
                            ytc = 0;
                            xtc = 0;
                            clear = false;
                            genAncientEntryWay();
                            PHASE = 2;
                            for (EntityPlayer player1 : players){
                                HandlerR.injectPhaseOnClient((EntityPlayerMP) player1, (byte) 2);
                            }

                            System.out.println("Generate structures");
                            genStructuresInWorld();
                            return;
                        }
                        int cx = 128 - 16 * xtc;
                        int cz = 128 - 16 * ytc;
                        GenStructure.generateStructure(world, cx, 80, cz, AIR_CUBE_STRING_ID);
                        GenStructure.generateStructure(world, cx, 80 - 31, cz, AIR_CUBE_STRING_ID);
//                        System.out.println("clear x:" + cx + " z:" + cz);

                        xtc++;
                    }
                }
                if (reloadLight) {
                    for (int i = 0; i != AncientWorldSettings.AncientWorldGenerationSettings.numberSetReloadLightPerTick; i++) {
                        if (xtl == 144) {
                            ytl++;
                            xtl = -128;
                        }
                        if (ytl == 144) {
                            ytl = -128;
                            xtl = -128;
                            reloadLight = false;
                            genFinish();
                            return;
                        }
                        world.checkLight(new BlockPos(xtl, 84, ytl));
                        xtl++;
                    }
                }
            }
        }
    }
}
