package com.artur.returnoftheancients.main;

import com.artur.returnoftheancients.ancientworld.map.gen.GenPhase;
import com.artur.returnoftheancients.ancientworldlegacy.genmap.util.StructureMap;
import com.artur.returnoftheancients.generation.generators.GenStructure;
import com.artur.returnoftheancients.referense.Referense;
import com.artur.returnoftheancients.tileentity.TileBase;
import com.artur.returnoftheancients.tileentity.TileEntityDummy;
import com.artur.returnoftheancients.tileentity.interf.ITileBBProvider;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;

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
