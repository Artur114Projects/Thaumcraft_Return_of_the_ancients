package com.artur.returnoftheancients.generation.generators;

import static com.artur.returnoftheancients.ancientworldutilities.Configs.AncientWorldSettings;
import static com.artur.returnoftheancients.ancientworldutilities.Configs.MobGenSettings;

import com.artur.returnoftheancients.ancientworldutilities.MCTimer;
import com.artur.returnoftheancients.ancientworldutilities.WorldData;
import static com.artur.returnoftheancients.handlers.Handler.*;
import static com.artur.returnoftheancients.init.InitDimensions.ancient_world_dim_id;
import static com.artur.returnoftheancients.generation.generators.AncientLabyrinthGeneratorHandler.*;

import com.artur.returnoftheancients.handlers.EventsHandler;
import com.artur.returnoftheancients.utils.interfaces.IALGS;
import com.artur.returnoftheancients.utils.interfaces.IMobsGen;
import com.artur.returnoftheancients.utils.interfaces.IWorldTimer;
import com.artur.returnoftheancients.utils.interfaces.IStructure;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.entities.monster.EntityEldritchGuardian;
import thaumcraft.common.entities.monster.EntityInhabitedZombie;
import thaumcraft.common.entities.monster.EntityMindSpider;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class AncientLabyrinthGenerator implements IStructure, IALGS{
    protected static byte[][] ANCIENT_LABYRINTH_STRUCTURES = new byte[17][17];
    protected static byte[][] ANCIENT_LABYRINTH_STRUCTURES_ROTATE = new byte[17][17];
    protected static final byte SIZE = 17;
    protected static WorldServer world;
    protected static final byte[] YX_states = new byte[2];
    protected static int bossGen = 0;

    public static boolean isGen = false;
    public static byte PHASE = -1;
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

    public static void startGenMobs() {
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

    protected static void reloadLight() {
        for (int z = -128; z != 144; z++) {
            for (int x = -128; x != 144; x++) {
                world.checkLight(new BlockPos(x, 84, z));
            }
        }
        for (int z = 6; z != 10; z++) {
            for (int x = 6; x != 10; x++) {
                for (int y = 82; y != 255; y++) {
                    world.checkLight(new BlockPos(x, y, z));
                }
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
    public static float getPercentages() {return (float) (((16 * YX_states[0]) + (YX_states[1] + 1)) / 2.89);}

    public static void genAncientLabyrinth(EntityPlayer player) {
        isGen = false;
        EventsHandler.setAncientWorldLoad(false);
        Random r = new Random();
        mobId = r.nextLong();
        WorldData worldData = WorldData.get();
        worldData.saveData.setLong("mobId", mobId);
        worldData.markDirty();
        world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(ancient_world_dim_id);
        System.out.println("Generating ancient labyrinth start");
//        genRandomStructures();
        PHASE = 0;
//        System.out.println(Arrays.deepToString(ANCIENT_LABYRINTH_STRUCTURES));
//        genRightLeftWay();
        PHASE = 1;
//        System.out.println(Arrays.deepToString(ANCIENT_LABYRINTH_STRUCTURES));
//        genDownUpWay();
        byte[][][] a;
        if (AncientWorldSettings.isOldGenerator) {
            a = AncientLabyrinthOldMap.genStructuresMap();
        } else {
            a = AncientLabyrinthMap.genStructuresMap();
        }
        ANCIENT_LABYRINTH_STRUCTURES = a[0];
        ANCIENT_LABYRINTH_STRUCTURES_ROTATE = a[1];
        PHASE = 2;
        System.out.println("Generating Ancient Entry Way");
        PHASE = 3;
        genAncientEntryWay();
        System.out.println("Cleaning area");
        PHASE = 4;
        clearArea();
        System.out.println("Generate structures");
        PHASE = 5;
        genStructuresInWorld();
        System.out.println("Reload light");
        PHASE = 6;
        reloadLight();
        System.out.println("Generate ancient labyrinth finish!");
        PHASE = 7;
        isGen = true;
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
