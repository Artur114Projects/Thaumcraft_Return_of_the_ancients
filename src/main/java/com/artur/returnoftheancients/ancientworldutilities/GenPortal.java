package com.artur.returnoftheancients.ancientworldutilities;

import com.artur.returnoftheancients.generation.generators.GenStructure;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

public class GenPortal {
    static int dimension;
    static int x;
    static int y;
    static int z;
    static boolean start = false;
    static boolean gen = false;
    static int genTime = 0;
    static boolean back = false;
    static boolean runBack = false;
    static short backTime = 0;
    static List<IBlockState[][]> blocks = new ArrayList<>();

    public static void start(int dimension, int x, int y, int z) {
        GenPortal.dimension = dimension;
        GenPortal.x = x;
        GenPortal.y = y;
        GenPortal.z = z;
        GenPortal.start = true;
        GenPortal.gen = true;
    }

    public static void start(int dimension, BlockPos pos) {
        GenPortal.dimension = dimension;
        GenPortal.x = pos.getX();
        GenPortal.y = pos.getY();
        GenPortal.z = pos.getZ();
        GenPortal.start = true;
        GenPortal.gen = true;
    }


    @SubscribeEvent
    public void Tick(TickEvent.WorldTickEvent e) {
        if (start) {
            if (e.world.provider.getDimension() == dimension) {
                if (gen) {
                    int y = GenPortal.y - genTime;
                    if (y == 0) {
                        GenStructure.generateStructure(e.world, x - 3, 0, z - 3, "ancient_portal_floor");
                        gen = false;
                        back = true;
                        genTime = 0;
                        return;
//                        for (IBlockState[][] states : blocks) {
//                            Handler.SOUT2DArray(states);
//                        }
                    } else {
                        IBlockState[][] blockStates = new IBlockState[6][6];
                        for (int i = x + 2; i >= x - 3; i--) {
                            for (int j = z + 2; j >= z - 3; j--) {
                                blockStates[j - (z - 3)][i - (x - 3)] = e.world.getBlockState(new BlockPos(i, y - 1, j));
                            }
                        }
                        blocks.add(GenPortal.y - (y), blockStates);
                        System.out.println(blocks.size());
                        GenStructure.generateStructure(e.world, x - 3, y - 1, z - 3, "ancient_kusok_portal");
                    }
                    EntityPlayerSP playerSP = Minecraft.getMinecraft().player;
                    if (playerSP.motionY > 0 && !playerSP.isCreative()) {
                        playerSP.motionY -= 1;
                    }
                    genTime++;
                }
                if (back) {
                    if (backTime >= 200 || runBack) {
                        if (!runBack) {
                            runBack = true;
                            backTime = 0;
                        }
                        int y = backTime;
                        if (backTime == GenPortal.y) {
                            back = false;
                            start = false;
                            blocks.clear();
                            runBack = false;
                            backTime = 0;
                            return;
                        }
                        IBlockState[][] states = blocks.get(GenPortal.y - backTime - 1);
                        for (int i = x + 2; i >= x - 3; i--) {
                            for (int j = z + 2; j >= z - 3; j--) {
                                if (states[j - (z - 3)][i - (x - 3)] != null) {
                                    e.world.setBlockState(new BlockPos(i, y, j), states[j - (z - 3)][i - (x - 3)]);
                                }
                            }
                        }
                    }
                    backTime++;
                }
            }
        }
    }
}
