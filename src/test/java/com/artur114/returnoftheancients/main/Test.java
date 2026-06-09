package com.artur114.returnoftheancients.main;

import com.artur114.returnoftheancients.referense.Referense;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(modid = Referense.MODID)
public class Test {

    public static void main(String[] args) {
        Vec3i size = new Vec3i(16, 16, 16);
        Random rand = new Random();

        for (int i = 0; i != size.getX() * size.getY() * size.getZ(); i++) {
            int x = rand.nextInt(16), y = rand.nextInt(16), z = rand.nextInt(16);
            System.out.println("[" + x + ", " + y + ", " + z + "]");
            i = (x & 15) + (y & 15) * 16 + (z & 15) * (16 * 16);
            System.out.println("[" + (i % size.getX()) + ", " + ((i / size.getX()) % size.getY()) + ", " + (((i / size.getX()) / size.getY()) % size.getZ()) + "]");
            System.out.println();
        }


//        Random rand = new Random();
//        GenPhase generator = GenPhase.initAllGenPhases();
//        for (int i = 0; i != 1000; i++) {
//            System.out.println("Started gen:" + i);
//            generator.getMap(rand.nextLong(), 17);
//        }
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
