package com.artur.returnoftheancients.handlers;

import com.artur.returnoftheancients.ancientworldutilities.Configs;
import com.artur.returnoftheancients.generation.generators.GenStructure;
import ibxm.Player;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class Handler {

    public static boolean isTriggered = false;

    public static int genRandomIntRange(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public static void playSound(SoundEvent e) {
        System.out.println("playSound start");
        if (FMLCommonHandler.instance().getMinecraftServerInstance().getServer() != null)
            Minecraft.getMinecraft().player.playSound(e, 1.0F, 1.0F);
    }

    public static int CalculateGenerationHeight(World world, int x, int z) {
        int y = world.getHeight();

        while (y-- >= 0) {
            Block block = world.getBlockState(new BlockPos(x,y,z)).getBlock();
            if (block != Blocks.AIR) {
                break;
            }
        }
//        System.out.println("Calculate: " + y);
        return y;
    }

    public static void SOUT2DArray(byte[][] array) {
        for (byte y = 0; y != array.length; y++) {
            System.out.println(Arrays.toString(array[y]));
        }
    }

    public static void SOUT2DArray(IBlockState[][] array) {
        for (byte y = 0; y != array.length; y++) {
            System.out.println(Arrays.toString(array[y]));
        }
    }


    public static void genAncientPortal(World world, int x, int y, int z, boolean isSetCube) {
        if (isSetCube) {
            GenStructure.generateStructure(world, x, Handler.CalculateGenerationHeight(world, x + 3, z + 3) + 1, z, "ancient_portal_air_cube");
        }
        while (CalculateGenerationHeight(world, x + 3, z + 3) > 0) {
            GenStructure.generateStructure(world, x, CalculateGenerationHeight(world, x + 3, z + 3) + y, z, "ancient_portal");
        }
        GenStructure.generateStructure(world, x, 0, z, "ancient_portal_floor");
    }

    public static ArrayList<String> isPlayerUseUnresolvedItems(EntityPlayer player) {
        ArrayList<String> ID = new ArrayList<>();
        String[] modId = Configs.PortalSettings.modId;
        boolean is = true;

        for (ItemStack itemStack : player.inventory.mainInventory) {
            for (byte i = 0; i != modId.length; i++) {
                is = Objects.equals(itemStack.getItem().getCreatorModId(itemStack), modId[i]);
                if (is) {
                    break;
                }
            }
            if (!is) {
                ID.add(itemStack.getItem().getCreatorModId(itemStack) + ":" + itemStack.getItem().getUnlocalizedName().replaceAll("item.", ""));
            }
        }
        for (ItemStack itemStack : player.inventory.armorInventory) {
            for (byte i = 0; i != modId.length; i++) {
                is = Objects.equals(itemStack.getItem().getCreatorModId(itemStack), modId[i]);
                if (is) {
                    break;
                }
            }
            if (!is) {
                ID.add(itemStack.getItem().getUnlocalizedName());
            }
        }

        return ID;
    }
}
