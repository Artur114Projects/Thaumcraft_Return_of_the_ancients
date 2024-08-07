package com.artur.returnoftheancients.generation.generators;

import static com.artur.returnoftheancients.blocks.TpToAncientWorldBlock.noCollisionNBT;
import static com.artur.returnoftheancients.misc.TRAConfigs.AncientWorldSettings;
import static com.artur.returnoftheancients.misc.TRAConfigs.MobGenSettings;

import static com.artur.returnoftheancients.init.InitDimensions.ancient_world_dim_id;

import com.artur.returnoftheancients.ancientworldgeneration.genmap.AncientLabyrinthMap;
import com.artur.returnoftheancients.ancientworldgeneration.structurebuilder.CustomGenStructure;
import com.artur.returnoftheancients.ancientworldgeneration.util.StructureMap;
import com.artur.returnoftheancients.events.MCTimer;
import com.artur.returnoftheancients.handlers.ServerEventsHandler;
import com.artur.returnoftheancients.handlers.FreeTeleporter;
import com.artur.returnoftheancients.handlers.HandlerR;
import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.misc.WorldData;
import com.artur.returnoftheancients.referense.Referense;
import com.artur.returnoftheancients.utils.interfaces.IALGS;
import com.artur.returnoftheancients.utils.interfaces.IWorldTimer;
import com.artur.returnoftheancients.utils.interfaces.IStructure;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class AncientLabyrinthGenerator implements IStructure, IALGS{
    protected static StructureMap MAP = new StructureMap(new byte[17][17], new byte[17][17]);
    protected static final byte SIZE = 17;
    protected static World world;
    @Deprecated
    protected static final byte[] YX_states = new byte[2];

    protected static ArrayList<EntityPlayer> players = new ArrayList<>();
    protected static boolean isGenerateStart = false;
    public static boolean isGen = false;
    public static byte PHASE = -1;
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

    // размещение структур
    @Deprecated
    protected static void genStructuresInWorld() {
        for (byte y = 0; y != SIZE; y++) {
            for (byte x = 0; x != SIZE; x++) {
                byte structure = MAP.getStructure(x, y);
                byte structureRotate  = MAP.getRotate(x, y);
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
//                        bossGen++;
//                        if (bossGen == 4) {
//                            GenStructure.generateStructure(world, cx, 79, cz, BOSS_STRING_ID);
//                            bossGen = 0;
//                        }
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
            }

            @Override
            public boolean is(long time, World world) {
                return false;
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
            CustomGenStructure.gen(world, 0, cordY, 0, ENTRY_WAY_STRING_ID);
        }
        CustomGenStructure.gen(world, 6, 255, 6, "ancient_border_cap");
    }


    // разное
    public static void tpToAncientWorld(EntityPlayerMP player) {
        world = Objects.requireNonNull(player.getServerWorld().getMinecraftServer()).getWorld(ancient_world_dim_id);
        if (!WorldData.get().saveData.getBoolean(isAncientWorldGenerateKey) || world.playerEntities.isEmpty()) {
            if (!world.playerEntities.isEmpty() && !isGenerateStart) {
                AncientWorldBuildProcessor.tpToHomePlayers(world.playerEntities);
                player.getEntityData().setBoolean(ServerEventsHandler.tpToHomeNBT, true);
                player.getEntityData().setBoolean(noCollisionNBT, true);
                return;
            }
            players.add(player);
            if (player.world.provider.getDimension() != ancient_world_dim_id) {
                FreeTeleporter.teleportToDimension(player, ancient_world_dim_id, 8, 126, -10);
            }
            HandlerR.setLoadingGuiState(player, true);
            HandlerR.injectPercentagesOnClient(player, 0, 0);
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
        GenStructure.generateStructure(world, 4, 124, -14, "ancient_developer_platform");

        System.out.println("Generating ancient labyrinth start");
        PHASE = 0;
        for (EntityPlayer player1 : players){
            HandlerR.injectPhaseOnClient((EntityPlayerMP) player1, (byte) 0);
        }
        StructureMap a;
        if (AncientWorldSettings.isOldGenerator) {
            byte[][][] r = AncientLabyrinthOldMap.genStructuresMap();
            a = new StructureMap(r[0], r[1]);
        } else {
            a = AncientLabyrinthMap.genStructuresMap();
        }
        MAP = a;

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

    public static void stopGenerationFor(EntityPlayer player) {
        players.remove(player);
        if (players.isEmpty()) {
            isGenerateStart = false;
            AncientWorldBuildProcessor.stop();
            if (TRAConfigs.PortalSettings.isSendWorldLoadMessage) {
                HandlerR.sendAllWorldLoadMessage(false);
            }
        }
    }

    @Mod.EventBusSubscriber(modid = Referense.MODID)
    public static class AncientWorldBuildProcessor {
        private static void tpToHomePlayers(List<EntityPlayer> playerList) {
            playersTP = playerList;
            tpToHome = true;
        }

        private static void clearArea() {
            clear = true;
        }

        private static void genStructuresInWorld() {
            please = true;
        }

        private static void reloadLight() {
            reloadLight = true;
        }

        private static void stop() {
            reloadLight = false;
            please = false;
            clear = false;
            xtp = 0;
            ytp = 0;
            xtc = 0;
            ytc = 0;
            xtl = -128;
            ytl = -128;
            t = 0;
        }

        private static byte xtp = 0;
        private static byte ytp = 0;
        private static byte xtc = 0;
        private static byte ytc = 0;
        private static int xtl = -128;
        private static int ytl = -128;

        private static boolean reloadLight = false;
        private static boolean please = false;
        private static boolean clear = false;
        private static boolean tpToHome = false;
        private static List<EntityPlayer> playersTP = new ArrayList<>();

        private static byte t = 0;
        private static byte tht = 0;
        protected static int bossGen = 0;

        @SubscribeEvent
        public static void Tick(TickEvent.WorldTickEvent e) {
            if (!e.world.isRemote) {
                if (please) {
                    if (t == AncientWorldSettings.AncientWorldGenerationSettings.structuresGenerationDelay) {
                        t = 0;
                        if (xtp == SIZE) {
                            ytp++;
                            xtp = 0;
                            if (!players.isEmpty()) {
                                for (EntityPlayer player1 : players) {
                                    HandlerR.injectPercentagesOnClient((EntityPlayerMP) player1, xtp, ytp);
                                }
                            }
                        }
                        if (ytp == SIZE) {
                            ytp = 0;
                            xtp = 0;
                            settings.setRotation(Rotation.NONE);
                            please = false;
                            PHASE = 3;
                            if (!players.isEmpty()) {
                                for (EntityPlayer player1 : players) {
                                    HandlerR.injectPhaseOnClient((EntityPlayerMP) player1, (byte) 3);
                                }
                            }
                            System.out.println("Reload light");
                            reloadLight();
                            return;
                        }
                        byte structure = MAP.getStructure(xtp, ytp);
                        byte structureRotate = MAP.getRotate(xtp, ytp);
                        int cx = 128 - 16 * xtp;
                        int cz = 128 - 16 * ytp;
                        byte rotate = 0;
                        switch (structureRotate) {
                            case 1:
                                rotate = 1;
                                break;
                            case 2:
                                rotate = 2;
                                break;
                            case 3:
                                rotate = 3;
                                break;
                            case 4:
                                rotate = 4;
                                break;
                        }
                        switch (structure) {
                            case WAY_ID:
                                CustomGenStructure.gen(world, cx, 80, cz, WAY_STRING_ID + rotate);
                                break;
                            case CROSSROADS_ID:
                                CustomGenStructure.gen(world, cx, 80, cz, CROSSROADS_STRING_ID);
                                break;
                            case ENTRY_ID:
                                CustomGenStructure.gen(world, cx, 80, cz, ENTRY_STRING_ID);
                                break;
                            case TURN_ID:
                                CustomGenStructure.gen(world, cx, 80, cz, TURN_STRING_ID + rotate);
                                break;
                            case FORK_ID:
                                CustomGenStructure.gen(world, cx, 80, cz, FORK_STRING_ID + rotate);
                                break;
                            case END_ID:
                                CustomGenStructure.gen(world, cx, 80, cz, END_STRING_ID + rotate);
                                break;
                            case BOSS_ID:
                                bossGen++;
                                if (bossGen == 4) {
                                    CustomGenStructure.gen(world, cx, 79, cz, BOSS_STRING_ID);
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
                            if (!players.isEmpty()) {
                                for (EntityPlayer player1 : players) {
                                    HandlerR.injectPercentagesOnClient((EntityPlayerMP) player1, xtc, ytc);
                                }
                            }
                        }
                        if (ytc == SIZE) {
                            ytc = 0;
                            xtc = 0;
                            clear = false;
                            genAncientEntryWay();
                            PHASE = 2;
                            if (!players.isEmpty()) {
                                for (EntityPlayer player1 : players) {
                                    HandlerR.injectPhaseOnClient((EntityPlayerMP) player1, (byte) 2);
                                    HandlerR.injectPercentagesOnClient((EntityPlayerMP) player1, 0, 0);
                                }
                            }
                            System.out.println("Generate structures");
                            genStructuresInWorld();
                            return;
                        }
                        int cx = 128 - 16 * xtc;
                        int cz = 128 - 16 * ytc;
                        CustomGenStructure.gen(world, cx, 80, cz, AIR_CUBE_STRING_ID);
                        CustomGenStructure.gen(world, cx, 80 - 31, cz, AIR_CUBE_STRING_ID);
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
                if (tpToHome) {
                    if (tht == playersTP.size()) {
                        tpToHome = false;
                        tht = 0;
                        playersTP.clear();
                        System.out.println("Players is teleported");
                        return;
                    }
                    EntityPlayerMP player = (EntityPlayerMP) playersTP.get(tht);
                    ServerEventsHandler.tpToHome(player);
                }
            }
        }
    }
}
