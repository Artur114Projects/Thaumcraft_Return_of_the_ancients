package com.artur.returnoftheancients.main;

import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Referense.MODID)
public class Test {

    public static void main(String[] args) {
        RenderPlayer renderplayer;
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
