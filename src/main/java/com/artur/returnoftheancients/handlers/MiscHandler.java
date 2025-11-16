package com.artur.returnoftheancients.handlers;

import com.artur.returnoftheancients.generation.portal.base.AncientPortal;
import com.artur.returnoftheancients.items.ItemSoulBinder;
import com.artur.returnoftheancients.misc.SoundTRA;
import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.ancientworldlegacy.legacy.GenStructure;
import com.artur.returnoftheancients.main.MainR;
import com.artur.returnoftheancients.network.ClientPacketMisc;
import com.artur.returnoftheancients.network.ClientPacketPlayerNBTData;
import com.artur.returnoftheancients.referense.Referense;
import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;

import java.util.*;
import java.util.stream.Collectors;

public class MiscHandler {

    public static int genRandomIntRange(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public static int calculateGenerationHeight(World world, BlockPos pos) {
        return calculateGenerationHeight(world, pos.getX(), pos.getZ());
    }

    public static int calculateGenerationHeight(World world, int x, int z) {
        return calculateGenerationHeight(world, x, z, Blocks.AIR, Blocks.BEDROCK);
    }

    public static int calculateGenerationHeight(World world, int x, int z, Block... ignoringBlocks) {
        UltraMutableBlockPos pos = UltraMutableBlockPos.getBlockPosFromPoll().setPos(x, world.getHeight(), z);

        while (pos.getY() >= 0) {
            Block block = world.getBlockState(pos).getBlock();
            boolean flag = true;
            for (Block ignoring : ignoringBlocks) {
                if (block == ignoring) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                break;
            }
            pos.down();
        }

        int y = pos.getY();
        UltraMutableBlockPos.returnBlockPosToPoll(pos);
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


    public static void genAncientPortal(World world, int x, int z, boolean isSetCube) {
        int y = TRAConfigs.PortalSettings.y;
        int fx = (16 * x) + 5;
        int fz = (16 * z) + 5;
        if (isSetCube) {
            GenStructure.generateStructure(world, fx, MiscHandler.calculateGenerationHeight(world, fx + 3, fz + 3) + 1, fz, "ancient_portal_air_cube");
        }
        while (calculateGenerationHeight(world, fx + 3, fz + 3) > 0) {
            GenStructure.generateStructure(world, fx, calculateGenerationHeight(world, fx + 3, fz + 3) + y, fz, "ancient_portal");
        }
        GenStructure.generateStructure(world, fx, 0, fz, "ancient_portal_floor");
    }

    public static List<String> isPlayerUseUnresolvedItems(EntityPlayer player) {
        List<String> ID = new ArrayList<>();
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

    /**
     * Creates a player data packet with a specified boolean value.
     *
     * @param tagSetName  Name of the NBT tag to set.
     * @param data        Boolean value to include in the packet.
     * @return An NBTTagCompound containing the specified data.
     */
    public static NBTTagCompound createPlayerDataPacketTag(String tagSetName, boolean data) {
        NBTTagCompound nbt = createRawPlayerDataPacketTag((byte) 1, tagSetName);
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
        final String[] Tag = new String[]{"dataIndex", "tagSetName", "data"};
        if (nbt.getByte("dataIndex") > 3) {
            return false;
        }
        return isGoodNBTTagBase(nbt, Tag, true);
    }

    public static boolean isGoodNBTTagMisc(NBTTagCompound nbt) {
        final String[] Tag = new String[]{
                "setGuiState",
                "sendAncientWorldLoadMessage",
                "sendMessage",
                "sendMessageTranslate",
                "injectPhase",
                "injectPercentages",
                "changeTitle",
                "playSound",
                "stopSound",
                "sendStatusMessage"
        };
        return isGoodNBTTagBase(nbt, Tag, false);
    }

    public static void setStartUpNBT(EntityPlayerMP playerMP, boolean data) {
        NBTTagCompound dataNBT = playerMP.getEntityData();
        dataNBT.setBoolean("startUpNBT", data);
        MainR.NETWORK.sendTo(new ClientPacketPlayerNBTData(MiscHandler.createPlayerDataPacketTag("startUpNBT", data)), playerMP);
    }

    public static void setTeleportingToHomeNBT(EntityPlayerMP playerMP, boolean data) {
        playerMP.getEntityData().setBoolean(AncientPortal.tpToHomeNBT, data);
        MainR.NETWORK.sendTo(new ClientPacketPlayerNBTData(MiscHandler.createPlayerDataPacketTag(AncientPortal.tpToHomeNBT, data)), playerMP);
    }

    public static void setLoadingGuiState(EntityPlayerMP playerMP, boolean state, boolean isTeam) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setBoolean("setGuiState", state);
        nbt.setBoolean("isTeam", isTeam);
        MainR.NETWORK.sendTo(new ClientPacketMisc(nbt), playerMP);
    }

    public static void injectPercentagesOnClient(EntityPlayerMP playerMP, int x, int y) {
//        NBTTagCompound nbt = new NBTTagCompound();
//        nbt.setByte("injectPercentages", (byte) Math.round(((16 * y) + (x + 1)) / 2.89D));
//        MainR.NETWORK.sendTo(new ClientPacketMisc(nbt), playerMP);
    }

    public static void injectPhaseOnClient(EntityPlayerMP playerMP, byte PHASE) {
//        NBTTagCompound nbt = new NBTTagCompound();
//        nbt.setByte("injectPhase", PHASE);
//        MainR.NETWORK.sendTo(new ClientPacketMisc(nbt), playerMP);
    }

    public static void injectNamesOnClient(List<String> names, EntityPlayerMP player) {
        NBTTagCompound nbt = new NBTTagCompound();
        NBTTagList list = new NBTTagList();
        for (String name : names) {
            list.appendTag(new NBTTagString(name));
        }
        nbt.setTag("injectNamesOnClient", list);
        MainR.NETWORK.sendTo(new ClientPacketMisc(nbt), player);
    }

    public static void sendMessageString(EntityPlayerMP playerMP, String message) {
        playerMP.sendMessage(new TextComponentString(message));
    }

    public static void sendMessageString(EntityPlayerMP playerMP, String message, TextFormatting formatting) {
        playerMP.sendMessage(new TextComponentString(message).setStyle(new Style().setColor(formatting)));
    }

    public static void sendAllMessageStringNoTitle(String message) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("sendMessage", message);
        nbt.setString("changeTitle", "");
        MainR.NETWORK.sendToAll(new ClientPacketMisc(nbt));
    }

    public static void sendMessageTranslate(EntityPlayerMP playerMP, String key) {
        String TITLE = TextFormatting.DARK_PURPLE + TRAConfigs.Any.ModChatName + TextFormatting.RESET;
        sendMessageTranslateWithChangeTitle(playerMP, key, TITLE);
    }

    public static void sendMessageTranslateToAll(String key) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("sendMessageTranslate", key);
        MainR.NETWORK.sendToAll(new ClientPacketMisc(nbt));
    }

    public static void sendMessageTranslateWithChangeTitle(EntityPlayerMP playerMP, String key, String title) {
        playerMP.sendMessage(new TextComponentString(title + new TextComponentTranslation(key).getFormattedText()));
    }

    public static boolean isNumber(char c) {
        return c < 10;
    }

    public static boolean isNumber(String s) {
        for (int i = 0; i != s.length(); i++) {
            if (s.charAt(i) != '-' && !isNumber(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static void playSound(EntityPlayerMP playerMP, SoundTRA sound) {
        playerMP.playSound(sound.SOUND, 1.0F, 1.0F);
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

    public static @Nullable ItemStack getSoulBinder(EntityPlayerMP player) {
        for (ItemStack stack : player.inventory.mainInventory) {
            if (stack.getItem() instanceof ItemSoulBinder) return stack;
        }
        return null;
    }

    public static boolean isHasItem(EntityPlayer player, Item item) {
        for (ItemStack stack : player.inventory.mainInventory) {
            if (stack.getItem().equals(item)) return true;
        }
        return false;
    }

    public static boolean isSoulBinderFull(@Nullable ItemStack stack) {
        if (stack == null) {
            return false;
        }
        if (!(stack.getItem() instanceof ItemSoulBinder)) {
            return false;
        }
        return stack.getOrCreateSubCompound(Referense.MODID).getBoolean("isFull");
    }

    public static void playSoundToTargetPoint(SoundTRA sound, int dimension, BlockPos pos, double range) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("playSound", sound.NAME);
//        nbt.setLong("soundPos", pos.toLong());
        MainR.NETWORK.sendToAllAround(new ClientPacketMisc(nbt), new NetworkRegistry.TargetPoint(dimension, pos.getX(), pos.getY(), pos.getZ(), range));
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

    public static void research(EntityPlayerMP player, String key) {
        if (!ThaumcraftCapabilities.knowsResearchStrict(player, key)) {
            IPlayerKnowledge knowledge = ThaumcraftCapabilities.getKnowledge(player);
            if (knowledge.addResearch(key)) {
                knowledge.sync(player);
            }
        }
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

    public static void researchTC(EntityPlayerMP player, String key) {
        if (!ThaumcraftCapabilities.knowsResearchStrict(player, key)) {
            IPlayerKnowledge knowledge = ThaumcraftCapabilities.getKnowledge(player);
            if (knowledge.addResearch(key)) {
                knowledge.sync(player);
            }
        }
    }

    public static boolean isWithinRadius(double x1, double z1, double x2, double z2, double radius) {
        double dx = x1 - x2;
        double dz = z1 - z2;
        return dx * dx + dz * dz <= radius * radius;
    }

    public static boolean getChance(int percentage, Random rand) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100");
        }
        return rand.nextInt(100) < percentage;
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


    public static int foundMostSmallUniqueIntInList(List<Integer> list) {
        if (list.isEmpty()) return 0;
        boolean isFound;
        int value = 0;
        while (true) {
            isFound = true;
            for (Integer i : list) {
                if (i == value) {
                    isFound = false;
                    break;
                }
            }
            if (isFound) {
                return value;
            }
            value++;
        }
    }

    public static int foundMostSmallUniqueIntInSet(Set<Integer> set) {
        if (set.isEmpty()) return 0;
        boolean isFound;
        int value = 0;
        while (true) {
            isFound = true;
            for (Integer i : set) {
                if (i == value) {
                    isFound = false;
                    break;
                }
            }
            if (isFound) {
                return value;
            }
            value++;
        }
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

    public static boolean fastCheckChunkContainsAnyOnBiomeArray(Chunk chunk, byte[] biomeArray) {
        byte[] chunkBiomeArray = chunk.getBiomeArray();
        return arrayContainsAny(biomeArray, chunkBiomeArray[0], chunkBiomeArray[15 * 16], chunkBiomeArray[15 + 15 * 16], chunkBiomeArray[15]);
    }

    public static boolean fullCheckChunkContainsAnyOnBiomeArray(Chunk chunk, byte[] biomeArray) {
        byte[] chunkBiomeArray = chunk.getBiomeArray();
        return arrayContainsAny(chunkBiomeArray, biomeArray);
    }

    public static float interpolate(float start, float end, float pct) {
        return start + (end - start) * pct;
    }

    public static double interpolate(double start, double end, float pct) {
        return start + (end - start) * pct;
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

    public static int mod(double v) {
        if (v < 0) {
            return -1;
        } else {
            return 1;
        }
    }
}
