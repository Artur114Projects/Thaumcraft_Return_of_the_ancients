package com.artur.returnoftheancients.generation.terraingen;

import com.artur.returnoftheancients.handlers.HandlerR;
import com.artur.returnoftheancients.misc.TRAConfigs;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class TerrainGenHandler {
    private static int portalGenerateOffset(Random rand) {
        return (((rand.nextInt(8) - 4) + 1) << 8);
    }

    public static void initPortalsPosOnWorld(ChunkPos[] portalPos, long seed) {
        final Random rand = new Random(seed);
        rand.nextInt();

        final double angleOffset = ((Math.PI * 2) / (rand.nextInt(16) + 1));
        final int defaultDistance = 4000;
        final int distance = 8000;

        for (int i = 0; i != portalPos.length; i++) {
            double angle = (((Math.PI * 2) / portalPos.length) * i) + angleOffset;
            int radius = defaultDistance + (distance * i) + portalGenerateOffset(rand);

            int chunkX = (int) ((radius * Math.cos(angle)) + portalGenerateOffset(rand)) >> 8;
            int chunkZ = (int) ((radius * Math.sin(angle)) + portalGenerateOffset(rand)) >> 8;

            portalPos[i] = new ChunkPos(chunkX << 4, chunkZ << 4);
        }

        if (TRAConfigs.Any.debugMode) {
            System.out.println("Seed:" + seed);
            for (ChunkPos pos : portalPos) {
                System.out.println(pos);
            }
        }
    }

    public static boolean isCollideToAnyPortal(ChunkPos[] portalsPos, int x, int z, int bitShift, int offset) {
        for (ChunkPos pos : portalsPos) {
            if (HandlerR.isCollide(x, z, pos.x >> bitShift, pos.z >> bitShift, offset)) {
                return true;
            }
        }
        return false;
    }

    public static int findAnyBiomeOnBAOnRange1(int[] aint, int[] biome, int i1, int j1, int areaWidth1, int range) {
        for (int range1 = 1; range1 != range + 1; range1++) {
            for (int i = 0; i != 4 * range1; i++) {
                float angle = (float) (((Math.PI * 2.0F) / (4.0F * range1)) * i);
                int k = aint[(int)((i1 + range1 * MathHelper.cos(angle)) + (j1 + range1 * MathHelper.sin(angle)) * areaWidth1)];
                if (HandlerR.arrayContains(biome, k)) {
                    return k;
                }
            }
        }
        return -1;
    }

    public static int findAnyBiomeOnBAOnRange2(int[] aint, int[] biome, int i1, int j1, int areaWidth1, int range) {
        for (int i = i1 - range; i != i1 + range + 1; i++) {
            for (int j = j1 - range; j != j1 + range + 1; j++) {
                if (i == i1 && j == j1) {
                    continue;
                }
                if (i == i1 - range && j == j1 - range) {
                    continue;
                }
                if (i == i1 - range && j == j1 + range) {
                    continue;
                }
                if (i == i1 + range && j == j1 - range) {
                    continue;
                }
                if (i == i1 + range && j == j1 + range) {
                    continue;
                }
                int k = aint[i + j * areaWidth1];
                if (HandlerR.arrayContains(biome, k)) {
                    return k;
                }
            }
        }
        return -1;
    }

    public static boolean hasBiomeOnRange0(int[] aint, int biome, int i1, int j1, int areaWidth1, int range) {
        for (int i = i1 - range; i != i1 + range + 1; i++) {
            for (int j = j1 - range; j != j1 + range + 1; j++) {
                if (i == i1 && j == j1) {
                    continue;
                }
                if (biome == aint[i + j * areaWidth1]) {
                    return true;
                }
            }
        }
        return false;
    }


    public static boolean hasBiomeOnRange1(int[] aint, int biome, int i1, int j1, int areaWidth1, int range) {
        for (int range1 = 1; range1 != range + 1; range1++) {
            for (int i = 0; i != 4 * range1; i++) {
                float angle = (float) (((Math.PI * 2.0F) / (4.0F * range1)) * i);
                if (biome == (aint[(int)((i1 + range1 * MathHelper.cos(angle)) + (j1 + range1 * MathHelper.sin(angle)) * areaWidth1)])) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasBiomeOnRange0(int[] aint, int[] biome, int i1, int j1, int areaWidth1, int range) {
        for (int i = i1 - range; i != i1 + range + 1; i++) {
            for (int j = j1 - range; j != j1 + range + 1; j++) {
                if (i == i1 && j == j1) {
                    continue;
                }
                if (HandlerR.arrayContains(biome, aint[i + j * areaWidth1])) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasBiomeOnRange1(int[] aint, int[] biome, int i1, int j1, int areaWidth1, int range) {
        for (int range1 = 1; range1 != range + 1; range1++) {
            for (int i = 0; i != 4 * range1; i++) {
                float angle = (float) (((Math.PI * 2.0F) / (4.0F * range1)) * i);
                if (HandlerR.arrayContains(biome, (aint[(int)((i1 + range1 * MathHelper.cos(angle)) + (j1 + range1 * MathHelper.sin(angle)) * areaWidth1)]))) {
                    return true;
                }
            }
        }
        return false;
    }


    public static boolean isAllBiomesOnRangeContainedOnBA0(int[] aint, int[] biomeArray, int i1, int j1, int areaWidth1, int range) {
        for (int i = i1 - range; i != i1 + range + 1; i++) {
            for (int j = j1 - range; j != j1 + range + 1; j++) {
                if (i == i1 && j == j1) {
                    continue;
                }
                if (!HandlerR.arrayContains(biomeArray, aint[i + j * areaWidth1])) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isAllBiomesOnRangeContainedOnBA1(int[] aint, int[] biomeArray, int i1, int j1, int areaWidth1, int range) {
        for (int range1 = 1; range1 != range + 1; range1++) {
            for (int i = 0; i != 4 * range1; i++) {
                float angle = (float) (((Math.PI * 2.0F) / (4.0F * range1)) * i);
                if (!HandlerR.arrayContains(biomeArray, (aint[(int)((i1 + range1 * MathHelper.cos(angle)) + (j1 + range1 * MathHelper.sin(angle)) * areaWidth1)]))) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isAllBiomesOnRangeEqualsInt1(int[] aint, int biome, int i1, int j1, int areaWidth1, int range) {
        for (int range1 = 1; range1 != range + 1; range1++) {
            for (int i = 0; i != 4 * range1; i++) {
                float angle = (float) (((Math.PI * 2.0F) / (4.0F * range1)) * i);
                if (biome != (aint[(int)((i1 + range1 * MathHelper.cos(angle)) + (j1 + range1 * MathHelper.sin(angle)) * areaWidth1)])) {
                    return false;
                }
            }
        }
        return true;
    }



    public static int getRandomIntOnArray(int[] array, int index, int badResult) {
        if (array[index] == badResult) {
            if (index + 1 < array.length) {
                return array[index + 1];
            } else {
                return array[index - 1];
            }
        } else {
            return array[index];
        }
    }
}
