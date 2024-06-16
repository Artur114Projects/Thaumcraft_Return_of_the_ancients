package com.artur.returnoftheancients.handlers;

import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.generation.generators.GenStructure;
import com.artur.returnoftheancients.main.MainR;
import com.artur.returnoftheancients.network.ClientPacketLoadingGui;
import com.artur.returnoftheancients.network.ClientPacketPlayerNBTData;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.*;

public class HandlerR {

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
            GenStructure.generateStructure(world, x, HandlerR.CalculateGenerationHeight(world, x + 3, z + 3) + 1, z, "ancient_portal_air_cube");
        }
        while (CalculateGenerationHeight(world, x + 3, z + 3) > 0) {
            GenStructure.generateStructure(world, x, CalculateGenerationHeight(world, x + 3, z + 3) + y, z, "ancient_portal");
        }
        GenStructure.generateStructure(world, x, 0, z, "ancient_portal_floor");
    }

    public static ArrayList<String> isPlayerUseUnresolvedItems(EntityPlayer player) {
        ArrayList<String> ID = new ArrayList<>();
        String[] modId = TRAConfigs.PortalSettings.modId;
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


    public static NBTTagCompound createPlayerDataPacketTag(byte dataIndex, String tagSetName, boolean data) {
        NBTTagCompound nbt = createRawPlayerDataPacketTag(dataIndex, tagSetName);
        nbt.setBoolean("data", data);
        return nbt;
    }

    public static NBTTagCompound createPlayerDataPacketTag(byte dataIndex, String tagSetName, int data) {
        NBTTagCompound nbt = createRawPlayerDataPacketTag(dataIndex, tagSetName);
        nbt.setInteger("data", data);
        return nbt;
    }

    public static NBTTagCompound createPlayerDataPacketTag(byte dataIndex, String tagSetName, String data) {
        NBTTagCompound nbt = createRawPlayerDataPacketTag(dataIndex, tagSetName);
        nbt.setString("data", data);
        return nbt;
    }

    private static NBTTagCompound createRawPlayerDataPacketTag(byte dataIndex, String tagSetName) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setByte("dataIndex", dataIndex);
        nbt.setString("tagSetName", tagSetName);
        return nbt;
    }

    private static boolean isGoodNBTTagBase(NBTTagCompound nbt, String[] Tag, boolean mustContainEverything) {
        if (nbt.getKeySet().isEmpty()) {
            return false;
        }
        for (String d : nbt.getKeySet()) {
            boolean isGood = false;
            for (String gt : Tag) {
                if (d.equals(gt)) {
                    isGood = true;
                    break;
                }
            }
            if (!isGood) {
                return false;
            }
        }
        if (mustContainEverything) {
            for (String g : Tag) {
                if (!nbt.hasKey(g)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isGoodNBTTagPND(NBTTagCompound nbt) {
        final String[] Tag = new String[] {"dataIndex", "tagSetName", "data"};
        if (nbt.getByte("dataIndex") > 3) {
            return false;
        }
        return isGoodNBTTagBase(nbt, Tag, true);
    }

    public static boolean isGoodNBTTagLG(NBTTagCompound nbt) {
        final String[] Tag = new String[] {"setGuiState", "sendAncientWorldLoadMessage", "sendMessage", "sendMessageTranslate"};
        return isGoodNBTTagBase(nbt, Tag, false);
    }

    public static void setStartUpNBT(EntityPlayerMP playerMP, boolean data) {
        playerMP.getEntityData().setBoolean("startUpNBT", data);
        MainR.NETWORK.sendTo(new ClientPacketPlayerNBTData(HandlerR.createPlayerDataPacketTag((byte) 1, "startUpNBT", data)), playerMP);
    }

    public static void setLoadingGuiState(EntityPlayerMP playerMP, boolean state) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setBoolean("setGuiState", state);
        MainR.NETWORK.sendTo(new ClientPacketLoadingGui(nbt), playerMP);
    }

    public static void sendAllWorldLoadMessage(boolean state) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setBoolean("sendAncientWorldLoadMessage", state);
        MainR.NETWORK.sendToAll(new ClientPacketLoadingGui(nbt));
    }
}
