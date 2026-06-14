package com.artur114.thaumrota.common.handlers;

import com.artur114.bananalib.mc.BananaMC;
import com.artur114.thaumrota.common.config.RotAConfigs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.BiomeDictionary;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;

import java.util.*;
import java.util.stream.Collectors;

@Deprecated
public class MiscHandler {
    public static List<String> isPlayerUseUnresolvedItems(EntityPlayer player) {
        List<String> ID = new ArrayList<>();
        String[] modId = RotAConfigs.PortalSettings.modId;
        boolean is = true;

        for (ItemStack itemStack : player.inventory.mainInventory) {
            for (byte i = 0; i != modId.length; i++) {
                is = Objects.equals(itemStack.getItem().getCreatorModId(itemStack), modId[i]);
                if (is) {
                    break;
                }
            }
            if (!is) {
                ID.add(itemStack.getDisplayName());
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
                ID.add(itemStack.getDisplayName());
            }
        }
        return ID.stream().distinct().collect(Collectors.toList());
    }

    public static void sendMessageTranslate(EntityPlayerMP playerMP, String key) {
        String TITLE = TextFormatting.DARK_PURPLE + RotAConfigs.Any.ModChatName + TextFormatting.RESET;
        sendMessageTranslateWithChangeTitle(playerMP, key, TITLE);
    }

    public static void sendMessageTranslateWithChangeTitle(EntityPlayerMP playerMP, String key, String title) {
        playerMP.sendMessage(new TextComponentString(title + new TextComponentTranslation(key).getFormattedText()));
    }

    public static void researchAndSendMessage(EntityPlayerMP player, String key, String translateKey, TextFormatting formatting) {
        if (!ThaumcraftCapabilities.knowsResearchStrict(player, key)) {
            IPlayerKnowledge knowledge = ThaumcraftCapabilities.getKnowledge(player);
            if (knowledge.addResearch(key)) {
                knowledge.sync(player);

                player.sendStatusMessage(new TextComponentTranslation(translateKey).setStyle(new Style().setColor(formatting)), true);
            }
        }
    }

    public static void researchAndSendMessage(EntityPlayerMP player, String key, String translateKey) {
        researchAndSendMessage(player, key, translateKey, TextFormatting.DARK_PURPLE);
    }

    public static boolean isWithinRadius(double x1, double z1, double x2, double z2, double radius) {
        double dx = x1 - x2;
        double dz = z1 - z2;
        return dx * dx + dz * dz <= radius * radius;
    }

    public static boolean getIgnoringChance(int percentage, Random rand) {
        if (percentage <= 0) {
            return true;
        }
        if (percentage >= 100) {
            return false;
        }
        return rand.nextInt(100) < percentage;
    }


    public static BlockPos.MutableBlockPos addToMutableBP(BlockPos.MutableBlockPos mPos, int addX, int addY, int addZ) {
        return mPos.setPos(mPos.getX() + addX, mPos.getY() + addY, mPos.getZ() + addZ);
    }

    @Nullable
    public static String getAspectChatColor(Aspect aspect) {
        String color = aspect.getChatcolor();
        if (color != null) {
            return "\u00a7" + color;
        }
        if (aspect == Aspect.DARKNESS) {
            return TextFormatting.BLACK.toString();
        } else if (aspect == Aspect.ELDRITCH) {
            return TextFormatting.DARK_PURPLE.toString();
        }
        return null;
    }

    public static boolean arrayContainsAny(byte[] array, byte... params) {
        for (int i : array) {
            for (int j : params) {
                if (i == j) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean arrayContains(byte[] array, byte param) {
        for (int i : array) {
            if (i == param) {
                return true;
            }
        }
        return false;
    }

    public static boolean arrayContains(int[] array, int param) {
        for (int i : array) {
            if (i == param) {
                return true;
            }
        }
        return false;
    }

    public static boolean isCollide(int pointX, int pointY, int point1X, int point1Y, int offset) {
        if (offset == 0) {
            return point1X == pointX && point1Y == pointY;
        }
        return Math.abs(point1X - pointX) <= offset && Math.abs(point1Y - pointY) <= offset;
    }

    public static byte getBiomeIdOnPos(World world, BlockPos pos) {
        return world.getChunkFromBlockCoords(pos).getBiomeArray()[(pos.getX() & 15) + (pos.getZ() & 15) * 16];
    }

    public static boolean fastCheckChunkContainsBiomeType(Chunk chunk, BiomeDictionary.Type type) {
        byte[] chunkBiomeArray = chunk.getBiomeArray();
        if (BananaMC.biomeHasType(chunkBiomeArray[0], type)) return true;
        if (BananaMC.biomeHasType(chunkBiomeArray[15 * 16], type)) return true;
        if (BananaMC.biomeHasType(chunkBiomeArray[15 + 15 * 16], type)) return true;
        return BananaMC.biomeHasType(chunkBiomeArray[15], type);
    }

    public static boolean fullCheckChunkContainsBiomeType(Chunk chunk, BiomeDictionary.Type type) {
        byte[] chunkBiomeArray = chunk.getBiomeArray();
        for (byte b : chunkBiomeArray) {
            if (BananaMC.biomeHasType(b, type)) {
                return true;
            }
        }
        return false;
    }

    public static boolean fastCheckChunkContainsAnyOnBiomeArray(Chunk chunk, byte[] biomeArray) {
        byte[] chunkBiomeArray = chunk.getBiomeArray();
        return arrayContainsAny(biomeArray, chunkBiomeArray[0], chunkBiomeArray[15 * 16], chunkBiomeArray[15 + 15 * 16], chunkBiomeArray[15]);
    }

    public static boolean fullCheckChunkContainsAnyOnBiomeArray(Chunk chunk, byte[] biomeArray) {
        byte[] chunkBiomeArray = chunk.getBiomeArray();
        return arrayContainsAny(chunkBiomeArray, biomeArray);
    }

    public static boolean intTagListContains(NBTTagList list, int value) {
        if (list.hasNoTags()) {
            return false;
        }
        if (list.getTagType() != 3) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i != list.tagCount(); i++) {
            int v = list.getIntAt(i);

            if (v == value) {
                return true;
            }
        }

        return false;
    }

    public static long chunkPosAsLong(ChunkPos chunkPos) {
        return (long) chunkPos.x & 4294967295L | ((long) chunkPos.z & 4294967295L) << 32;
    }

    public static ChunkPos chunkPosFromLong(long data) {
        return new ChunkPos((int) (data), (int) (data >> 32));
    }
}
