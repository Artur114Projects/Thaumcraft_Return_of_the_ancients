package com.artur.returnoftheancients.handlers;

import com.artur.returnoftheancients.ancientworldgeneration.main.entry.AncientEntry;
import com.artur.returnoftheancients.ancientworldgeneration.structurebuilder.TRAStructure;
import com.artur.returnoftheancients.items.ItemSoulBinder;
import com.artur.returnoftheancients.misc.SoundTRA;
import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.generation.generators.GenStructure;
import com.artur.returnoftheancients.main.MainR;
import com.artur.returnoftheancients.network.ClientPacketMisc;
import com.artur.returnoftheancients.network.ClientPacketPlayerNBTData;
import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class HandlerR {
    private static ArrayList<String> ID = new ArrayList<>();
    public static boolean isTriggered = false;

    public static int genRandomIntRange(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
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
        ID.clear();
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

    public static boolean isGoodNBTTagMisc(NBTTagCompound nbt) {
        final String[] Tag = new String[] {
                "setGuiState",
                "sendAncientWorldLoadMessage",
                "sendMessage",
                "sendMessageTranslate",
                "injectPhase",
                "injectPercentages",
                "changeTitle",
                "playSound",
                "stopSound"
        };
        return isGoodNBTTagBase(nbt, Tag, false);
    }

    public static void setStartUpNBT(EntityPlayerMP playerMP, boolean data) {
        playerMP.getEntityData().setBoolean("startUpNBT", data);
        MainR.NETWORK.sendTo(new ClientPacketPlayerNBTData(HandlerR.createPlayerDataPacketTag((byte) 1, "startUpNBT", data)), playerMP);
    }

    public static void setLoadingGuiState(EntityPlayerMP playerMP, boolean state) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setBoolean("setGuiState", state);
        MainR.NETWORK.sendTo(new ClientPacketMisc(nbt), playerMP);
    }

    public static void sendAllWorldLoadMessage(boolean state) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setBoolean("sendAncientWorldLoadMessage", state);
        MainR.NETWORK.sendToAll(new ClientPacketMisc(nbt));
    }

    public static void injectPercentagesOnClient(EntityPlayerMP playerMP, int x, int y) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setByte("injectPercentages", (byte) Math.round(((16 * y) + (x + 1)) / 2.89D));
        MainR.NETWORK.sendTo(new ClientPacketMisc(nbt), playerMP);
    }

    public static void injectPhaseOnClient(EntityPlayerMP playerMP, byte PHASE) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setByte("injectPhase", PHASE);
        MainR.NETWORK.sendTo(new ClientPacketMisc(nbt), playerMP);
    }

    public static void sendMessageString(EntityPlayerMP playerMP, String message) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("sendMessage", message);
        MainR.NETWORK.sendTo(new ClientPacketMisc(nbt), playerMP);
    }

    public static void sendAllMessageStringNoTitle(String message) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("sendMessage", message);
        nbt.setString("changeTitle", "");
        MainR.NETWORK.sendToAll(new ClientPacketMisc(nbt));
    }

    public static void sendMessageTranslate(EntityPlayerMP playerMP, String key) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("sendMessageTranslate", key);
        MainR.NETWORK.sendTo(new ClientPacketMisc(nbt), playerMP);
    }

    public static void sendMessageTranslateWithChangeTitle(EntityPlayerMP playerMP, String key, String title) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("sendMessageTranslate", key);
        nbt.setString("changeTitle", title);
        MainR.NETWORK.sendTo(new ClientPacketMisc(nbt), playerMP);
    }

    public static boolean isNumber(char c) {
        char[] numbers = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        for (char c1 : numbers) {
            if (c == c1) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNumber(String s) {
        char[] numbers = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        for (int i = 0; i != s.length(); i++) {
            boolean is = false;
            for (char c1 : numbers) {
                if (s.charAt(i) == c1) {
                    is = true;
                    break;
                }
            }
            if (!is) {
                return false;
            }
        }
        return true;
    }

    public static final String[] defaultCountDifficultyData = new String[] {
            "players=12, effect=invisibility, amplifier=1r0",
            "players=12, effect=resistance, amplifier=3p4",
            "players=12, effect=regeneration, amplifier=3p6",
            "players=12, effect=strength, amplifier=6p",
            "players=12, effect=fireResistance, amplifier=0",

            "players=6, effect=resistance, amplifier=3p4",
            "players=6, effect=regeneration, amplifier=3p6",
            "players=6, effect=invisibility, amplifier=4r0",
            "players=6, effect=strength, amplifier=6p",
            "players=6, effect=fireResistance, amplifier=0",

            "players=3, effect=resistance, amplifier=1",
            "players=3, effect=regeneration, amplifier=1",
            "players=3, effect=strength, amplifier=1",
            "players=3, effect=fireResistance, amplifier=0",

            "players=2, effect=resistance, amplifier=0",
            "players=2, effect=regeneration, amplifier=0",
            "players=2, effect=strength, amplifier=0",

            "players=1, effect=speed, amplifier=1"
    };

    public static void playSound(EntityPlayerMP playerMP, SoundTRA sound) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("playSound", sound.NAME);
        MainR.NETWORK.sendTo(new ClientPacketMisc(nbt), playerMP);
    }

    public static List<String> uuidKeySetToList(Set<String> set) {
        String[] keys = set.toArray(new String[0]);
        List<String> keysF = new ArrayList<>();
        for (String key : keys) {
            key = key.replaceAll("Most", "");
            key = key.replaceAll("Least", "");
            keysF.add(key);
        }
        keysF = keysF.stream().distinct().collect(Collectors.toList());
        return keysF;
    }

    public static List<String> uuidKeySetToList(Set<String> set, TextFormatting textFormatting) {
        String[] keys = set.toArray(new String[0]);
        List<String> keysF = new ArrayList<>();
        for (String key : keys) {
            key = key.replaceAll("Most", "");
            key = key.replaceAll("Least", "");
            keysF.add(textFormatting + key);
        }
        keysF = keysF.stream().distinct().collect(Collectors.toList());
        return keysF;
    }

    public static ItemStack getSoulBinder(EntityPlayerMP player) {
        for (ItemStack stack : player.inventory.mainInventory) {
            if (stack.getItem() instanceof ItemSoulBinder) return stack;
        }
        return null;
    }

    public static boolean isSoulBinderFull(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemSoulBinder)) {
            return false;
        }
        return stack.getOrCreateSubCompound(Referense.MODID).getBoolean("isFull");
    }

    public static void playSoundToTargetPoint(SoundTRA sound, int dimension, double x, double y, double z, double range) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("playSound", sound.NAME);
        MainR.NETWORK.sendToAllAround(new ClientPacketMisc(nbt), new NetworkRegistry.TargetPoint(dimension, x, y, z, range));
    }

    public static void stopSoundToTargetPoint(SoundTRA sound, int dimension, double x, double y, double z, double range) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("stopSound", sound.NAME);
        MainR.NETWORK.sendToAllAround(new ClientPacketMisc(nbt), new NetworkRegistry.TargetPoint(dimension, x, y, z, range));
    }

    public static Block getBlockByString(String block) {
        ResourceLocation resourcelocation = new ResourceLocation(block);
        if (!Block.REGISTRY.containsKey(resourcelocation)) {
            throw new RuntimeException(block);
        } else {
            return Block.REGISTRY.getObject(resourcelocation);
        }
    }


}
