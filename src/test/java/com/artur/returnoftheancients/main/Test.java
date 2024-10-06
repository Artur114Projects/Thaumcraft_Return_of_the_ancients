package com.artur.returnoftheancients.main;

import com.artur.returnoftheancients.ancientworldgeneration.genmap.AncientEntryMapProvider;
import com.artur.returnoftheancients.ancientworldgeneration.structurebuilder.CustomGenStructure;
import com.artur.returnoftheancients.generation.generators.GenStructure;
import com.artur.returnoftheancients.handlers.HandlerR;
import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Mod.EventBusSubscriber(modid = Referense.MODID)
public class Test { //
    public static void main(String[] args) {
        double baseGen = 100;
        // Начальное значение множителя = 1.0;
        double genMult = 1.0d;
        double gen = 1;
        //Делегируем значение буста на предмет-бустер, таким образом получается 1..n тиров со своими значениями. Главное отрицательными их не сделать ^_^
        for(int i = 0; i != 2; i++) {
            //Значение getBoost должно лежать на (0; 1].
            gen *= 0.8;
            // Подобная формула приведёт к тому, что значение genMult будет 0 < genBoost <= 1.
        }

        //Поэтому мы его прибавим к 1 и домножим на какое-то базовое значение.
        //Ну и обработаем слувай, когда улучшений нет, конечно же.
        System.out.println(baseGen * (1 + gen));
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
