package com.artur.returnoftheancients.main;

import com.artur.returnoftheancients.ancientworldgeneration.structurebuilder.CustomGenStructure;
import com.artur.returnoftheancients.generation.generators.GenStructure;
import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Referense.MODID)
public class Test { //
    public static void main(String[] args) {
        System.out.println((int) 10240 / 10000);
    }

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
