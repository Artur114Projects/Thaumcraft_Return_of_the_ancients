package com.artur.returnoftheancients.main;

import com.artur.returnoftheancients.ancientworldlegacy.genmap.util.StructureMap;
import com.artur.returnoftheancients.generation.generators.GenStructure;
import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;

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

    }

    private static short pacArray(byte[] array) {
        int ret = 0;

        for (int i = array.length; i != 0; i--) {
            byte b = array[i - 1];

            ret |= ((b & 1) << i);
        }

        return (short) ret;
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

}
