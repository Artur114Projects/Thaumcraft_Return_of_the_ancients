package com.artur.returnoftheancients.main;

import com.artur.returnoftheancients.ancientworld.map.gen.GenPhase;
import com.artur.returnoftheancients.ancientworld.map.utils.EnumMultiChunkStrType;
import com.artur.returnoftheancients.ancientworld.map.utils.EnumRotate;
import com.artur.returnoftheancients.ancientworld.map.utils.StrPos;
import com.artur.returnoftheancients.ancientworld.map.utils.maps.ImmutableMap;
import com.artur.returnoftheancients.referense.Referense;
import net.minecraftforge.fml.common.Mod;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Mod.EventBusSubscriber(modid = Referense.MODID)
public class Test {

    public static void main(String[] args) {
        Random rand = new Random();
        GenPhase generator = GenPhase.initAllGenPhases();
        for (int i = 0; i != 1000; i++) {
            System.out.println("Started gen:" + i);
            generator.getMap(rand.nextLong(), 17);
        }
    }

    private static short pacArray(byte[] array) {
        int ret = 0;

        for (int i = array.length; i != 0; i--) {
            byte b = array[i - 1];

            ret |= ((b & 1) << i);
        }

        return (short) ret;
    }

    protected static int packBytes(byte b1, byte b2, byte b3, byte b4) {
        return (b1 << 24) | ((b2 & 0xFF) << 16) | ((b3 & 0xFF) << 8) | (b4 & 0xFF);
    }
}
