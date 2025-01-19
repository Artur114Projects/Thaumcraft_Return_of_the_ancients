package com.artur.returnoftheancients.handlers;

import com.artur.returnoftheancients.generation.generators.portal.base.AncientPortal;
import com.artur.returnoftheancients.items.ItemSoulBinder;
import com.artur.returnoftheancients.misc.SoundTRA;
import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.generation.generators.GenStructure;
import com.artur.returnoftheancients.main.MainR;
import com.artur.returnoftheancients.network.ClientPacketMisc;
import com.artur.returnoftheancients.network.ClientPacketPlayerNBTData;
import com.artur.returnoftheancients.referense.Referense;
import com.artur.returnoftheancients.utils.math.UltraMutableBlockPos;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class HandlerR {
    private static final ArrayList<String> ID = new ArrayList<>();

    public static int genRandomIntRange(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public static int calculateGenerationHeight(World world, int x, int z) {
        UltraMutableBlockPos pos = new UltraMutableBlockPos(x, world.getHeight(), z);

        while (pos.getY() >= 0) {
            Block block = world.getBlockState(pos).getBlock();
            if (block != Blocks.AIR && block != Blocks.BEDROCK) {
                break;
            }
            pos.add(0, -1, 0);
        }
        return pos.getY();
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
            GenStructure.generateStructure(world, fx, HandlerR.calculateGenerationHeight(world, fx + 3, fz + 3) + 1, fz, "ancient_portal_air_cube");
        }
        while (calculateGenerationHeight(world, fx + 3, fz + 3) > 0) {
            GenStructure.generateStructure(world, fx, calculateGenerationHeight(world, fx + 3, fz + 3) + y, fz, "ancient_portal");
        }
        GenStructure.generateStructure(world, fx, 0, fz, "ancient_portal_floor");
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
                ID.add(I18n.format(itemStack.getItem().getUnlocalizedName() + ".name"));
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
                ID.add(I18n.format(itemStack.getItem().getUnlocalizedName() + ".name"));
            }
        }
        return ID;
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
        MainR.NETWORK.sendTo(new ClientPacketPlayerNBTData(HandlerR.createPlayerDataPacketTag("startUpNBT", data)), playerMP);
    }

    public static void setTeleportingToHomeNBT(EntityPlayerMP playerMP, boolean data) {
        playerMP.getEntityData().setBoolean(AncientPortal.tpToHomeNBT, data);
        MainR.NETWORK.sendTo(new ClientPacketPlayerNBTData(HandlerR.createPlayerDataPacketTag(AncientPortal.tpToHomeNBT, data)), playerMP);
    }

    public static void setLoadingGuiState(EntityPlayerMP playerMP, boolean state, boolean isTeam) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setBoolean("setGuiState", state);
        nbt.setBoolean("isTeam", isTeam);
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
        char[] numbers = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        for (char c1 : numbers) {
            if (c == c1) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNumber(String s) {
        char[] numbers = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
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

    public static final String[] defaultCountDifficultyData = new String[]{
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

    @SideOnly(Side.CLIENT)
    private void renderBox(Tessellator tessellator, BufferBuilder bufferBuilder, double x, double y, double z, double x1, double y1, double z1, int a, int b, int c) {
        GlStateManager.glLineWidth(2.0F);
        bufferBuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferBuilder.pos(x, y, z).color((float) b, (float) b, (float) b, 0.0F).endVertex();
        bufferBuilder.pos(x, y, z).color(b, b, b, a).endVertex();
        bufferBuilder.pos(x1, y, z).color(b, c, c, a).endVertex();
        bufferBuilder.pos(x1, y, z1).color(b, b, b, a).endVertex();
        bufferBuilder.pos(x, y, z1).color(b, b, b, a).endVertex();
        bufferBuilder.pos(x, y, z).color(c, c, b, a).endVertex();
        bufferBuilder.pos(x, y1, z).color(c, b, c, a).endVertex();
        bufferBuilder.pos(x1, y1, z).color(b, b, b, a).endVertex();
        bufferBuilder.pos(x1, y1, z1).color(b, b, b, a).endVertex();
        bufferBuilder.pos(x, y1, z1).color(b, b, b, a).endVertex();
        bufferBuilder.pos(x, y1, z).color(b, b, b, a).endVertex();
        bufferBuilder.pos(x, y1, z1).color(b, b, b, a).endVertex();
        bufferBuilder.pos(x, y, z1).color(b, b, b, a).endVertex();
        bufferBuilder.pos(x1, y, z1).color(b, b, b, a).endVertex();
        bufferBuilder.pos(x1, y1, z1).color(b, b, b, a).endVertex();
        bufferBuilder.pos(x1, y1, z).color(b, b, b, a).endVertex();
        bufferBuilder.pos(x1, y, z).color(b, b, b, a).endVertex();
        bufferBuilder.pos(x1, y, z).color((float) b, (float) b, (float) b, 0.0F).endVertex();
        tessellator.draw();
        GlStateManager.glLineWidth(1.0F);
    }

    public static void renderPrimitive(int x, int x1, int y, int y1, double startU, double endU, double startV, double endV) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        builder.pos(x, y1, 0).tex(startU, endV).endVertex();
        builder.pos(x1, y1, 0).tex(endU, endV).endVertex();
        builder.pos(x1, y, 0).tex(endU, startV).endVertex();
        builder.pos(x, y, 0).tex(startU, startV).endVertex();
        tessellator.draw();
    }

    @SideOnly(Side.CLIENT)
    public static void renderTextureAtlas(int posX, int posY, float startDrawX, float startDrawY, float textureSizeX, float textureSizeY, float drawAreaWidth, float drawAreaHeight, float scale) {
        startDrawX = (scale * startDrawX);
        startDrawY = (scale * startDrawY);
        textureSizeX = (scale * textureSizeX);
        textureSizeY = (scale * textureSizeY);
        drawAreaWidth = (scale * drawAreaWidth);
        drawAreaHeight = (scale * drawAreaHeight);

        float posX1 = posX + drawAreaWidth;
        float posY1 = posY + drawAreaHeight;

        float iX = startDrawX / drawAreaWidth;
        float iY = startDrawY / drawAreaHeight;

        double endU = (double) (drawAreaWidth * (iX + 1)) / textureSizeX;
        double startU = (double) (drawAreaWidth * iX) / textureSizeX;

        double endV = (double) (drawAreaHeight * (iY + 1)) / textureSizeY;
        double startV = (double) (drawAreaHeight * iY) / textureSizeY;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        builder.pos(posX, posY1, 0).tex(startU, endV).endVertex();
        builder.pos(posX1, posY1, 0).tex(endU, endV).endVertex();
        builder.pos(posX1, posY, 0).tex(endU, startV).endVertex();
        builder.pos(posX, posY, 0).tex(startU, startV).endVertex();
        tessellator.draw();
    }

    @SideOnly(Side.CLIENT)
    public static void renderTextureAtlas(int posX, int posY, float startDrawX, float startDrawY, float textureSizeX, float textureSizeY, float drawAreaWidth, float drawAreaHeight) {
        renderTextureAtlas(posX, posY, startDrawX, startDrawY, textureSizeX, textureSizeY, drawAreaWidth, drawAreaHeight, 1);
    }

    /**
     * @param posX drawing start position
     * @param posY drawing start position
     * @param startDrawX drawing start position in texture
     * @param startDrawY drawing start position in texture
     * @param textureSizeX texture size
     * @param textureSizeY texture size
     * @param scale scale
     */
    @SideOnly(Side.CLIENT)
    public static void renderQuadTextureAtlas(int posX, int posY, float startDrawX, float startDrawY, float textureSizeX, float textureSizeY, float drawQuadSize, float scale) {
        startDrawX = (scale * startDrawX);
        startDrawY = (scale * startDrawY);
        textureSizeX = (scale * textureSizeX);
        textureSizeY = (scale * textureSizeY);

        float posX1 = posX + drawQuadSize;
        float posY1 = posY + drawQuadSize;

        float iX = startDrawX / drawQuadSize;
        float iY = startDrawY / drawQuadSize;

        double endU = (double) (drawQuadSize * (iX + 1)) / textureSizeX;
        double startU = (double) (drawQuadSize * iX) / textureSizeX;

        double endV = (double) (drawQuadSize * (iY + 1)) / textureSizeY;
        double startV = (double) (drawQuadSize * iY) / textureSizeY;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        builder.pos(posX, posY1, 0).tex(startU, endV).endVertex();
        builder.pos(posX1, posY1, 0).tex(endU, endV).endVertex();
        builder.pos(posX1, posY, 0).tex(endU, startV).endVertex();
        builder.pos(posX, posY, 0).tex(startU, startV).endVertex();
        tessellator.draw();
    }

    @SideOnly(Side.CLIENT)
    public static void renderQuadTextureAtlas(int posX, int posY, float startDrawX, float startDrawY, float textureSizeX, float textureSizeY, float scale) {
        renderQuadTextureAtlas(posX, posY, startDrawX, startDrawY, textureSizeX, textureSizeY, Math.min(textureSizeX, textureSizeY), scale);
    }

    @SideOnly(Side.CLIENT)
    public static void renderQuadTextureAtlas(int posX, int posY, int startDrawX, int startDrawY, int textureSizeX, int textureSizeY) {
        renderQuadTextureAtlas(posX, posY, startDrawX, startDrawY, textureSizeX, textureSizeY, 1);
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

    public static String kJToString(float count) {
        String[] prefixes = I18n.format(Referense.MODID + ".kJ.prefixes").split("/");
        float localCount = count;
        if (localCount < 1) {
            return ((int) (localCount * 1000)) + prefixes[0];
        }
        for (int i = 1; i != prefixes.length; i++) {
            if (localCount < 1000) {
                return (((int) (localCount * 100)) / 100.0F) + prefixes[i];
            }
            localCount /= 1000;
        }
        return "";
    }

    public static String kWToString(float count) {
        String[] prefixes = I18n.format(Referense.MODID + ".kW.prefixes").split("/");
        float localCount = count;
        if (localCount < 1) {
            return ((int) (localCount * 1000)) + prefixes[0];
        }
        for (int i = 1; i != prefixes.length; i++) {
            if (localCount < 1000) {
                return (((int) (localCount * 100)) / 100.0F) + prefixes[i];
            }
            localCount /= 1000;
        }
        return "";
    }

    public static Tuple<Integer, Integer> getTextureSize(ResourceLocation textureRL) {
        GlStateManager.pushMatrix();
        Minecraft.getMinecraft().getTextureManager().bindTexture(textureRL);
        int x = GlStateManager.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
        int y = GlStateManager.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
        Tuple<Integer, Integer> size = new Tuple<>(x, y);
        GlStateManager.popMatrix();
        return size;
    }

    public static String createDescriptor(Class<?> returnValue, Class<?>... params) {
        StringBuilder builder = new StringBuilder("(");
        for (Class<?> clas : params) {
            builder.append(formatDescriptor(clas));
            builder.append(';');
        }
        builder.append(')');
        builder.append(formatDescriptor(returnValue));
        String res = builder.toString();
        if (TRAConfigs.Any.debugMode) System.out.println("Descriptor is created {" + res + "}");
        return res;
    }

    public static String createDescriptor(Class<?> methodClass, String methodName, Class<?>... params) {
        Method[] methods = findMethods(methodClass, methodName);
        if (methods.length == 0) {
            return "null";
        }

        Method findMethod = null;
        if (params.length == 0) {
            findMethod = methods[0];
        } else {
            for (Method method : methods) {
                if (Arrays.equals(method.getParameterTypes(), params)) {
                    findMethod = method;
                    break;
                }
            }
            if (findMethod == null) {
                return "null";
            }
        }

        StringBuilder builder = new StringBuilder("(");
        for (Class<?> clas : findMethod.getParameterTypes()) {
            builder.append(formatDescriptor(clas));
            builder.append(';');
        }
        builder.append(')');
        builder.append(formatDescriptor(findMethod.getReturnType()));
        String res = builder.toString();
        if (TRAConfigs.Any.debugMode) System.out.println("Descriptor is created {" + res + "}");
        return res;
    }

    public static String formatDescriptor(Class<?> param) {
        if (param == boolean.class) {
            return "Z";
        } else if (param == byte.class) {
            return "B";
        } else if (param == char.class) {
            return "C";
        } else if (param == double.class) {
            return "D";
        } else if (param == float.class) {
            return "F";
        } else if (param == int.class) {
            return "I";
        } else if (param == long.class) {
            return "J";
        } else if (param == short.class) {
            return "S";
        } else if (param == void.class) {
            return "V";
        }

        String name = param.getName();
        if (!name.contains("[L")) {
            name = "L" + name;
        }
        name = name.replaceAll("\\.", "/");
        return name;
    }

    public static Method[] findMethods(Class<?> methodClass, String methodName) {
        ArrayList<Method> methods = new ArrayList<>();
        for (Method method : methodClass.getDeclaredMethods()) {
            if (method.getName().equals(methodName)) {
                methods.add(method);
            }
        }
        Class<?> superClass = methodClass.getSuperclass();
        while (superClass != null) {
            for (Method method : superClass.getDeclaredMethods()) {
                if (method.getName().equals(methodName)) {
                    methods.add(method);
                }
            }
            superClass = superClass.getSuperclass();
        }
        return methods.toArray(new Method[0]);
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

    public static int elementCountOnArray(byte[] array, byte element) {
        int ret = 0;
        for (byte b : array) {
            if (b == element) {
                ret++;
            }
        }
        return ret;
    }

    public static byte fastGetMostBiomeInChunk(Chunk chunk) {
        byte[] chunkBiomeArray = chunk.getBiomeArray();
        byte[] biomes = new byte[] {chunkBiomeArray[0], chunkBiomeArray[15 * 16], chunkBiomeArray[15 + 15 * 16], chunkBiomeArray[15]};
        int[] biomesCount = new int[] {elementCountOnArray(biomes, biomes[0]), elementCountOnArray(biomes, biomes[1]), elementCountOnArray(biomes, biomes[2]), elementCountOnArray(biomes, biomes[3])};
        return 0;
    }
}
