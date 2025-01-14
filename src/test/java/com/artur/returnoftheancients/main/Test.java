package com.artur.returnoftheancients.main;

import com.artur.returnoftheancients.ancientworldgeneration.genmap.AncientEntryMapProvider;
import com.artur.returnoftheancients.ancientworldgeneration.structurebuilder.CustomGenStructure;
import com.artur.returnoftheancients.generation.generators.GenStructure;
import com.artur.returnoftheancients.generation.generators.portal.base.AncientPortalsProcessor;
import com.artur.returnoftheancients.handlers.HandlerR;
import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.referense.Referense;
import com.artur.returnoftheancients.transform.util.MappingsProcessor;
import com.artur.returnoftheancients.utils.AspectBottle;
import com.artur.returnoftheancients.utils.math.UltraMutableBlockPos;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Mod.EventBusSubscriber(modid = Referense.MODID)
public class Test { //


    // Проверка выхода за границы и препятствий
    private static boolean isValid(int[][] grid, int x, int y, boolean[][] visited) {
        return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length
                && grid[x][y] == 0 && !visited[x][y];
    }

    public static List<int[]> findPath(int[][] grid, int startX, int startY, int endX, int endY) {
        int[][] directions = { {0, 1}, {1, 0}, {0, -1}, {-1, 0} }; // Вправо, вниз, влево, вверх
        Queue<List<int[]>> queue = new LinkedList<>(); // Очередь для BFS
        boolean[][] visited = new boolean[grid.length][grid[0].length]; // Массив посещений

        // Добавляем стартовую позицию в очередь с пустым путём
        List<int[]> startPath = new ArrayList<>();
        startPath.add(new int[] {startX, startY});
        queue.add(startPath);
        visited[startX][startY] = true;

        while (!queue.isEmpty()) {
            List<int[]> path = queue.poll(); // Берём текущий путь из очереди
            int[] current = path.get(path.size() - 1); // Последняя клетка в пути
            int curX = current[0], curY = current[1];

            if (curX == endX && curY == endY) {
                return path; // Нашли цель, возвращаем путь
            }

            // Добавляем соседей
            for (int[] dir : directions) {
                int newX = curX + dir[0];
                int newY = curY + dir[1];

                if (isValid(grid, newX, newY, visited)) {
                    visited[newX][newY] = true; // Отмечаем как посещённую
                    List<int[]> newPath = new ArrayList<>(path); // Копируем текущий путь
                    newPath.add(new int[] {newX, newY}); // Добавляем новую клетку
                    queue.add(newPath); // Добавляем в очередь
                }
            }
        }


        return null; // Если путь не найден
    }

    public static void main(String[] args) {
        System.out.println("Start!");
//        ChunkPos[] poss = new ChunkPos[8];
//        for (int i = 0; i != 20; i++) {
//            AncientPortalsProcessor.initPortalsPosOnWorld(poss, i);
//        }
        float[] timeArray = new float[200000];
        AncientEntryMapProvider.createAncientEntryMap(new Random(1234568798));
        for (int i = 0; i != 200000; i++) {
            long time = System.nanoTime();
            AncientEntryMapProvider.createAncientEntryMap(new Random(i));
            timeArray[i] = ((System.nanoTime() - time) / 1000000.0F);
//            System.out.println("Created new map! Is took " + timeArray[i] + "ms");
        }
        float resultTime = 0;
        for (float f : timeArray) {
            resultTime += f;
        }
        resultTime /= timeArray.length;
        System.out.println("Is took ~" + resultTime + "ms");
//        ChunkPos chunkPos = new ChunkPos(0, 0);
//        long time = System.nanoTime();
//        UltraMutableBlockPos pos = new UltraMutableBlockPos(64, 0 ,0);
//        System.out.println("Is took " + ((System.nanoTime() - time) / 1000000.0D) + "ms");
//        System.out.println(pos.distance(chunkPos));
    }

    private static int aboba(int i, long b, boolean r, float v, double g, byte e) {
        return 0;
    }

    private static int aboba(long b, boolean r, float v, double g, byte e) {
        return 0;
    }


    protected static int packBytes(byte b1, byte b2, byte b3, byte b4) {
        return (b1 << 24) | ((b2 & 0xFF) << 16) | ((b3 & 0xFF) << 8) | (b4 & 0xFF);
    }

    private static List<UUID> list = new ArrayList<>();

    public static void startTest(EntityPlayer player) {
        startTest = true;
        Test.player = player;
        p = 0;
    }

    private static EntityPlayer player = null;
    private static byte p = 0;
    private static int test = 0;
    private static boolean startTest = false;
    private static final List<Long> templateTime = new ArrayList<>();
    private static final List<Long> myGenTime = new ArrayList<>();


    @SubscribeEvent
    public static void Tick(TickEvent.WorldTickEvent e) {
        if (startTest) {
            test = 200;
            startTest = false;
        }
        if (test > 0) {
            BlockPos playerPos = player.getPosition();
            long time = System.currentTimeMillis();
            CustomGenStructure.registerOrGen(player.world, playerPos.getX() + 2, playerPos.getY(), playerPos.getZ(), "ancient_turn");
            myGenTime.add(System.currentTimeMillis() -  time);
            long time1 = System.currentTimeMillis();
            GenStructure.generateStructure(player.world, playerPos.getX() + 2, playerPos.getY() + 20, playerPos.getZ(), "ancient_turn");
            templateTime.add(System.currentTimeMillis() -  time1);
            GenStructure.generateStructure(player.world, playerPos.getX() + 2, playerPos.getY(), playerPos.getZ(), "air_cube");
            test--;
            if (test % 50 == 0) {
                p += 25;
                player.sendMessage(new TextComponentString(p + "%"));
            }
            if (test <= 0) {
                postProcessing();
            }
        }
    }

    private static void postProcessing() {
        long templateResult = 0;
        long myGenResult = 0;
        for (long i : templateTime) {
            templateResult += i;
        }
        for (long i : myGenTime) {
            myGenResult += i;
        }
        templateResult /= templateTime.size();
        myGenResult /= myGenTime.size();

        player.sendMessage(new TextComponentString("template is took:" + templateResult + "ms"));
        player.sendMessage(new TextComponentString("my gen is took:" + myGenResult + "ms"));
    }
}
